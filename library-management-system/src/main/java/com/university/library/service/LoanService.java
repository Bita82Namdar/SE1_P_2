package com.university.library.service;

import com.university.library.model.Loan;
import com.university.library.model.StudentReport;
import com.university.library.model.LibraryStats;
import com.university.library.model.StudentDelayReport;
import com.university.library.repository.LoanRepository;
import com.university.library.repository.BookRepository;
import com.university.library.repository.UserRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoanService {
    private LoanRepository loanRepository;
    private BookRepository bookRepository;
    private UserRepository userRepository;
    private StudentService studentService;
    private BookService bookService;
    
    // Constructor اصلی
    public LoanService() {
        this.loanRepository = LoanRepository.getInstance();
        this.bookRepository = BookRepository.getInstance();
        this.userRepository = UserRepository.getInstance();
        this.studentService = new StudentService(userRepository);
        this.bookService = new BookService(bookRepository);
    }
    
    // Constructor برای تست
    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, 
                      UserRepository userRepository, StudentService studentService,
                      BookService bookService) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.studentService = studentService;
        this.bookService = bookService;
    }
    
    // ========== پیاده‌سازی سناریوها ==========
    
    /**
     * سناریو 1-3: دانشجوی فعال برای کتاب موجود درخواست امانت می‌دهد
     */
    public Loan createBorrowRequest(String loanId, String studentId, String bookId,
                                   LocalDate startDate, LocalDate endDate) {
        
        // بررسی سناریو 3-2: فعال بودن دانشجو
        if (!studentService.isStudentActive(studentId)) {
            throw new RuntimeException("دانشجو غیرفعال است");
        }
        
        // بررسی سناریو 3-3: موجود بودن کتاب
        if (!bookService.isBookAvailable(bookId)) {
            throw new RuntimeException("کتاب موجود نیست");
        }
        
        // ایجاد درخواست امانت
        Loan loan = Loan.createBorrowRequest(loanId, studentId, bookId, startDate, endDate);
        
        // ذخیره درخواست
        return loanRepository.save(loan);
    }
    
    /**
     * سناریو 3-4: تایید درخواست امانت معتبر
     * سناریو 3-5: اگر قبلاً تایید شده باشد Exception می‌دهد
     */
    public Loan approveBorrowRequest(String loanId, String employeeId) {
        Optional<Loan> optionalLoan = loanRepository.findById(loanId);
        if (optionalLoan.isEmpty()) {
            throw new IllegalArgumentException("درخواست یافت نشد");
        }
        
        Loan loan = optionalLoan.get();
        
        // بررسی سناریو 3-5: اگر قبلاً تایید شده باشد
        if (loan.isApproved() || loan.isBorrowed()) {
            throw new RuntimeException("این درخواست قبلاً تایید شده است");
        }
        
        // بررسی مجدد موجود بودن کتاب
        if (!bookService.isBookAvailable(loan.getBookId())) {
            throw new RuntimeException("کتاب دیگر موجود نیست");
        }
        
        // تایید درخواست
        loan.approve(employeeId);
        
        // تغییر وضعیت کتاب به امانت داده شده
        bookService.markBookAsBorrowed(loan.getBookId());
        
        return loanRepository.save(loan);
    }
    
    /**
     * متد کمکی برای تحویل کتاب به دانشجو
     */
    public Loan markAsBorrowed(String loanId) {
        Optional<Loan> optionalLoan = loanRepository.findById(loanId);
        if (optionalLoan.isEmpty()) {
            throw new IllegalArgumentException("درخواست یافت نشد");
        }
        
        Loan loan = optionalLoan.get();
        
        if (!loan.isApproved()) {
            throw new RuntimeException("فقط درخواست‌های تایید شده قابل تحویل هستند");
        }
        
        loan.markAsBorrowed();
        return loanRepository.save(loan);
    }
    
    /**
     * بازگرداندن کتاب
     */
    public Loan returnBook(String loanId) {
        Optional<Loan> optionalLoan = loanRepository.findById(loanId);
        if (optionalLoan.isEmpty()) {
            throw new IllegalArgumentException("درخواست یافت نشد");
        }
        
        Loan loan = optionalLoan.get();
        
        if (loan.isReturned()) {
            throw new RuntimeException("کتاب قبلاً برگردانده شده است");
        }
        
        loan.returnBook();
        bookService.markBookAsReturned(loan.getBookId());
        
        return loanRepository.save(loan);
    }
    
    /**
     * رد درخواست امانت
     */
    public Loan rejectBorrowRequest(String loanId) {
        Optional<Loan> optionalLoan = loanRepository.findById(loanId);
        if (optionalLoan.isEmpty()) {
            throw new IllegalArgumentException("درخواست یافت نشد");
        }
        
        Loan loan = optionalLoan.get();
        
        if (loan.isApproved() || loan.isBorrowed()) {
            throw new RuntimeException("نمی‌توان درخواست تأیید شده را رد کرد");
        }
        
        loan.reject();
        return loanRepository.save(loan);
    }
    
    // ========== سناریوی 4: سرویس گزارش‌گیری ==========
    
    /**
     * سناریو 4-1: تولید گزارش برای یک دانشجو
     * شامل: تعداد کل امانت‌ها، تعداد کتاب‌های تحویل‌داده‌نشده و تعداد امانت‌های با تاخیر
     */
    public StudentReport generateStudentReport(String studentId) {
        // دریافت تمام امانت‌های دانشجو
        List<Loan> studentLoans = loanRepository.findByStudentId(studentId);
        
        int totalLoans = studentLoans.size();
        int notReturnedCount = 0;
        int delayedLoansCount = 0;
        
        LocalDate today = LocalDate.now();
        
        for (Loan loan : studentLoans) {
            // شمارش کتاب‌های تحویل داده نشده
            if (loan.isBorrowed() && !loan.isReturned()) {
                notReturnedCount++;
            }
            
            // شمارش امانت‌های با تاخیر
            if (loan.isBorrowed() && !loan.isReturned() && 
                loan.getEndDate().isBefore(today)) {
                delayedLoansCount++;
            }
        }
        
        return new StudentReport(totalLoans, notReturnedCount, delayedLoansCount);
    }
    
    /**
     * سناریو 4-2: محاسبه آمار کلی کتابخانه
     * شامل: میانگین روزهای امانت
     */
    public LibraryStats generateLibraryStats() {
        List<Loan> allLoans = loanRepository.findAll();
        
        int totalLoans = allLoans.size();
        int totalLoanDays = 0;
        int completedLoansCount = 0;
        
        // محاسبه میانگین روزهای امانت برای امانت‌های کامل شده
        for (Loan loan : allLoans) {
            if (loan.isReturned() && loan.getActualReturnDate() != null) {
                long days = java.time.temporal.ChronoUnit.DAYS.between(
                    loan.getStartDate(), loan.getActualReturnDate());
                totalLoanDays += days;
                completedLoansCount++;
            }
        }
        
        double averageLoanDays = completedLoansCount > 0 ? 
            (double) totalLoanDays / completedLoansCount : 0.0;
        
        // دریافت سایر آمارها (اگر سایر سرویس‌ها موجود باشند)
        int totalStudents = studentService.getTotalStudentsCount();
        int totalBooks = bookService.getTotalBooksCount();
        
        return new LibraryStats(averageLoanDays, totalStudents, totalBooks, totalLoans);
    }
    
    /**
     * متد کمکی: لیست ۱۰ دانشجوی با بیشترین تاخیر در تحویل کتاب
     */
    public List<StudentDelayReport> getTop10StudentsWithMostDelays() {
        // دریافت تمام دانشجویان
        List<String> allStudentIds = studentService.getAllStudentIds();
        List<StudentDelayReport> delayReports = new ArrayList<>();
        
        for (String studentId : allStudentIds) {
            StudentReport report = generateStudentReport(studentId);
            if (report.getDelayedLoansCount() > 0) {
                delayReports.add(new StudentDelayReport(
                    studentId, 
                    report.getDelayedLoansCount(),
                    report.getNotReturnedCount()
                ));
            }
        }
        
        // مرتب‌سازی بر اساس بیشترین تاخیر
        delayReports.sort((a, b) -> b.getDelayCount() - a.getDelayCount());
        
        // برگرداندن ۱۰ مورد اول
        return delayReports.stream().limit(10).collect(java.util.stream.Collectors.toList());
    }
    
    // متدهای کمکی
    
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }
    
    public List<Loan> getLoansByStudent(String studentId) {
        return loanRepository.findByStudentId(studentId);
    }
    
    public List<Loan> getLoansByBook(String bookId) {
        return loanRepository.findByBookId(bookId);
    }
}