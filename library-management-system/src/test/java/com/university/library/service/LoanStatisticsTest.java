package com.university.library.service;

import com.university.library.model.Loan;
import com.university.library.model.LoanStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanStatisticsTest {

    @Test
    void testLoanStatisticsCalculation() {
        List<Loan> loans = new ArrayList<>();
        
        // ایجاد وام‌های تستی
        Loan returnedLoan = new Loan("L1", "S1", "B1", "E1", 
                LocalDate.now().minusDays(10), LocalDate.now().minusDays(5));
        returnedLoan.returnBook();
        
        Loan overdueLoan = new Loan("L2", "S1", "B2", "E1", 
                LocalDate.now().minusDays(20), LocalDate.now().minusDays(1));
        overdueLoan.borrow();
        
        Loan pendingLoan = new Loan("L3", "S1", "B3", null, 
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(15));
        
        loans.add(returnedLoan);
        loans.add(overdueLoan);
        loans.add(pendingLoan);
        
        LoanStatistics stats = new LoanStatistics(loans);
        
        assertEquals(3, stats.getTotalLoans());
        assertEquals(2, stats.getNotReturnedCount()); // overdue + pending
        assertEquals(1, stats.getDelayedReturnCount()); // فقط overdue
        assertEquals(1, stats.getReturnedOnTimeCount()); // returned on time
    }
}
