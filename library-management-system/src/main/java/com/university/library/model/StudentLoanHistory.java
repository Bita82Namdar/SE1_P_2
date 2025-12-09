package com.university.library.model;

import java.util.List;

public class StudentLoanHistory {
    private String studentId;
    private List<Loan> loans;
    private StudentReport summary;
    
    public StudentLoanHistory(String studentId, List<Loan> loans, StudentReport summary) {
        this.studentId = studentId;
        this.loans = loans;
        this.summary = summary;
    }
    
    // Getters
    public String getStudentId() { return studentId; }
    public List<Loan> getLoans() { return loans; }
    public StudentReport getSummary() { return summary; }
}
