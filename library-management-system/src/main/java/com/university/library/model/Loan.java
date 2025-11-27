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
        this.status = LoanStatus.REQUESTED;
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

    // متدهای کمکی
    public boolean isOverdue() {
        return !isReturned && LocalDate.now().isAfter(endDate);
    }

    public long getDaysOverdue() {
        if (!isOverdue()) return 0;
        return LocalDate.now().toEpochDay() - endDate.toEpochDay();
    }

    public void approve(String employeeId) {
        this.employeeId = employeeId;
        this.status = LoanStatus.APPROVED;
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

    @Override
    public String toString() {
        return String.format("Loan{id='%s', student='%s', book='%s', status=%s}", 
                loanId, studentId, bookId, status);
    }
}
