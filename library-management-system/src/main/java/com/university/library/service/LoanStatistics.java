package com.university.library.service;

import com.university.library.model.Loan;
import com.university.library.model.LoanStatus;

import java.util.List;

public class LoanStatistics {
    private int totalLoans;
    private int notReturnedCount;
    private int delayedReturnCount;
    private int returnedOnTimeCount;

    public LoanStatistics(List<Loan> loans) {
        this.totalLoans = loans.size();
        this.notReturnedCount = (int) loans.stream()
                .filter(loan -> loan.getStatus() != LoanStatus.RETURNED)
                .count();
        this.delayedReturnCount = (int) loans.stream()
                .filter(Loan::isOverdue)
                .count();
        this.returnedOnTimeCount = (int) loans.stream()
                .filter(loan -> loan.getStatus() == LoanStatus.RETURNED && !loan.isOverdue())
                .count();
    }

    // Getters
    public int getTotalLoans() { return totalLoans; }
    public int getNotReturnedCount() { return notReturnedCount; }
    public int getDelayedReturnCount() { return delayedReturnCount; }
    public int getReturnedOnTimeCount() { return returnedOnTimeCount; }

    @Override
    public String toString() {
        return String.format("LoanStatistics{total=%d, notReturned=%d, delayed=%d, onTime=%d}", 
                totalLoans, notReturnedCount, delayedReturnCount, returnedOnTimeCount);
    }
}