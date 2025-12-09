package com.university.library.model;

import java.time.LocalDate;

public class Loan {
    private String loanId;
    private String studentId;
    private String bookId;
    private String employeeId;
    private LocalDate requestDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate actualReturnDate;
    private LoanStatus status;
    private boolean isReturned;

    public Loan(String loanId, String studentId, String bookId, String employeeId, 
                LocalDate startDate, LocalDate endDate) {
        this.loanId = loanId;
        this.studentId = studentId;
        this.bookId = bookId;
        this.employeeId = employeeId;
        this.requestDate = LocalDate.now();
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = LoanStatus.REQUESTED;  // این همان PENDING است
        this.isReturned = false;
    }

    // Getter methods
    public String getLoanId() { return loanId; }
    public String getStudentId() { return studentId; }
    public String getBookId() { return bookId; }
    public String getEmployeeId() { return employeeId; }
    public LocalDate getRequestDate() { return requestDate; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public LocalDate getActualReturnDate() { return actualReturnDate; }
    public LoanStatus getStatus() { return status; }
    public boolean isReturned() { return isReturned; }

    // Setter methods
    public void setLoanId(String loanId) { this.loanId = loanId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setBookId(String bookId) { this.bookId = bookId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public void setRequestDate(LocalDate requestDate) { this.requestDate = requestDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setActualReturnDate(LocalDate actualReturnDate) { 
        this.actualReturnDate = actualReturnDate; 
    }
    public void setStatus(LoanStatus status) { 
        this.status = status; 
        this.isReturned = (status == LoanStatus.RETURNED);
    }

    // ========== متدهای جدید برای سناریوها ==========
    
    // سناریو 1-3: ایجاد درخواست امانت
    public static Loan createBorrowRequest(String loanId, String studentId, String bookId,
                                          LocalDate startDate, LocalDate endDate) {
        // این constructor درخواست با وضعیت REQUESTED ایجاد می‌کند
        return new Loan(loanId, studentId, bookId, null, startDate, endDate);
    }
    
    // سناریو 3-4: تایید درخواست امانت
    public void approve(String employeeId) {
        // بررسی سناریو 3-5: اگر قبلاً تایید شده باشد
        if (this.status == LoanStatus.APPROVED || this.status == LoanStatus.BORROWED) {
            throw new IllegalStateException("این درخواست قبلاً تایید شده است");
        }
        
        // بررسی سناریو 3-3: (در Service بررسی می‌شود)
        this.employeeId = employeeId;
        this.status = LoanStatus.APPROVED;
    }
    
    // متد برای تحویل کتاب (تبدیل APPROVED به BORROWED)
    public void markAsBorrowed() {
        if (this.status != LoanStatus.APPROVED) {
            throw new IllegalStateException("فقط درخواست‌های تایید شده قابل تحویل هستند");
        }
        this.status = LoanStatus.BORROWED;
    }
    
    // ========== متدهای کمکی موجود ==========
    public boolean isOverdue() {
        return !isReturned && LocalDate.now().isAfter(endDate);
    }

    public long getDaysOverdue() {
        if (!isOverdue()) return 0;
        return LocalDate.now().toEpochDay() - endDate.toEpochDay();
    }

    public void borrow() {
        this.status = LoanStatus.BORROWED;
    }

    public void returnBook() {
        this.status = LoanStatus.RETURNED;
        this.actualReturnDate = LocalDate.now();
        this.isReturned = true;
    }

    public void markOverdue() {
        if (!isReturned && LocalDate.now().isAfter(endDate)) {
            this.status = LoanStatus.OVERDUE;
        }
    }

    public void reject() {
        this.status = LoanStatus.REJECTED;
    }
    
    // متدهای کمکی برای بررسی وضعیت‌ها
    public boolean isPending() {
        return this.status == LoanStatus.REQUESTED;
    }
    
    public boolean isApproved() {
        return this.status == LoanStatus.APPROVED;
    }
    
    public boolean isBorrowed() {
        return this.status == LoanStatus.BORROWED;
    }
    
    public boolean isActive() {
        return this.status == LoanStatus.APPROVED || 
               this.status == LoanStatus.BORROWED || 
               this.status == LoanStatus.OVERDUE;
    }

    @Override
    public String toString() {
        return String.format("Loan{id='%s', student='%s', book='%s', status=%s}", 
                loanId, studentId, bookId, status);
    }
}