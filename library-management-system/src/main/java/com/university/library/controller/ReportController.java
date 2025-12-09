package com.university.library.controller;

import com.university.library.model.*;
import com.university.library.service.StatisticsService;
import com.university.library.service.LoanService;
import com.university.library.service.StudentService;
import com.university.library.service.BookService;
import java.util.List;

/**
 * کنترلر مدیریت گزارش‌های کتابخانه
 * این کنترلر endpointهای مربوط به گزارش‌گیری را ارائه می‌دهد
 */
public class ReportController {
    private final StatisticsService statisticsService;
    private final LoanService loanService;
    private final StudentService studentService;
    private final BookService bookService;
    
    public ReportController() {
        this.statisticsService = new StatisticsService();
        this.loanService = new LoanService();
        this.studentService = new StudentService();
        this.bookService = new BookService();
    }
    
    // Constructor برای تست
    public ReportController(StatisticsService statisticsService,
                          LoanService loanService,
                          StudentService studentService,
                          BookService bookService) {
        this.statisticsService = statisticsService;
        this.loanService = loanService;
        this.studentService = studentService;
        this.bookService = bookService;
    }
    
    // ========== گزارش‌های دانشجو ==========
    
    /**
     * سناریو 3-6: مشاهده گزارش تاریخچه امانات یک دانشجو
     * @param studentId شناسه دانشجو
     * @return گزارش کامل دانشجو
     */
    public StudentLoanHistory getStudentLoanHistory(String studentId) {
        // بررسی وجود دانشجو
        if (studentService.getStudentById(studentId) == null) {
            throw new IllegalArgumentException("دانشجو با شناسه " + studentId + " یافت نشد");
        }
        
        return statisticsService.getStudentLoanHistory(studentId);
    }
    
    /**
     * سناریو 3-6 و 4-1: تولید گزارش آماری برای یک دانشجو
     * @param studentId شناسه دانشجو
     * @return گزارش آماری دانشجو
     */
    public StudentReport getStudentReport(String studentId) {
        // بررسی وجود دانشجو
        if (studentService.getStudentById(studentId) == null) {
            throw new IllegalArgumentException("دانشجو با شناسه " + studentId + " یافت نشد");
        }
        
        return statisticsService.generateStudentReport(studentId);
    }
    
    // ========== گزارش‌های مهمان ==========
    
    /**
     * سناریو 2-1: مشاهده تعداد دانشجویان ثبت‌نام کرده
     * @return تعداد دانشجویان
     */
    public int getRegisteredStudentsCount() {
        return studentService.getTotalStudentsCount();
    }
    
    /**
     * سناریو 2-3: مشاهده اطلاعات آماری ساده
     * @return آمارهای ساده کتابخانه
     */
    public GuestStatistics getGuestStatistics() {
        return statisticsService.generateGuestStatistics();
    }
    
    /**
     * سناریو 2-3: مشاهده تعداد کل کتاب‌ها
     * @return تعداد کل کتاب‌ها
     */
    public int getTotalBooksCount() {
        return bookService.getTotalBooksCount();
    }
    
    /**
     * سناریو 2-3: مشاهده تعداد کل امانت‌ها
     * @return تعداد کل امانت‌ها
     */
    public int getTotalLoansCount() {
        return loanService.getAllLoans().size();
    }
    
    /**
     * سناریو 2-3: مشاهده تعداد کتاب‌های در امانت
     * @return تعداد کتاب‌های در امانت
     */
    public int getCurrentLoansCount() {
        return bookService.getBorrowedBooksCount();
    }
    
    // ========== گزارش‌های کارمند ==========
    
    /**
     * سناریو 3-6: گزارش تاریخچه امانات دانشجو برای کارمند
     * @param studentId شناسه دانشجو
     * @return گزارش دانشجو
     */
    public StudentReport getStudentReportForEmployee(String studentId) {
        return getStudentReport(studentId);
    }
    
    /**
     * سناریو 3-7: بررسی وضعیت فعال/غیرفعال بودن دانشجو
     * @param studentId شناسه دانشجو
     * @return true اگر دانشجو فعال باشد
     */
    public boolean isStudentActive(String studentId) {
        return studentService.isStudentActive(studentId);
    }
    
    // ========== گزارش‌های مدیر ==========
    
    /**
     * سناریو 4-2: مشاهده عملکرد کارمند
     * @return لیست گزارش عملکرد کارمندان
     */
    public List<EmployeePerformanceReport> getEmployeePerformanceReports() {
        return statisticsService.generateEmployeePerformanceReport();
    }
    
    /**
     * سناریو 4-3: مشاهده اطلاعات آماری امانات کتاب
     * @return گزارش آماری امانات
     */
    public LoanStatisticsReport getLoanStatisticsReport() {
        return statisticsService.generateLoanStatisticsReport();
    }
    
    /**
     * سناریو 4-4: مشاهده اطلاعات آماری دانشجویان
     * @return گزارش جامع دانشجویان
     */
    public StudentStatisticsReport getStudentStatisticsReport() {
        int totalStudents = studentService.getTotalStudentsCount();
        int activeStudents = studentService.getActiveStudentsCount();
        int inactiveStudents = studentService.getInactiveStudentsCount();
        
        // محاسبه میانگین امانت‌ها به ازای هر دانشجو
        double averageLoansPerStudent = totalStudents > 0 ? 
            (double) getTotalLoansCount() / totalStudents : 0.0;
        
        // دریافت ۱۰ دانشجوی با بیشترین تاخیر
        List<StudentDelayReport> topDelayedStudents = statisticsService.getTop10StudentsWithMostDelays();
        
        return new StudentStatisticsReport(
            totalStudents,
            activeStudents,
            inactiveStudents,
            averageLoansPerStudent,
            topDelayedStudents
        );
    }
    
    /**
     * سناریو 4-4: لیست ۱۰ دانشجوی با بیشترین تاخیر در تحویل کتاب
     * @return لیست دانشجویان با بیشترین تاخیر
     */
    public List<StudentDelayReport> getTop10DelayedStudents() {
        return statisticsService.getTop10StudentsWithMostDelays();
    }
    
    /**
     * سناریو 4-2: آمار کلی کتابخانه
     * @return آمار کلی کتابخانه
     */
    public LibraryStats getLibraryStatistics() {
        return statisticsService.generateLibraryStats();
    }
    
    /**
     * سناریو 4-3: تعداد درخواست‌های امانت ثبت شده
     * @return تعداد درخواست‌ها
     */
    public int getTotalBorrowRequests() {
        return loanService.getAllLoans().size();
    }
    
    /**
     * سناریو 4-3: تعداد کل امانت داده شده
     * @return تعداد امانت‌های تأیید شده
     */
    public int getTotalApprovedLoans() {
        List<Loan> allLoans = loanService.getAllLoans();
        int approvedCount = 0;
        for (Loan loan : allLoans) {
            if (loan.isApproved() || loan.isBorrowed()) {
                approvedCount++;
            }
        }
        return approvedCount;
    }
    
    /**
     * سناریو 4-3: میانگین تعداد روزهای امانت
     * @return میانگین روزهای امانت
     */
    public double getAverageLoanDays() {
        return statisticsService.generateLibraryStats().getAverageLoanDays();
    }
    
    // ========== گزارش‌های عمومی ==========
    
    /**
     * گزارش پرفروش‌ترین کتاب‌ها
     * @param limit تعداد کتاب‌های برتر
     * @return لیست کتاب‌های پرفروش
     */
    public List<PopularBookReport> getMostPopularBooks(int limit) {
        return statisticsService.getMostPopularBooks(limit);
    }
    
    /**
     * گزارش امانت‌های فعال
     * @return لیست امانت‌های فعال
     */
    public List<ActiveLoanReport> getActiveLoansReport() {
        return statisticsService.getActiveLoansReport();
    }
    
    /**
     * گزارش کتاب‌های موجود
     * @return لیست کتاب‌های موجود
     */
    public List<Book> getAvailableBooks() {
        return bookService.getAvailableBooks();
    }
    
    /**
     * گزارش کتاب‌های امانت داده شده
     * @return لیست کتاب‌های امانت داده شده
     */
    public List<Book> getBorrowedBooks() {
        return bookService.getBorrowedBooks();
    }
    
    /**
     * گزارش دانشجویان فعال
     * @return لیست دانشجویان فعال
     */
    public List<Student> getActiveStudents() {
        return studentService.getActiveStudents();
    }
    
    /**
     * گزارش دانشجویان غیرفعال
     * @return لیست دانشجویان غیرفعال
     */
    public List<Student> getInactiveStudents() {
        return studentService.getInactiveStudents();
    }
}
