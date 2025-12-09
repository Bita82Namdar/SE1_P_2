package com.university.library.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

public class LoanTest {
    
    @Test
    void testCreateBorrowRequest_ReturnsLoanWithRequestedStatus() {
        // سناریو 1-3 در سطح مدل
        Loan loan = Loan.createBorrowRequest("L001", "S001", "B001", 
                                           LocalDate.now(), LocalDate.now().plusDays(7));
        
        assertEquals(LoanStatus.REQUESTED, loan.getStatus());
        assertTrue(loan.isPending());
    }
    
    @Test
    void testApprove_WhenAlreadyApproved_ThrowsException() {
        // سناریو 3-5 در سطح مدل
        Loan loan = new Loan("L002", "S002", "B002", "E001", 
                           LocalDate.now(), LocalDate.now().plusDays(7));
        loan.approve("E001"); // تایید اولیه
        
        // تلاش برای تایید مجدد باید Exception بدهد
        assertThrows(IllegalStateException.class, () -> {
            loan.approve("E002");
        });
    }
    
    @Test
    void testMarkAsBorrowed_WhenNotApproved_ThrowsException() {
        Loan loan = new Loan("L003", "S003", "B003", null, 
                           LocalDate.now(), LocalDate.now().plusDays(7));
        // هنوز تایید نشده (REQUESTED)
        
        assertThrows(IllegalStateException.class, () -> {
            loan.markAsBorrowed();
        });
    }
}