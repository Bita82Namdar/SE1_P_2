package com.university.library.repository;

import com.university.library.model.Loan;
import com.university.library.model.LoanStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class LoanRepositoryTest {
    private LoanRepository loanRepository;

    @BeforeEach
    void setUp() {
        loanRepository = LoanRepository.getInstance();
    }

    @Test
    void testFindById_ExistingLoan() {
        Optional<Loan> loan = loanRepository.findById("L1");
        assertTrue(loan.isPresent());
        assertEquals("S1", loan.get().getStudentId());
    }

    @Test
    void testFindByStudentId() {
        List<Loan> studentLoans = loanRepository.findByStudentId("S1");
        assertFalse(studentLoans.isEmpty());
        assertEquals("S1", studentLoans.get(0).getStudentId());
    }

    @Test
    void testFindByBookId() {
        List<Loan> bookLoans = loanRepository.findByBookId("B1");
        assertFalse(bookLoans.isEmpty());
        assertEquals("B1", bookLoans.get(0).getBookId());
    }

    @Test
    void testFindPendingLoans() {
        List<Loan> pendingLoans = loanRepository.findPendingLoans();
        // باید حداقل یک وام در وضعیت REQUESTED داشته باشیم
        assertTrue(pendingLoans.size() >= 1);
    }

    @Test
    void testGetActiveLoans() {
        List<Loan> activeLoans = loanRepository.getActiveLoans();
        assertFalse(activeLoans.isEmpty());
    }

    @Test
    void testAddAndUpdateLoan() {
        LocalDate startDate = LocalDate.now().plusDays(2);
        LocalDate endDate = LocalDate.now().plusDays(16);
        Loan newLoan = new Loan("L3", "S3", "B3", null, startDate, endDate);
        
        loanRepository.addLoan(newLoan);
        
        Optional<Loan> retrievedLoan = loanRepository.findById("L3");
        assertTrue(retrievedLoan.isPresent());
        assertEquals(LoanStatus.REQUESTED, retrievedLoan.get().getStatus());
        
        // تأیید وام
        newLoan.approve("E1");
        newLoan.borrow();
        loanRepository.updateLoan(newLoan);
        
        retrievedLoan = loanRepository.findById("L3");
        assertEquals(LoanStatus.BORROWED, retrievedLoan.get().getStatus());
    }
}
