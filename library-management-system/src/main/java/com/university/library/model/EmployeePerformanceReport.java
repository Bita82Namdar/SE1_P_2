package com.university.library.model;

public class EmployeePerformanceReport {
    private String employeeId;
    private String employeeName;
    private int booksRegistered;
    private int booksLoanedOut;
    private int booksReceived;
    
    public EmployeePerformanceReport(String employeeId, String employeeName,
                                    int booksRegistered, int booksLoanedOut,
                                    int booksReceived) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.booksRegistered = booksRegistered;
        this.booksLoanedOut = booksLoanedOut;
        this.booksReceived = booksReceived;
    }
    
    // Getters
    public String getEmployeeId() { return employeeId; }
    public String getEmployeeName() { return employeeName; }
    public int getBooksRegistered() { return booksRegistered; }
    public int getBooksLoanedOut() { return booksLoanedOut; }
    public int getBooksReceived() { return booksReceived; }
}
