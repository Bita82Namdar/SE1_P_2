package com.university.library.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Loan {
    private String loanId;
    private String bookId;
    private String studentId;
    private String employeeId;
    private LocalDate requestDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate actualReturnDate;
    private LoanStatus status;

    public Loan(String loanId, String bookId, String studentId, String employeeId, 
                LocalDate requestDate, LocalDate startDate, LocalDate endDate) {
        this.loanId = loanId;
        this.bookId = bookId;
        this.studentId = studentId;
        this.employeeId = employeeId;
        this.requestDate = requestDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = LoanStatus.REQUESTED;
    }

    // Getters and Setters
    public String getLoanId() { return loanId; }
    public void setLoanId(String loanId) { this.loanId = loanId; }
    
    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    
    public LocalDate getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDate requestDate) { this.requestDate = requestDate; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    public LocalDate getActualReturnDate() { return actualReturnDate; }
    public void setActualReturnDate(LocalDate actualReturnDate) { this.actualReturnDate = actualReturnDate; }
    
    public LoanStatus getStatus() { return status; }
    public void setStatus(LoanStatus status) { this.status = status; }

    public boolean isOverdue() {
        if (actualReturnDate != null) {
            return actualReturnDate.isAfter(endDate);
        }
        return LocalDate.now().isAfter(endDate);
    }

    public long getOverdueDays() {
        if (isOverdue()) {
            LocalDate comparisonDate = actualReturnDate != null ? actualReturnDate : LocalDate.now();
            return ChronoUnit.DAYS.between(endDate, comparisonDate);
        }
        return 0;
    }
}

enum LoanStatus {
    REQUESTED, APPROVED, BORROWED, RETURNED, REJECTED
}