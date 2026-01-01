package com.university.library.controller;

import java.util.*;

public class ReportControllerTest {
    
    // کلاس‌های داخلی برای تست
    static class Book {
        private String id;
        private String title;
        private boolean available;
        
        public Book(String id, String title, boolean available) {
            this.id = id;
            this.title = title;
            this.available = available;
        }
        
        public String getId() { return id; }
        public String getTitle() { return title; }
        public boolean isAvailable() { return available; }
    }
    
    static class Student {
        private String id;
        private String name;
        private boolean active;
        
        public Student(String id, String name) {
            this.id = id;
            this.name = name;
            this.active = true;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
    }
    
    static class Loan {
        private String id;
        private String studentId;
        private String bookId;
        private boolean borrowed;
        private boolean returned;
        private boolean overdue;
        
        public Loan(String id, String studentId, String bookId) {
            this.id = id;
            this.studentId = studentId;
            this.bookId = bookId;
            this.borrowed = true;
            this.returned = false;
            this.overdue = false;
        }
        
        public String getId() { return id; }
        public String getStudentId() { return studentId; }
        public String getBookId() { return bookId; }
        public boolean isBorrowed() { return borrowed; }
        public boolean isReturned() { return returned; }
        public boolean isOverdue() { return overdue; }
        public void setBorrowed(boolean borrowed) { this.borrowed = borrowed; }
        public void setReturned(boolean returned) { this.returned = returned; }
        public void setOverdue(boolean overdue) { this.overdue = overdue; }
    }
    
    static class StudentReport {
        private String studentId;
        private int totalLoans;
        private int notReturnedCount;
        private int delayedLoansCount;
        
        public StudentReport(String studentId, int totalLoans, int notReturnedCount, int delayedLoansCount) {
            this.studentId = studentId;
            this.totalLoans = totalLoans;
            this.notReturnedCount = notReturnedCount;
            this.delayedLoansCount = delayedLoansCount;
        }
        
        public String getStudentId() { return studentId; }
        public int getTotalLoans() { return totalLoans; }
        public int getNotReturnedCount() { return notReturnedCount; }
        public int getDelayedLoansCount() { return delayedLoansCount; }
    }
    
    static class StudentDelayReport {
        private String studentId;
        private int delayCount;
        private int notReturnedCount;
        
        public StudentDelayReport(String studentId, int delayCount, int notReturnedCount) {
            this.studentId = studentId;
            this.delayCount = delayCount;
            this.notReturnedCount = notReturnedCount;
        }
        
        public String getStudentId() { return studentId; }
        public int getDelayCount() { return delayCount; }
        public int getNotReturnedCount() { return notReturnedCount; }
    }
    
    static class LibraryStats {
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
        
        public int getTotalBooks() { return totalBooks; }
        public int getTotalStudents() { return totalStudents; }
        public int getTotalLoans() { return totalLoans; }
        public double getAverageLoanDays() { return averageLoanDays; }
    }
    
    // کلاس ReportController ساده شده برای تست
    static class TestReportController {
        private List<Student> students = new ArrayList<>();
        private List<Book> books = new ArrayList<>();
        private List<Loan> loans = new ArrayList<>();
        
        public TestReportController() {
            // داده‌های نمونه
            students.add(new Student("ST001", "علی محمدی"));
            students.add(new Student("ST002", "فاطمه کریمی"));
            students.add(new Student("ST003", "محمد رضایی"));
            
            books.add(new Book("B001", "Effective Java", true));
            books.add(new Book("B002", "Clean Code", false));
            books.add(new Book("B003", "Design Patterns", true));
            books.add(new Book("B004", "Algorithms", false));
            
            Loan loan1 = new Loan("L001", "ST001", "B001");
            loan1.setReturned(true);
            
            Loan loan2 = new Loan("L002", "ST001", "B002");
            loan2.setOverdue(true);
            
            Loan loan3 = new Loan("L003", "ST002", "B003");
            
            loans.add(loan1);
            loans.add(loan2);
            loans.add(loan3);
        }
        
        public int getRegisteredStudentsCount() {
            return students.size();
        }
        
        public int getTotalBooksCount() {
            return books.size();
        }
        
        public int getTotalLoansCount() {
            return loans.size();
        }
        
        public int getCurrentLoansCount() {
            int count = 0;
            for (Loan loan : loans) {
                if (loan.isBorrowed() && !loan.isReturned()) {
                    count++;
                }
            }
            return count;
        }
        
        public boolean isStudentActive(String studentId) {
            for (Student student : students) {
                if (student.getId().equals(studentId)) {
                    return student.isActive();
                }
            }
            return false;
        }
        
        public StudentReport getStudentReport(String studentId) {
            int totalLoans = 0;
            int notReturnedCount = 0;
            int delayedLoansCount = 0;
            
            for (Loan loan : loans) {
                if (loan.getStudentId().equals(studentId)) {
                    totalLoans++;
                    if (!loan.isReturned()) {
                        notReturnedCount++;
                    }
                    if (loan.isOverdue()) {
                        delayedLoansCount++;
                    }
                }
            }
            
            return new StudentReport(studentId, totalLoans, notReturnedCount, delayedLoansCount);
        }
        
        public List<StudentDelayReport> getTop10DelayedStudents() {
            List<StudentDelayReport> result = new ArrayList<>();
            
            for (Student student : students) {
                StudentReport report = getStudentReport(student.getId());
                if (report.getDelayedLoansCount() > 0) {
                    result.add(new StudentDelayReport(
                        student.getId(),
                        report.getDelayedLoansCount(),
                        report.getNotReturnedCount()
                    ));
                }
            }
            
            // مرتب‌سازی بر اساس بیشترین تاخیر
            result.sort((a, b) -> b.getDelayCount() - a.getDelayCount());
            
            // فقط ۱۰ مورد اول
            return result.size() > 10 ? result.subList(0, 10) : result;
        }
        
        public LibraryStats getLibraryStatistics() {
            int totalStudents = students.size();
            int totalBooks = books.size();
            int totalLoans = loans.size();
            double averageLoanDays = 7.5; // مقدار نمونه
            
            return new LibraryStats(totalBooks, totalStudents, totalLoans, averageLoanDays);
        }
        
        public int getTotalBorrowRequests() {
            return loans.size();
        }
        
        public int getTotalApprovedLoans() {
            int count = 0;
            for (Loan loan : loans) {
                if (loan.isBorrowed()) {
                    count++;
                }
            }
            return count;
        }
        
        public List<Book> getAvailableBooks() {
            List<Book> available = new ArrayList<>();
            for (Book book : books) {
                if (book.isAvailable()) {
                    available.add(book);
                }
            }
            return available;
        }
        
        public List<Book> getBorrowedBooks() {
            List<Book> borrowed = new ArrayList<>();
            for (Book book : books) {
                if (!book.isAvailable()) {
                    borrowed.add(book);
                }
            }
            return borrowed;
        }
        
        public List<Student> getActiveStudents() {
            List<Student> active = new ArrayList<>();
            for (Student student : students) {
                if (student.isActive()) {
                    active.add(student);
                }
            }
            return active;
        }
        
        public List<Student> getInactiveStudents() {
            List<Student> inactive = new ArrayList<>();
            for (Student student : students) {
                if (!student.isActive()) {
                    inactive.add(student);
                }
            }
            return inactive;
        }
    }
    
    // تست‌ها
    public static void main(String[] args) {
        System.out.println("🧪 شروع تست‌های ReportController");
        System.out.println("=================================");
        
        TestReportController controller = new TestReportController();
        
        try {
            // تست ۱: تعداد دانشجویان ثبت‌نام کرده
            testGetRegisteredStudentsCount(controller);
            
            // تست ۲: تعداد کل کتاب‌ها
            testGetTotalBooksCount(controller);
            
            // تست ۳: تعداد کل امانت‌ها
            testGetTotalLoansCount(controller);
            
            // تست ۴: تعداد امانت‌های جاری
            testGetCurrentLoansCount(controller);
            
            // تست ۵: بررسی فعال بودن دانشجو
            testIsStudentActive(controller);
            
            // تست ۶: گزارش دانشجو
            testGetStudentReport(controller);
            
            // تست ۷: دانشجویان با بیشترین تاخیر
            testGetTop10DelayedStudents(controller);
            
            // تست ۸: آمار کتابخانه
            testGetLibraryStatistics(controller);
            
            // تست ۹: کتاب‌های موجود
            testGetAvailableBooks(controller);
            
            // تست ۱۰: کتاب‌های امانت داده شده
            testGetBorrowedBooks(controller);
            
            // تست ۱۱: دانشجویان فعال
            testGetActiveStudents(controller);
            
            System.out.println("\n🎉 تمام تست‌ها با موفقیت گذرانده شدند!");
            
        } catch (Exception e) {
            System.out.println("\n❌ خطا در تست: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // متدهای تست
    private static void testGetRegisteredStudentsCount(TestReportController controller) {
        System.out.print("📝 تست تعداد دانشجویان... ");
        int result = controller.getRegisteredStudentsCount();
        if (result != 3) {
            throw new RuntimeException("انتظار 3 دانشجو، دریافت: " + result);
        }
        System.out.println("✅");
    }
    
    private static void testGetTotalBooksCount(TestReportController controller) {
        System.out.print("📚 تست تعداد کتاب‌ها... ");
        int result = controller.getTotalBooksCount();
        if (result != 4) {
            throw new RuntimeException("انتظار 4 کتاب، دریافت: " + result);
        }
        System.out.println("✅");
    }
    
    private static void testGetTotalLoansCount(TestReportController controller) {
        System.out.print("📖 تست تعداد امانت‌ها... ");
        int result = controller.getTotalLoansCount();
        if (result != 3) {
            throw new RuntimeException("انتظار 3 امانت، دریافت: " + result);
        }
        System.out.println("✅");
    }
    
    private static void testGetCurrentLoansCount(TestReportController controller) {
        System.out.print("⏳ تست امانت‌های جاری... ");
        int result = controller.getCurrentLoansCount();
        if (result != 2) { // دو امانت باز هستند
            throw new RuntimeException("انتظار 2 امانت جاری، دریافت: " + result);
        }
        System.out.println("✅");
    }
    
    private static void testIsStudentActive(TestReportController controller) {
        System.out.print("👤 تست وضعیت دانشجو... ");
        boolean result = controller.isStudentActive("ST001");
        if (!result) {
            throw new RuntimeException("انتظار فعال بودن دانشجو ST001");
        }
        System.out.println("✅");
    }
    
    private static void testGetStudentReport(TestReportController controller) {
        System.out.print("📊 تست گزارش دانشجو... ");
        StudentReport result = controller.getStudentReport("ST001");
        if (result == null) {
            throw new RuntimeException("گزارش دانشجو null است");
        }
        if (result.getTotalLoans() != 2) {
            throw new RuntimeException("انتظار 2 امانت برای ST001، دریافت: " + result.getTotalLoans());
        }
        if (result.getNotReturnedCount() != 1) {
            throw new RuntimeException("انتظار 1 امانت تحویل نداده برای ST001، دریافت: " + result.getNotReturnedCount());
        }
        if (result.getDelayedLoansCount() != 1) {
            throw new RuntimeException("انتظار 1 امانت با تأخیر برای ST001، دریافت: " + result.getDelayedLoansCount());
        }
        System.out.println("✅");
    }
    
    private static void testGetTop10DelayedStudents(TestReportController controller) {
        System.out.print("⏰ تست دانشجویان با تاخیر... ");
        List<StudentDelayReport> result = controller.getTop10DelayedStudents();
        if (result == null) {
            throw new RuntimeException("لیست دانشجویان با تأخیر null است");
        }
        if (result.size() != 1) {
            throw new RuntimeException("انتظار 1 دانشجوی با تأخیر، دریافت: " + result.size());
        }
        if (!"ST001".equals(result.get(0).getStudentId())) {
            throw new RuntimeException("انتظار دانشجوی ST001 در لیست تأخیر");
        }
        System.out.println("✅");
    }
    
    private static void testGetLibraryStatistics(TestReportController controller) {
        System.out.print("📈 تست آمار کتابخانه... ");
        LibraryStats result = controller.getLibraryStatistics();
        if (result == null) {
            throw new RuntimeException("آمار کتابخانه null است");
        }
        if (result.getTotalBooks() != 4) {
            throw new RuntimeException("انتظار 4 کتاب در آمار، دریافت: " + result.getTotalBooks());
        }
        if (result.getTotalStudents() != 3) {
            throw new RuntimeException("انتظار 3 دانشجو در آمار، دریافت: " + result.getTotalStudents());
        }
        if (result.getTotalLoans() != 3) {
            throw new RuntimeException("انتظار 3 امانت در آمار، دریافت: " + result.getTotalLoans());
        }
        System.out.println("✅");
    }
    
    private static void testGetAvailableBooks(TestReportController controller) {
        System.out.print("✅ تست کتاب‌های موجود... ");
        List<Book> result = controller.getAvailableBooks();
        if (result == null) {
            throw new RuntimeException("لیست کتاب‌های موجود null است");
        }
        if (result.size() != 2) {
            throw new RuntimeException("انتظار 2 کتاب موجود، دریافت: " + result.size());
        }
        System.out.println("✅");
    }
    
    private static void testGetBorrowedBooks(TestReportController controller) {
        System.out.print("📥 تست کتاب‌های امانت داده شده... ");
        List<Book> result = controller.getBorrowedBooks();
        if (result == null) {
            throw new RuntimeException("لیست کتاب‌های امانت داده شده null است");
        }
        if (result.size() != 2) {
            throw new RuntimeException("انتظار 2 کتاب امانت داده شده، دریافت: " + result.size());
        }
        System.out.println("✅");
    }
    
    private static void testGetActiveStudents(TestReportController controller) {
        System.out.print("👥 تست دانشجویان فعال... ");
        List<Student> result = controller.getActiveStudents();
        if (result == null) {
            throw new RuntimeException("لیست دانشجویان فعال null است");
        }
        if (result.size() != 3) {
            throw new RuntimeException("انتظار 3 دانشجوی فعال، دریافت: " + result.size());
        }
        System.out.println("✅");
    }
}