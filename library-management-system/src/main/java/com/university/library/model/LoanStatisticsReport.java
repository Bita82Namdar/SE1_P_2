package com.university.library.model;

import java.util.List;

public class LoanStatisticsReport {
    private int totalRequests;
    private int totalApprovedLoans;
    private int totalReturnedLoans;
    private int totalDelayedLoans;
    private int activeLoans;
    private double averageLoanDays;
    private List<StudentDelayReport> topDelayedStudents;
    
    public LoanStatisticsReport(int totalRequests, int totalApprovedLoans,
                               int totalReturnedLoans, int totalDelayedLoans,
                               int activeLoans, double averageLoanDays,
                               List<StudentDelayReport> topDelayedStudents) {
        this.totalRequests = totalRequests;
        this.totalApprovedLoans = totalApprovedLoans;
        this.totalReturnedLoans = totalReturnedLoans;
        this.totalDelayedLoans = totalDelayedLoans;
        this.activeLoans = activeLoans;
        this.averageLoanDays = averageLoanDays;
        this.topDelayedStudents = topDelayedStudents;
    }
    
    // Getters
    public int getTotalRequests() { return totalRequests; }
    public int getTotalApprovedLoans() { return totalApprovedLoans; }
    public int getTotalReturnedLoans() { return totalReturnedLoans; }
    public int getTotalDelayedLoans() { return totalDelayedLoans; }
    public int getActiveLoans() { return activeLoans; }
    public double getAverageLoanDays() { return averageLoanDays; }
    public List<StudentDelayReport> getTopDelayedStudents() { return topDelayedStudents; }
}
