package com.university.library.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class LoanTest {

    @Test
    public void testLoanCreation() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(15);
        
        Loan loan = new Loan("L1", "B1", "S1", "E1", LocalDate.now(), startDate, endDate);
        
        assertEquals("L1", loan.getLoanId());
        assertEquals("B1", loan.getBookId());
        assertEquals(LoanStatus.REQUESTED, loan.getStatus());
        assertFalse(loan.isOverdue());
        assertEquals(0, loan.getOverdueDays());
    }

    @Test
    public void testOverdueLoan() {
        LocalDate startDate = LocalDate.now().minusDays(20);
        LocalDate endDate = LocalDate.now().minusDays(5);
        
        Loan loan = new Loan("L1", "B1", "S1", "E1", LocalDate.now().minusDays(25), startDate, endDate);
        
        assertTrue(loan.isOverdue());
        assertTrue(loan.getOverdueDays() > 0);
    }

    @Test
    public void testLoanStatusTransition() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(15);
        
        Loan loan = new Loan("L1", "B1", "S1", "E1", LocalDate.now(), startDate, endDate);
        
        // Test initial status
        assertEquals(LoanStatus.REQUESTED, loan.getStatus());
        
        // Test status change (assuming setStatus method exists)
        loan.setStatus(LoanStatus.APPROVED);
        assertEquals(LoanStatus.APPROVED, loan.getStatus());
        
        loan.setStatus(LoanStatus.BORROWED);
        assertEquals(LoanStatus.BORROWED, loan.getStatus());
        
        loan.setStatus(LoanStatus.RETURNED);
        assertEquals(LoanStatus.RETURNED, loan.getStatus());
    }

    @Test
    public void testLoanDates() {
        LocalDate requestDate = LocalDate.now().minusDays(5);
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(15);
        
        Loan loan = new Loan("L1", "B1", "S1", "E1", requestDate, startDate, endDate);
        
        assertEquals(requestDate, loan.getRequestDate());
        assertEquals(startDate, loan.getStartDate());
        assertEquals(endDate, loan.getEndDate());
        assertNull(loan.getActualReturnDate()); // Should be null initially
    }

    @Test
    public void testNotOverdueLoan() {
        LocalDate startDate = LocalDate.now().minusDays(5);
        LocalDate endDate = LocalDate.now().plusDays(10);
        
        Loan loan = new Loan("L1", "B1", "S1", "E1", LocalDate.now().minusDays(10), startDate, endDate);
        
        assertFalse(loan.isOverdue());
        assertEquals(0, loan.getOverdueDays());
    }
}