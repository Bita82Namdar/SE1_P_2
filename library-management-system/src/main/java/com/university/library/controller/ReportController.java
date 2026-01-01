package com.university.library.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReportController {
    
    // کلاس‌های داخلی برای جلوگیری از خطا
    public static class LibrarySummary {
        private int totalStudents;
        private int totalBooks;
        private int totalLoans;
        private int activeLoans;
        private int availableBooks;
        
        public LibrarySummary(int totalStudents, int totalBooks, int totalLoans, int activeLoans, int availableBooks) {
            this.totalStudents = totalStudents;
            this.totalBooks = totalBooks;
            this.totalLoans = totalLoans;
            this.activeLoans = activeLoans;
            this.availableBooks = availableBooks;
        }
        
        // Getters
        public int getTotalStudents() { return totalStudents; }
        public int getTotalBooks() { return totalBooks; }
        public int getTotalLoans() { return totalLoans; }
        public int getActiveLoans() { return activeLoans; }
        public int getAvailableBooks() { return availableBooks; }
    }
    
    public static class BorrowStatistics {
        private int totalRequests;
        private int approvedRequests;
        private int rejectedRequests;
        private int pendingRequests;
        private double averageLoanDays;
        
        public BorrowStatistics(int totalRequests, int approvedRequests, int rejectedRequests, 
                               int pendingRequests, double averageLoanDays) {
            this.totalRequests = totalRequests;
            this.approvedRequests = approvedRequests;
            this.rejectedRequests = rejectedRequests;
            this.pendingRequests = pendingRequests;
            this.averageLoanDays = averageLoanDays;
        }
        
        // Getters
        public int getTotalRequests() { return totalRequests; }
        public int getApprovedRequests() { return approvedRequests; }
        public int getRejectedRequests() { return rejectedRequests; }
        public int getPendingRequests() { return pendingRequests; }
        public double getAverageLoanDays() { return averageLoanDays; }
    }
    
    public static class EmployeePerformance {
        private String employeeId;
        private String employeeName;
        private int processedRequests;
        private int booksAdded;
        private double averageProcessingTime;
        
        public EmployeePerformance(String employeeId, String employeeName, int processedRequests, 
                                  int booksAdded, double averageProcessingTime) {
            this.employeeId = employeeId;
            this.employeeName = employeeName;
            this.processedRequests = processedRequests;
            this.booksAdded = booksAdded;
            this.averageProcessingTime = averageProcessingTime;
        }
        
        // Getters
        public String getEmployeeId() { return employeeId; }
        public String getEmployeeName() { return employeeName; }
        public int getProcessedRequests() { return processedRequests; }
        public int getBooksAdded() { return booksAdded; }
        public double getAverageProcessingTime() { return averageProcessingTime; }
    }
    
    public static class StudentDelayInfo {
        private String studentId;
        private String studentName;
        private int delayCount;
        private int totalDaysDelayed;
        
        public StudentDelayInfo(String studentId, String studentName, int delayCount, int totalDaysDelayed) {
            this.studentId = studentId;
            this.studentName = studentName;
            this.delayCount = delayCount;
            this.totalDaysDelayed = totalDaysDelayed;
        }
        
        // Getters
        public String getStudentId() { return studentId; }
        public String getStudentName() { return studentName; }
        public int getDelayCount() { return delayCount; }
        public int getTotalDaysDelayed() { return totalDaysDelayed; }
    }
    
    public static class StudentReport {
        private String studentId;
        private String studentName;
        private int totalLoans;
        private int notReturnedCount;
        private int delayedLoansCount;
        
        public StudentReport(String studentId, String studentName, int totalLoans, 
                            int notReturnedCount, int delayedLoansCount) {
            this.studentId = studentId;
            this.studentName = studentName;
            this.totalLoans = totalLoans;
            this.notReturnedCount = notReturnedCount;
            this.delayedLoansCount = delayedLoansCount;
        }
        
        // Getters
        public String getStudentId() { return studentId; }
        public String getStudentName() { return studentName; }
        public int getTotalLoans() { return totalLoans; }
        public int getNotReturnedCount() { return notReturnedCount; }
        public int getDelayedLoansCount() { return delayedLoansCount; }
    }
    
    public static class LibraryStats {
        private int totalBooks;
        private int totalStudents;
        private int totalLoans;
        private double averageLoanDays;
        
        public LibraryStats(int totalBooks, int totalStudents, int totalLoans, double averageLoanDays) {
            this.totalBooks = totalBooks;
            this.totalStudents = totalStudents;
            this.totalLoans = totalLoans;
            this.averageLoanDays = averageLoanDays;
        }
        
        // Getters
        public int getTotalBooks() { return totalBooks; }
        public int getTotalStudents() { return totalStudents; }
        public int getTotalLoans() { return totalLoans; }
        public double getAverageLoanDays() { return averageLoanDays; }
    }
    
    public static class StudentDelayReport {
        private String studentId;
        private int delayCount;
        private int notReturnedCount;
        
        public StudentDelayReport(String studentId, int delayCount, int notReturnedCount) {
            this.studentId = studentId;
            this.delayCount = delayCount;
            this.notReturnedCount = notReturnedCount;
        }
        
        // Getters
        public String getStudentId() { return studentId; }
        public int getDelayCount() { return delayCount; }
        public int getNotReturnedCount() { return notReturnedCount; }
    }
    
    public static class ResponseEntity<T> {
        private T body;
        private int status;
        
        public ResponseEntity(T body, int status) {
            this.body = body;
            this.status = status;
        }
        
        public T getBody() { return body; }
        public int getStatus() { return status; }
    }
    
    // داده‌های نمونه
    private List<StudentDelayInfo> delayedStudents = new ArrayList<>();
    
    public ReportController() {
        // داده‌های نمونه برای دانشجویان با تاخیر
        delayedStudents.add(new StudentDelayInfo("STU001", "علی محمدی", 3, 15));
        delayedStudents.add(new StudentDelayInfo("STU002", "فاطمه کریمی", 2, 8));
        delayedStudents.add(new StudentDelayInfo("STU003", "محمد رضایی", 1, 5));
        delayedStudents.add(new StudentDelayInfo("STU004", "زهرا احمدی", 4, 22));
        delayedStudents.add(new StudentDelayInfo("STU005", "امیر حسینی", 2, 10));
    }
    
    // GET /api/stats/summary
    public ResponseEntity<LibrarySummary> getSummary() {
        try {
            // داده‌های نمونه
            LibrarySummary summary = new LibrarySummary(
                150,    // totalStudents
                500,    // totalBooks
                1200,   // totalLoans
                85,     // activeLoans
                415     // availableBooks
            );
            
            return new ResponseEntity<>(summary, 200); // HTTP 200 OK
        } catch (Exception e) {
            return new ResponseEntity<>(null, 500); // HTTP 500 Internal Server Error
        }
    }
    
    // GET /api/stats/borrows
    public ResponseEntity<BorrowStatistics> getBorrowStats() {
        try {
            // داده‌های نمونه
            BorrowStatistics stats = new BorrowStatistics(
                1500,   // totalRequests
                1200,   // approvedRequests
                100,    // rejectedRequests
                200,    // pendingRequests
                14.5    // averageLoanDays
            );
            
            return new ResponseEntity<>(stats, 200); // HTTP 200 OK
        } catch (Exception e) {
            return new ResponseEntity<>(null, 500); // HTTP 500 Internal Server Error
        }
    }
    
    // GET /api/stats/employees/{id}/performance
    public ResponseEntity<EmployeePerformance> getEmployeePerformance(String id) {
        try {
            // داده‌های نمونه بر اساس ID
            EmployeePerformance performance;
            
            switch(id) {
                case "EMP001":
                    performance = new EmployeePerformance("EMP001", "احمد رضایی", 450, 120, 2.5);
                    break;
                case "EMP002":
                    performance = new EmployeePerformance("EMP002", "مریم محمودی", 380, 95, 3.1);
                    break;
                case "EMP003":
                    performance = new EmployeePerformance("EMP003", "محمد کریمی", 520, 150, 2.2);
                    break;
                default:
                    performance = new EmployeePerformance(id, "کارمند " + id, 300, 80, 2.8);
            }
            
            return new ResponseEntity<>(performance, 200); // HTTP 200 OK
        } catch (Exception e) {
            return new ResponseEntity<>(null, 500); // HTTP 500 Internal Server Error
        }
    }
    
    // GET /api/stats/top-delayed
    public ResponseEntity<List<StudentDelayInfo>> getTopDelayedStudents() {
        try {
            // مرتب‌سازی بر اساس بیشترین تعداد تاخیر
            delayedStudents.sort((a, b) -> b.getDelayCount() - a.getDelayCount());
            
            // برگرداندن ۵ مورد اول
            List<StudentDelayInfo> topDelayed = delayedStudents.size() > 5 ? 
                delayedStudents.subList(0, 5) : delayedStudents;
            
            return new ResponseEntity<>(topDelayed, 200); // HTTP 200 OK
        } catch (Exception e) {
            return new ResponseEntity<>(null, 500); // HTTP 500 Internal Server Error
        }
    }
    
    // سایر متدهای موجود در کد شما (با کمی اصلاح)
    public StudentReport getStudentReport(String studentId) {
        // داده‌های نمونه
        return new StudentReport(studentId, "دانشجو " + studentId, 15, 2, 1);
    }
    
    public int getRegisteredStudentsCount() {
        return 150;
    }
    
    public int getTotalBooksCount() {
        return 500;
    }
    
    public int getTotalLoansCount() {
        return 1200;
    }
    
    public int getCurrentLoansCount() {
        return 85;
    }
    
    public boolean isStudentActive(String studentId) {
        // همه دانشجویان فعال فرض می‌شوند
        return true;
    }
    
    public LibraryStats getLibraryStatistics() {
        return new LibraryStats(500, 150, 1200, 14.5);
    }
    
    public List<StudentDelayReport> getTop10DelayedStudents() {
        List<StudentDelayReport> result = new ArrayList<>();
        result.add(new StudentDelayReport("STU004", 4, 1));
        result.add(new StudentDelayReport("STU001", 3, 2));
        result.add(new StudentDelayReport("STU002", 2, 0));
        result.add(new StudentDelayReport("STU005", 2, 1));
        result.add(new StudentDelayReport("STU003", 1, 0));
        
        return result;
    }
    
    public int getTotalBorrowRequests() {
        return 1500;
    }
    
    public int getTotalApprovedLoans() {
        return 1200;
    }
    
    public double getAverageLoanDays() {
        return 14.5;
    }
    
    public String getSimpleGuestStatistics() {
        return "آمار کتابخانه: 150 دانشجو، 500 کتاب، 1200 امانت کل، 85 امانت جاری";
    }
    
    public String getSimpleEmployeePerformance() {
        return "عملکرد کلی: 500 کتاب ثبت شده، 1500 امانت پردازش شده";
    }
    
    public List<String> getSimpleActiveLoansReport() {
        List<String> activeLoans = new ArrayList<>();
        activeLoans.add("امانت REQ001: دانشجو STU001، کتاب B001، وضعیت: فعال");
        activeLoans.add("امانت REQ002: دانشجو STU002، کتاب B002، وضعیت: تأخیر دارد");
        return activeLoans;
    }
    
    // متد تست
    public static void main(String[] args) {
        ReportController controller = new ReportController();
        
        // تست آمار خلاصه
        ResponseEntity<LibrarySummary> summary = controller.getSummary();
        if (summary.getBody() != null) {
            System.out.println("تعداد کل دانشجویان: " + summary.getBody().getTotalStudents());
        }
        
        // تست آمار امانت
        ResponseEntity<BorrowStatistics> borrowStats = controller.getBorrowStats();
        if (borrowStats.getBody() != null) {
            System.out.println("متوسط روزهای امانت: " + borrowStats.getBody().getAverageLoanDays());
        }
        
        // تست عملکرد کارمند
        ResponseEntity<EmployeePerformance> empPerformance = controller.getEmployeePerformance("EMP001");
        if (empPerformance.getBody() != null) {
            System.out.println("نام کارمند: " + empPerformance.getBody().getEmployeeName());
        }
        
        // تست دانشجویان با تاخیر
        ResponseEntity<List<StudentDelayInfo>> delayed = controller.getTopDelayedStudents();
        if (delayed.getBody() != null) {
            System.out.println("تعداد دانشجویان با تاخیر: " + delayed.getBody().size());
        }
    }
}