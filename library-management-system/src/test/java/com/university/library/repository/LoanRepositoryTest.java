package com.university.library.repository;

import java.util.*;

public class LoanRepositoryTest {
    
    // کلاس داخلی LoanStatus
    enum LoanStatus {
        REQUESTED,    // درخواست شده
        APPROVED,     // تأیید شده
        BORROWED,     // امانت داده شده
        RETURNED,     // بازگردانده شده
        OVERDUE,      // تأخیر دارد
        REJECTED      // رد شده
    }
    
    // کلاس داخلی Loan
    static class Loan {
        private String id;
        private String studentId;
        private String bookId;
        private String employeeId;
        private Date startDate;
        private Date endDate;
        private Date returnDate;
        private LoanStatus status;
        
        public Loan(String id, String studentId, String bookId) {
            this.id = id;
            this.studentId = studentId;
            this.bookId = bookId;
            this.startDate = new Date();
            this.endDate = addDays(new Date(), 14); // مهلت ۱۴ روز
            this.status = LoanStatus.REQUESTED;
        }
        
        public Loan(String id, String studentId, String bookId, String employeeId, 
                   Date startDate, Date endDate) {
            this.id = id;
            this.studentId = studentId;
            this.bookId = bookId;
            this.employeeId = employeeId;
            this.startDate = startDate;
            this.endDate = endDate;
            this.status = LoanStatus.REQUESTED;
        }
        
        // Getters
        public String getId() { return id; }
        public String getStudentId() { return studentId; }
        public String getBookId() { return bookId; }
        public String getEmployeeId() { return employeeId; }
        public Date getStartDate() { return startDate; }
        public Date getEndDate() { return endDate; }
        public Date getReturnDate() { return returnDate; }
        public LoanStatus getStatus() { return status; }
        
        // Setters
        public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
        public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }
        public void setStatus(LoanStatus status) { this.status = status; }
        
        // عملیات وام
        public void approve(String employeeId) {
            this.employeeId = employeeId;
            this.status = LoanStatus.APPROVED;
        }
        
        public void borrow() {
            this.status = LoanStatus.BORROWED;
        }
        
        public void returnLoan() {
            this.returnDate = new Date();
            this.status = LoanStatus.RETURNED;
        }
        
        public void reject(String employeeId) {
            this.employeeId = employeeId;
            this.status = LoanStatus.REJECTED;
        }
        
        // بررسی تأخیر
        public boolean isOverdue() {
            if (status == LoanStatus.RETURNED || status == LoanStatus.REJECTED) {
                return false;
            }
            return new Date().after(endDate);
        }
        
        public boolean isActive() {
            return status == LoanStatus.BORROWED || 
                   status == LoanStatus.APPROVED;
        }
        
        public boolean isPending() {
            return status == LoanStatus.REQUESTED;
        }
        
        // متد کمکی برای اضافه کردن روز
        private Date addDays(Date date, int days) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, days);
            return calendar.getTime();
        }
        
        @Override
        public String toString() {
            return "وام " + id + " - دانشجو: " + studentId + " - کتاب: " + bookId + 
                   " - وضعیت: " + status + (isOverdue() ? " (تأخیر)" : "");
        }
    }
    
    // کلاس LoanRepository برای تست
    static class TestLoanRepository {
        private Map<String, Loan> loans = new HashMap<>();
        private int loanCounter = 1;
        
        public TestLoanRepository() {
            // اضافه کردن وام‌های نمونه
            initializeSampleLoans();
        }
        
        private void initializeSampleLoans() {
            // وام فعال
            Loan loan1 = new Loan("L1", "S1", "B1", "E1", 
                                 new Date(), addDays(new Date(), 10));
            loan1.borrow();
            loans.put("L1", loan1);
            
            // وام درخواست شده (در انتظار)
            Loan loan2 = new Loan("L2", "S2", "B2");
            loans.put("L2", loan2);
            
            // وام تأیید شده
            Loan loan3 = new Loan("L3", "S1", "B3");
            loan3.approve("E2");
            loans.put("L3", loan3);
            
            // وام بازگردانده شده
            Loan loan4 = new Loan("L4", "S3", "B4");
            loan4.approve("E1");
            loan4.borrow();
            loan4.returnLoan();
            loans.put("L4", loan4);
            
            // وام رد شده
            Loan loan5 = new Loan("L5", "S4", "B5");
            loan5.reject("E3");
            loans.put("L5", loan5);
            
            loanCounter = 6;
        }
        
        private Date addDays(Date date, int days) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, days);
            return calendar.getTime();
        }
        
        // ۱. یافتن وام با ID
        public Optional<Loan> findById(String id) {
            return Optional.ofNullable(loans.get(id));
        }
        
        // ۲. یافتن وام‌های دانشجو
        public List<Loan> findByStudentId(String studentId) {
            List<Loan> result = new ArrayList<>();
            for (Loan loan : loans.values()) {
                if (loan.getStudentId().equals(studentId)) {
                    result.add(loan);
                }
            }
            return result;
        }
        
        // ۳. یافتن وام‌های کتاب
        public List<Loan> findByBookId(String bookId) {
            List<Loan> result = new ArrayList<>();
            for (Loan loan : loans.values()) {
                if (loan.getBookId().equals(bookId)) {
                    result.add(loan);
                }
            }
            return result;
        }
        
        // ۴. یافتن وام‌های در انتظار
        public List<Loan> findPendingLoans() {
            List<Loan> result = new ArrayList<>();
            for (Loan loan : loans.values()) {
                if (loan.isPending()) {
                    result.add(loan);
                }
            }
            return result;
        }
        
        // ۵. دریافت وام‌های فعال
        public List<Loan> getActiveLoans() {
            List<Loan> result = new ArrayList<>();
            for (Loan loan : loans.values()) {
                if (loan.isActive()) {
                    result.add(loan);
                }
            }
            return result;
        }
        
        // ۶. اضافه کردن وام جدید
        public void addLoan(Loan loan) {
            loans.put(loan.getId(), loan);
        }
        
        // ۷. به‌روزرسانی وام
        public void updateLoan(Loan loan) {
            loans.put(loan.getId(), loan);
        }
        
        // ۸. حذف وام
        public boolean deleteLoan(String id) {
            return loans.remove(id) != null;
        }
        
        // ۹. دریافت تمام وام‌ها
        public List<Loan> getAllLoans() {
            return new ArrayList<>(loans.values());
        }
        
        // ۱۰. تعداد وام‌ها
        public int getTotalLoans() {
            return loans.size();
        }
        
        // ۱۱. ایجاد وام جدید
        public Loan createLoan(String studentId, String bookId) {
            String loanId = "L" + loanCounter++;
            Loan newLoan = new Loan(loanId, studentId, bookId);
            loans.put(loanId, newLoan);
            return newLoan;
        }
        
        // ۱۲. تأیید وام
        public boolean approveLoan(String loanId, String employeeId) {
            Loan loan = loans.get(loanId);
            if (loan == null || !loan.isPending()) {
                return false;
            }
            
            loan.approve(employeeId);
            return true;
        }
        
        // ۱۳. امانت دادن کتاب
        public boolean borrowLoan(String loanId) {
            Loan loan = loans.get(loanId);
            if (loan == null || loan.getStatus() != LoanStatus.APPROVED) {
                return false;
            }
            
            loan.borrow();
            return true;
        }
        
        // ۱۴. بازگرداندن کتاب
        public boolean returnLoan(String loanId) {
            Loan loan = loans.get(loanId);
            if (loan == null || !loan.isActive()) {
                return false;
            }
            
            loan.returnLoan();
            return true;
        }
        
        // ۱۵. رد وام
        public boolean rejectLoan(String loanId, String employeeId) {
            Loan loan = loans.get(loanId);
            if (loan == null || !loan.isPending()) {
                return false;
            }
            
            loan.reject(employeeId);
            return true;
        }
    }
    
    // تست‌ها
    public static void main(String[] args) {
        System.out.println("📖 شروع تست‌های LoanRepository");
        System.out.println("==============================");
        
        TestLoanRepository repository = new TestLoanRepository();
        int passedTests = 0;
        int totalTests = 0;
        
        try {
            totalTests++;
            System.out.print("\n1. تست یافتن وام موجود با ID... ");
            testFindById_ExistingLoan(repository);
            System.out.println("✅");
            passedTests++;
            
            totalTests++;
            System.out.print("2. تست یافتن وام‌های دانشجو... ");
            testFindByStudentId(repository);
            System.out.println("✅");
            passedTests++;
            
            totalTests++;
            System.out.print("3. تست یافتن وام‌های کتاب... ");
            testFindByBookId(repository);
            System.out.println("✅");
            passedTests++;
            
            totalTests++;
            System.out.print("4. تست یافتن وام‌های در انتظار... ");
            testFindPendingLoans(repository);
            System.out.println("✅");
            passedTests++;
            
            totalTests++;
            System.out.print("5. تست دریافت وام‌های فعال... ");
            testGetActiveLoans(repository);
            System.out.println("✅");
            passedTests++;
            
            totalTests++;
            System.out.print("6. تست اضافه کردن و به‌روزرسانی وام... ");
            testAddAndUpdateLoan(repository);
            System.out.println("✅");
            passedTests++;
            
            totalTests++;
            System.out.print("7. تست تأیید وام... ");
            testApproveLoan(repository);
            System.out.println("✅");
            passedTests++;
            
            totalTests++;
            System.out.print("8. تست امانت دادن کتاب... ");
            testBorrowLoan(repository);
            System.out.println("✅");
            passedTests++;
            
            totalTests++;
            System.out.print("9. تست بازگرداندن کتاب... ");
            testReturnLoan(repository);
            System.out.println("✅");
            passedTests++;
            
            totalTests++;
            System.out.print("10. تست رد وام... ");
            testRejectLoan(repository);
            System.out.println("✅");
            passedTests++;
            
            System.out.println("\n📊 نتایج تست:");
            System.out.println("   تست‌های گذرانده شده: " + passedTests + " از " + totalTests);
            System.out.println("   نرخ موفقیت: " + (passedTests * 100 / totalTests) + "%");
            
            if (passedTests == totalTests) {
                System.out.println("\n🎉 تمام تست‌ها با موفقیت گذرانده شدند!");
            } else {
                System.out.println("\n⚠️  برخی تست‌ها ناموفق بودند!");
            }
            
        } catch (Exception e) {
            System.out.println("❌");
            System.out.println("خطا در تست: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // متدهای تست
    private static void testFindById_ExistingLoan(TestLoanRepository repository) {
        Optional<Loan> loan = repository.findById("L1");
        if (!loan.isPresent()) {
            throw new RuntimeException("وام با ID L1 باید موجود باشد");
        }
        if (!loan.get().getStudentId().equals("S1")) {
            throw new RuntimeException("دانشجوی وام L1 باید S1 باشد");
        }
    }
    
    private static void testFindByStudentId(TestLoanRepository repository) {
        List<Loan> studentLoans = repository.findByStudentId("S1");
        if (studentLoans.isEmpty()) {
            throw new RuntimeException("باید وام‌هایی برای دانشجوی S1 وجود داشته باشد");
        }
        if (!studentLoans.get(0).getStudentId().equals("S1")) {
            throw new RuntimeException("همه وام‌های برگشتی باید متعلق به دانشجوی S1 باشند");
        }
    }
    
    private static void testFindByBookId(TestLoanRepository repository) {
        List<Loan> bookLoans = repository.findByBookId("B1");
        if (bookLoans.isEmpty()) {
            throw new RuntimeException("باید وام‌هایی برای کتاب B1 وجود داشته باشد");
        }
        if (!bookLoans.get(0).getBookId().equals("B1")) {
            throw new RuntimeException("همه وام‌های برگشتی باید متعلق به کتاب B1 باشند");
        }
    }
    
    private static void testFindPendingLoans(TestLoanRepository repository) {
        List<Loan> pendingLoans = repository.findPendingLoans();
        if (pendingLoans.size() < 1) {
            throw new RuntimeException("باید حداقل یک وام در وضعیت درخواست شده وجود داشته باشد");
        }
        
        for (Loan loan : pendingLoans) {
            if (!loan.isPending()) {
                throw new RuntimeException("تمام وام‌های لیست باید در وضعیت درخواست شده باشند");
            }
        }
    }
    
    private static void testGetActiveLoans(TestLoanRepository repository) {
        List<Loan> activeLoans = repository.getActiveLoans();
        if (activeLoans.isEmpty()) {
            throw new RuntimeException("باید حداقل یک وام فعال وجود داشته باشد");
        }
        
        for (Loan loan : activeLoans) {
            if (!loan.isActive()) {
                throw new RuntimeException("تمام وام‌های لیست باید فعال باشند");
            }
        }
    }
    
    private static void testAddAndUpdateLoan(TestLoanRepository repository) {
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 14 * 24 * 60 * 60 * 1000L); // 14 روز بعد
        
        Loan newLoan = new Loan("L10", "S10", "B10", null, startDate, endDate);
        
        repository.addLoan(newLoan);
        
        Optional<Loan> retrievedLoan = repository.findById("L10");
        if (!retrievedLoan.isPresent()) {
            throw new RuntimeException("وام جدید باید اضافه شده باشد");
        }
        if (retrievedLoan.get().getStatus() != LoanStatus.REQUESTED) {
            throw new RuntimeException("وام جدید باید در وضعیت درخواست شده باشد");
        }
        
        // تأیید وام
        newLoan.approve("E10");
        newLoan.borrow();
        repository.updateLoan(newLoan);
        
        retrievedLoan = repository.findById("L10");
        if (retrievedLoan.get().getStatus() != LoanStatus.BORROWED) {
            throw new RuntimeException("وام باید در وضعیت امانت داده شده باشد");
        }
    }
    
    private static void testApproveLoan(TestLoanRepository repository) {
        Loan newLoan = repository.createLoan("S20", "B20");
        String loanId = newLoan.getId();
        
        boolean approved = repository.approveLoan(loanId, "E20");
        if (!approved) {
            throw new RuntimeException("وام باید تأیید شود");
        }
        
        Loan loan = repository.findById(loanId).get();
        if (loan.getStatus() != LoanStatus.APPROVED) {
            throw new RuntimeException("وضعیت وام باید تأیید شده باشد");
        }
        if (!loan.getEmployeeId().equals("E20")) {
            throw new RuntimeException("کارمند تأیید کننده باید E20 باشد");
        }
    }
    
    private static void testBorrowLoan(TestLoanRepository repository) {
        Loan newLoan = repository.createLoan("S30", "B30");
        String loanId = newLoan.getId();
        
        // ابتدا تأیید کنیم
        repository.approveLoan(loanId, "E30");
        
        // سپس امانت دهیم
        boolean borrowed = repository.borrowLoan(loanId);
        if (!borrowed) {
            throw new RuntimeException("وام باید امانت داده شود");
        }
        
        Loan loan = repository.findById(loanId).get();
        if (loan.getStatus() != LoanStatus.BORROWED) {
            throw new RuntimeException("وضعیت وام باید امانت داده شده باشد");
        }
    }
    
    private static void testReturnLoan(TestLoanRepository repository) {
        Loan newLoan = repository.createLoan("S40", "B40");
        String loanId = newLoan.getId();
        
        // تأیید و امانت
        repository.approveLoan(loanId, "E40");
        repository.borrowLoan(loanId);
        
        // بازگرداندن
        boolean returned = repository.returnLoan(loanId);
        if (!returned) {
            throw new RuntimeException("کتاب باید بازگردانده شود");
        }
        
        Loan loan = repository.findById(loanId).get();
        if (loan.getStatus() != LoanStatus.RETURNED) {
            throw new RuntimeException("وضعیت وام باید بازگردانده شده باشد");
        }
        if (loan.getReturnDate() == null) {
            throw new RuntimeException("تاریخ بازگرداندن باید تنظیم شده باشد");
        }
    }
    
    private static void testRejectLoan(TestLoanRepository repository) {
        Loan newLoan = repository.createLoan("S50", "B50");
        String loanId = newLoan.getId();
        
        boolean rejected = repository.rejectLoan(loanId, "E50");
        if (!rejected) {
            throw new RuntimeException("وام باید رد شود");
        }
        
        Loan loan = repository.findById(loanId).get();
        if (loan.getStatus() != LoanStatus.REJECTED) {
            throw new RuntimeException("وضعیت وام باید رد شده باشد");
        }
        if (!loan.getEmployeeId().equals("E50")) {
            throw new RuntimeException("کارمند رد کننده باید E50 باشد");
        }
    }
}