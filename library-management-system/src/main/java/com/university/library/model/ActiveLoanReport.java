package com.university.library.model;

import java.time.LocalDate;

public class ActiveLoanReport {
    private String loanId;
    private String studentId;
    private String bookId;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isOverdue;
    private long daysOverdue;
    
    public ActiveLoanReport(String loanId, String studentId, String bookId,
                          LocalDate startDate, LocalDate endDate,
                          boolean isOverdue, long daysOverdue) {
        this.loanId = loanId;
        this.studentId = studentId;
        this.bookId = bookId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isOverdue = isOverdue;
        this.daysOverdue = daysOverdue;
    }
    
    // Getters
    public String getLoanId() { return loanId; }
    public String getStudentId() { return studentId; }
    public String getBookId() { return bookId; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public boolean isOverdue() { return isOverdue; }
    public long getDaysOverdue() { return daysOverdue; }
}
