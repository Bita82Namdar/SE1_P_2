package com.university.library.service;

import com.university.library.model.*;
import com.university.library.repository.*;
import java.time.LocalDate;
import java.util.*;

/**
 * سرویس تخصصی برای تولید گزارش‌های مختلف کتابخانه
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
     * گزارش ساده تاریخچه امانات دانشجو
     * @param studentId شناسه دانشجو
     * @return Map حاوی اطلاعات تاریخچه
     */
    public Map<String, Object> getSimpleStudentLoanHistory(String studentId) {
        List<Loan> studentLoans = loanRepository.findByStudentId(studentId);
        StudentReport report = generateStudentReport(studentId);
        
        Map<String, Object> history = new HashMap<>();
        history.put("studentId", studentId);
        history.put("loans", studentLoans);
        history.put("totalLoans", report.getTotalLoans());
        history.put("notReturnedCount", report.getNotReturnedCount());
        history.put("delayedLoansCount", report.getDelayedLoansCount());
        
        return history;
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
     * سناریو 4-3: گزارش ساده عملکرد کارمندان
     * @return Map حاوی آمار عملکرد
     */
    public Map<String, Object> generateSimpleEmployeePerformanceReport() {
        Map<String, Object> performance = new HashMap<>();
        
        // آمار کلی
        int totalBooks = bookService.getTotalBooksCount();
        List<Loan> allLoans = loanRepository.findAll();
        int totalLoans = allLoans.size();
        int approvedLoans = 0;
        
        for (Loan loan : allLoans) {
            if (loan.isApproved() || loan.isBorrowed()) {
                approvedLoans++;
            }
        }
        
        performance.put("totalBooksRegistered", totalBooks);
        performance.put("totalLoansProcessed", totalLoans);
        performance.put("approvedLoans", approvedLoans);
        
        return performance;
    }
    
    /**
     * سناریو 4-4: گزارش ساده امانت‌های کتاب‌ها
     * @return Map حاوی آمار امانت‌ها
     */
    public Map<String, Object> generateSimpleLoanStatisticsReport() {
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
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRequests", totalRequests);
        stats.put("totalApprovedLoans", totalApprovedLoans);
        stats.put("totalReturnedLoans", totalReturnedLoans);
        stats.put("totalDelayedLoans", totalDelayedLoans);
        stats.put("activeLoans", activeLoans);
        stats.put("averageLoanDays", averageLoanDays);
        stats.put("topDelayedStudents", topDelayedStudents);
        
        return stats;
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
        return delayReports.size() > 10 ? delayReports.subList(0, 10) : delayReports;
    }
    
    /**
     * گزارش آمار مهمان (سناریو 2-3)
     * @return Map شامل آمارهای ساده برای مهمانان
     */
    public Map<String, Integer> generateSimpleGuestStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        
        stats.put("totalStudents", studentService.getTotalStudentsCount());
        stats.put("totalBooks", bookService.getTotalBooksCount());
        stats.put("totalLoans", loanRepository.findAll().size());
        stats.put("currentLoans", bookService.getBorrowedBooksCount());
        
        return stats;
    }
    
    /**
     * گزارش ساده پرفروش‌ترین کتاب‌ها
     * @param limit تعداد کتاب‌های برتر
     * @return لیست Mapهای حاوی اطلاعات کتاب
     */
    public List<Map<String, Object>> getSimpleMostPopularBooks(int limit) {
        List<Book> allBooks = bookRepository.findAll();
        List<Map<String, Object>> popularBooks = new ArrayList<>();
        
        for (Book book : allBooks) {
            List<Loan> bookLoans = loanRepository.findByBookId(book.getBookId());
            int loanCount = bookLoans.size();
            
            if (loanCount > 0) {
                Map<String, Object> bookInfo = new HashMap<>();
                bookInfo.put("bookId", book.getBookId());
                bookInfo.put("title", book.getTitle());
                bookInfo.put("author", book.getAuthor());
                bookInfo.put("loanCount", loanCount);
                
                popularBooks.add(bookInfo);
            }
        }
        
        // مرتب‌سازی بر اساس بیشترین تعداد امانت
        popularBooks.sort((a, b) -> 
            (int)b.get("loanCount") - (int)a.get("loanCount"));
        
        // برگرداندن limit مورد اول
        return popularBooks.size() > limit ? 
            popularBooks.subList(0, limit) : popularBooks;
    }
    
    /**
     * گزارش ساده امانت‌های فعال
     * @return لیست Mapهای حاوی اطلاعات امانت‌های فعال
     */
    public List<Map<String, Object>> getSimpleActiveLoansReport() {
        List<Loan> allLoans = loanRepository.findAll();
        List<Map<String, Object>> activeLoans = new ArrayList<>();
        LocalDate today = LocalDate.now();
        
        for (Loan loan : allLoans) {
            if (loan.isBorrowed() && !loan.isReturned()) {
                boolean isOverdue = loan.getEndDate().isBefore(today);
                long daysOverdue = isOverdue ? 
                    java.time.temporal.ChronoUnit.DAYS.between(loan.getEndDate(), today) : 0;
                
                Map<String, Object> loanInfo = new HashMap<>();
                loanInfo.put("loanId", loan.getLoanId());
                loanInfo.put("studentId", loan.getStudentId());
                loanInfo.put("bookId", loan.getBookId());
                loanInfo.put("startDate", loan.getStartDate());
                loanInfo.put("endDate", loan.getEndDate());
                loanInfo.put("isOverdue", isOverdue);
                loanInfo.put("daysOverdue", daysOverdue);
                
                activeLoans.add(loanInfo);
            }
        }
        
        return activeLoans;
    }
    
    // ========== متدهای کمکی ==========
    
    /**
     * دریافت آمار کامل کتابخانه در یک Map
     * @return Map شامل تمام آمارها
     */
    public Map<String, Object> getCompleteLibraryStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // آمارهای کلی
        LibraryStats libraryStats = generateLibraryStats();
        stats.put("libraryStats", libraryStats);
        
        // آمار امانت‌ها
        Map<String, Object> loanStats = generateSimpleLoanStatisticsReport();
        stats.put("loanStatistics", loanStats);
        
        // آمار مهمان
        Map<String, Integer> guestStats = generateSimpleGuestStatistics();
        stats.put("guestStatistics", guestStats);
        
        // امانت‌های فعال
        List<Map<String, Object>> activeLoans = getSimpleActiveLoansReport();
        stats.put("activeLoans", activeLoans);
        
        // دانشجویان با تاخیر
        List<StudentDelayReport> delayedStudents = getTop10StudentsWithMostDelays();
        stats.put("topDelayedStudents", delayedStudents);
        
        // عملکرد کارمندان
        Map<String, Object> employeePerformance = generateSimpleEmployeePerformanceReport();
        stats.put("employeePerformance", employeePerformance);
        
        return stats;
    }
}
