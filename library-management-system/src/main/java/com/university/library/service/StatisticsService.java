package com.university.library.service;

import com.university.library.model.*;
import com.university.library.repository.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * سرویس تخصصی برای تولید گزارش‌های مختلف کتابخانه
 * این سرویس منطق گزارش‌گیری را از LoanService جدا کرده است
 */
public class StatisticsService {
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final LoanService loanService;
    private final StudentService studentService;
    private final BookService bookService;
    
    // Constructor اصلی
    public StatisticsService() {
        this.loanRepository = LoanRepository.getInstance();
        this.bookRepository = BookRepository.getInstance();
        this.userRepository = UserRepository.getInstance();
        this.loanService = new LoanService();
        this.studentService = new StudentService(userRepository);
        this.bookService = new BookService(bookRepository);
    }
    
    // Constructor برای تست
    public StatisticsService(LoanRepository loanRepository, 
                           BookRepository bookRepository,
                           UserRepository userRepository,
                           LoanService loanService,
                           StudentService studentService,
                           BookService bookService) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.loanService = loanService;
        this.studentService = studentService;
        this.bookService = bookService;
    }
    
    // ========== گزارش‌های دانشجو (سناریوهای 3-6 و 4-1) ==========
    
    /**
     * سناریو 3-6 و 4-1: گزارش تاریخچه امانات یک دانشجو
     * @param studentId شناسه دانشجو
     * @return StudentReport شامل آمار امانات دانشجو
     */
    public StudentReport generateStudentReport(String studentId) {
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
     * گزارش کامل تاریخچه امانات دانشجو
     * @param studentId شناسه دانشجو
     * @return لیست تمام امانت‌های دانشجو
     */
    public StudentLoanHistory getStudentLoanHistory(String studentId) {
        List<Loan> studentLoans = loanRepository.findByStudentId(studentId);
        StudentReport report = generateStudentReport(studentId);
        
        return new StudentLoanHistory(
            studentId,
            studentLoans,
            report
        );
    }
    
    // ========== گزارش‌های کلی کتابخانه (سناریوهای 4-2 تا 4-4) ==========
    
    /**
     * سناریو 4-2: آمار کلی کتابخانه
     * @return LibraryStats شامل آمارهای مختلف کتابخانه
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
        
        // دریافت سایر آمارها
        int totalStudents = studentService.getTotalStudentsCount();
        int totalBooks = bookService.getTotalBooksCount();
        
        return new LibraryStats(averageLoanDays, totalStudents, totalBooks, totalLoans);
    }
    
    /**
     * سناریو 4-3: گزارش عملکرد کارمندان
     * @return لیست گزارش عملکرد کارمندان
     */
    public List<EmployeePerformanceReport> generateEmployeePerformanceReport() {
        // دریافت تمام کارمندان
        List<Employee> allEmployees = userRepository.findAllEmployees();
        List<EmployeePerformanceReport> reports = new ArrayList<>();
        
        for (Employee employee : allEmployees) {
            // محاسبه آمار کارمند
            int booksRegistered = countBooksRegisteredByEmployee(employee.getId());
            int booksLoanedOut = countBooksLoanedByEmployee(employee.getId());
            int booksReceived = countBooksReceivedByEmployee(employee.getId());
            
            EmployeePerformanceReport report = new EmployeePerformanceReport(
                employee.getId(),
                employee.getName(),
                booksRegistered,
                booksLoanedOut,
                booksReceived
            );
            
            reports.add(report);
        }
        
        return reports;
    }
    
    /**
     * سناریو 4-4: گزارش امانت‌های کتاب‌ها
     * @return گزارش جامع امانت‌ها
     */
    public LoanStatisticsReport generateLoanStatisticsReport() {
        List<Loan> allLoans = loanRepository.findAll();
        
        int totalRequests = allLoans.size();
        int totalApprovedLoans = 0;
        int totalReturnedLoans = 0;
        int totalDelayedLoans = 0;
        int activeLoans = 0;
        
        LocalDate today = LocalDate.now();
        long totalLoanDays = 0;
        int completedLoans = 0;
        
        for (Loan loan : allLoans) {
            if (loan.isApproved()) {
                totalApprovedLoans++;
            }
            
            if (loan.isReturned()) {
                totalReturnedLoans++;
                
                if (loan.getActualReturnDate() != null) {
                    long days = java.time.temporal.ChronoUnit.DAYS.between(
                        loan.getStartDate(), loan.getActualReturnDate());
                    totalLoanDays += days;
                    completedLoans++;
                }
            }
            
            if (loan.isBorrowed() && !loan.isReturned() && 
                loan.getEndDate().isBefore(today)) {
                totalDelayedLoans++;
            }
            
            if (loan.isBorrowed() && !loan.isReturned()) {
                activeLoans++;
            }
        }
        
        double averageLoanDays = completedLoans > 0 ? 
            (double) totalLoanDays / completedLoans : 0.0;
        
        // دریافت ۱۰ دانشجوی با بیشترین تاخیر
        List<StudentDelayReport> topDelayedStudents = getTop10StudentsWithMostDelays();
        
        return new LoanStatisticsReport(
            totalRequests,
            totalApprovedLoans,
            totalReturnedLoans,
            totalDelayedLoans,
            activeLoans,
            averageLoanDays,
            topDelayedStudents
        );
    }
    
    /**
     * سناریو 4-4: لیست ۱۰ دانشجوی با بیشترین تاخیر در تحویل کتاب
     * @return لیست دانشجویان با بیشترین تاخیر
     */
    public List<StudentDelayReport> getTop10StudentsWithMostDelays() {
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
        return delayReports.stream().limit(10).collect(Collectors.toList());
    }
    
    /**
     * گزارش آمار مهمان (سناریو 2-3)
     * @return GuestStatistics شامل آمارهای ساده برای مهمانان
     */
    public GuestStatistics generateGuestStatistics() {
        int totalStudents = studentService.getTotalStudentsCount();
        int totalBooks = bookService.getTotalBooksCount();
        int totalLoans = loanRepository.findAll().size();
        int currentLoans = bookService.getBorrowedBooksCount();
        
        return new GuestStatistics(totalStudents, totalBooks, totalLoans, currentLoans);
    }
    
    // ========== متدهای کمکی برای محاسبات ==========
    
    /**
     * شمارش کتاب‌های ثبت شده توسط یک کارمند
     */
    private int countBooksRegisteredByEmployee(String employeeId) {
        List<Book> allBooks = bookRepository.findAll();
        int count = 0;
        for (Book book : allBooks) {
            if (book.getRegisteredBy() != null && 
                book.getRegisteredBy().equals(employeeId)) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * شمارش کتاب‌های امانت داده شده توسط یک کارمند
     */
    private int countBooksLoanedByEmployee(String employeeId) {
        List<Loan> allLoans = loanRepository.findAll();
        int count = 0;
        for (Loan loan : allLoans) {
            if (loan.getApprovedBy() != null && 
                loan.getApprovedBy().equals(employeeId)) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * شمارش کتاب‌های تحویل گرفته شده توسط یک کارمند
     */
    private int countBooksReceivedByEmployee(String employeeId) {
        List<Loan> allLoans = loanRepository.findAll();
        int count = 0;
        for (Loan loan : allLoans) {
            if (loan.isReturned() && 
                loan.getReturnProcessedBy() != null && 
                loan.getReturnProcessedBy().equals(employeeId)) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * گزارش پرفروش‌ترین کتاب‌ها
     * @param limit تعداد کتاب‌های برتر
     * @return لیست کتاب‌های پرفروش
     */
    public List<PopularBookReport> getMostPopularBooks(int limit) {
        // دریافت تمام کتاب‌ها
        List<Book> allBooks = bookRepository.findAll();
        List<PopularBookReport> popularBooks = new ArrayList<>();
        
        for (Book book : allBooks) {
            List<Loan> bookLoans = loanRepository.findByBookId(book.getBookId());
            int loanCount = bookLoans.size();
            
            if (loanCount > 0) {
                popularBooks.add(new PopularBookReport(
                    book.getBookId(),
                    book.getTitle(),
                    book.getAuthor(),
                    loanCount
                ));
            }
        }
        
        // مرتب‌سازی بر اساس بیشترین تعداد امانت
        popularBooks.sort((a, b) -> b.getLoanCount() - a.getLoanCount());
        
        return popularBooks.stream().limit(limit).collect(Collectors.toList());
    }
    
    /**
     * گزارش امانت‌های فعال
     * @return لیست امانت‌های فعال
     */
    public List<ActiveLoanReport> getActiveLoansReport() {
        List<Loan> allLoans = loanRepository.findAll();
        List<ActiveLoanReport> activeLoans = new ArrayList<>();
        LocalDate today = LocalDate.now();
        
        for (Loan loan : allLoans) {
            if (loan.isBorrowed() && !loan.isReturned()) {
                boolean isOverdue = loan.getEndDate().isBefore(today);
                long daysOverdue = isOverdue ? 
                    java.time.temporal.ChronoUnit.DAYS.between(loan.getEndDate(), today) : 0;
                
                activeLoans.add(new ActiveLoanReport(
                    loan.getLoanId(),
                    loan.getStudentId(),
                    loan.getBookId(),
                    loan.getStartDate(),
                    loan.getEndDate(),
                    isOverdue,
                    daysOverdue
                ));
            }
        }
        
        return activeLoans;
    }
}
