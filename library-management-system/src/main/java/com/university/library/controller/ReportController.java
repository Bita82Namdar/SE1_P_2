package com.university.library.controller;

import com.university.library.model.*;
import com.university.library.service.StatisticsService;
import com.university.library.service.LoanService;
import com.university.library.service.StudentService;
import com.university.library.service.BookService;
import java.util.List;
import java.util.ArrayList;

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
     * سناریو 4-2: آمار کلی کتابخانه
     * @return آمار کلی کتابخانه
     */
    public LibraryStats getLibraryStatistics() {
        return statisticsService.generateLibraryStats();
    }
    
    /**
     * سناریو 4-4: لیست ۱۰ دانشجوی با بیشترین تاخیر در تحویل کتاب
     * @return لیست دانشجویان با بیشترین تاخیر
     */
    public List<StudentDelayReport> getTop10DelayedStudents() {
        // پیاده‌سازی ساده
        List<StudentReport> allStudentReports = new ArrayList<>();
        List<String> allStudentIds = studentService.getAllStudentIds();
        
        List<StudentDelayReport> result = new ArrayList<>();
        
        for (String studentId : allStudentIds) {
            StudentReport report = getStudentReport(studentId);
            if (report.getDelayedLoansCount() > 0) {
                result.add(new StudentDelayReport(
                    studentId,
                    report.getDelayedLoansCount(),
                    report.getNotReturnedCount()
                ));
            }
        }
        
        // مرتب‌سازی بر اساس بیشترین تاخیر
        result.sort((a, b) -> b.getDelayCount() - a.getDelayCount());
        
        // برگرداندن ۱۰ مورد اول
        return result.size() > 10 ? result.subList(0, 10) : result;
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
    
    /**
     * گزارش آماری ساده برای مهمانان
     * @return رشته حاوی آمارها
     */
    public String getSimpleGuestStatistics() {
        int students = getRegisteredStudentsCount();
        int books = getTotalBooksCount();
        int loans = getTotalLoansCount();
        int currentLoans = getCurrentLoansCount();
        
        return String.format(
            "آمار کتابخانه: %d دانشجو، %d کتاب، %d امانت کل، %d امانت جاری",
            students, books, loans, currentLoans
        );
    }
    
    /**
     * گزارش عملکرد کارمندان (ساده)
     * @return رشته حاوی آمار عملکرد
     */
    public String getSimpleEmployeePerformance() {
        // پیاده‌سازی ساده
        int totalBooks = bookService.getTotalBooksCount();
        int totalLoans = loanService.getAllLoans().size();
        
        return String.format(
            "عملکرد کلی: %d کتاب ثبت شده، %d امانت پردازش شده",
            totalBooks, totalLoans
        );
    }
    
    /**
     * گزارش امانت‌های فعال (ساده)
     * @return لیست رشته‌ای از امانت‌های فعال
     */
    public List<String> getSimpleActiveLoansReport() {
        List<Loan> allLoans = loanService.getAllLoans();
        List<String> activeLoans = new ArrayList<>();
        
        for (Loan loan : allLoans) {
            if (loan.isBorrowed() && !loan.isReturned()) {
                String status = loan.getEndDate().isBefore(java.time.LocalDate.now()) ?
                    "تأخیر دارد" : "فعال";
                activeLoans.add(String.format(
                    "امانت %s: دانشجو %s، کتاب %s، وضعیت: %s",
                    loan.getLoanId(), loan.getStudentId(), loan.getBookId(), status
                ));
            }
        }
        
        return activeLoans;
    }
}
