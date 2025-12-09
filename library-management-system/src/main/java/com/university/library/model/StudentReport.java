package com.university.library.model;

public class StudentReport {
    private int totalLoans;
    private int notReturnedCount;
    private int delayedLoansCount;
    
    public StudentReport(int totalLoans, int notReturnedCount, int delayedLoansCount) {
        this.totalLoans = totalLoans;
        this.notReturnedCount = notReturnedCount;
        this.delayedLoansCount = delayedLoansCount;
    }
    
    // Getters
    public int getTotalLoans() { return totalLoans; }
    public int getNotReturnedCount() { return notReturnedCount; }
    public int getDelayedLoansCount() { return delayedLoansCount; }
    
    @Override
    public String toString() {
        return String.format("StudentReport{totalLoans=%d, notReturned=%d, delayed=%d}",
            totalLoans, notReturnedCount, delayedLoansCount);
    }
}
