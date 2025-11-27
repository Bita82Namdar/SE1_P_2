package com.university.library.service;

import com.university.library.model.Loan;
import com.university.library.model.LoanStatus;
import com.university.library.model.Book;
import com.university.library.model.Student;
import com.university.library.repository.LoanRepository;
import com.university.library.repository.BookRepository;
import com.university.library.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class LoanServiceTest {
    private LoanService loanService;
    private LoanRepository loanRepository;
    private BookRepository bookRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        loanRepository = LoanRepository.getInstance();
        bookRepository = BookRepository.getInstance();
        userRepository = UserRepository.getInstance();
        
        // پاک کردن داده‌های تست
        loanRepository.getAllLoans().clear();
        loanService = new LoanService();
    }

    @Test
    void testRequestLoan_Success() {
        // اضافه کردن کتاب و دانشجو برای تست
        bookRepository.addBook(new Book("B_TEST", "Test Book", "Test Author", "123-456789", 2023, "Test Pub", 2));
        userRepository.addUser(new Student("S_TEST", "teststudent", "password", "Test Student", "ST_TEST"));
        
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(15);
        
        Optional<Loan> loan = loanService.requestLoan("B_TEST", "S_TEST", startDate, endDate);
        
        assertTrue(loan.isPresent(), "Loan should be created successfully");
        assertEquals(LoanStatus.REQUESTED, loan.get().getStatus());
        assertEquals("B_TEST", loan.get().getBookId());
        assertEquals("S_TEST", loan.get().getStudentId());
    }

    @Test
    void testRequestLoan_BookNotAvailable() {
        bookRepository.addBook(new Book("B_UNAVAILABLE", "Unavailable Book", "Author", "111-111111", 2023, "Pub", 0));
        userRepository.addUser(new Student("S1", "student1", "pass", "Student 1", "ST1"));
        
        Optional<Loan> loan = loanService.requestLoan("B_UNAVAILABLE", "S1", 
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(15));
        
        assertFalse(loan.isPresent(), "Loan should not be created for unavailable book");
    }

    @Test
    void testApproveLoan_Success() {
        // ایجاد وام تستی
        bookRepository.addBook(new Book("B_APPROVE", "Approve Book", "Author", "222-222222", 2023, "Pub", 3));
        userRepository.addUser(new Student("S_APPROVE", "approvestudent", "pass", "Approve Student", "ST_APPROVE"));
        
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(15);
        
        Optional<Loan> loan = loanService.requestLoan("B_APPROVE", "S_APPROVE", startDate, endDate);
        assertTrue(loan.isPresent());
        
        boolean approvalResult = loanService.approveLoan(loan.get().getLoanId(), "E1");
        assertTrue(approvalResult, "Loan should be approved successfully");
        
        Optional<Loan> approvedLoan = loanRepository.findById(loan.get().getLoanId());
        assertTrue(approvedLoan.isPresent());
        assertEquals(LoanStatus.BORROWED, approvedLoan.get().getStatus());
        assertEquals("E1", approvedLoan.get().getEmployeeId());
    }

    @Test
    void testReturnLoan_Success() {
        bookRepository.addBook(new Book("B_RETURN", "Return Book", "Author", "333-333333", 2023, "Pub", 2));
        userRepository.addUser(new Student("S_RETURN", "returnstudent", "pass", "Return Student", "ST_RETURN"));
        
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(15);
        
        Optional<Loan> loan = loanService.requestLoan("B_RETURN", "S_RETURN", startDate, endDate);
        assertTrue(loan.isPresent());
        
        loanService.approveLoan(loan.get().getLoanId(), "E1");
        
        boolean returnResult = loanService.returnLoan(loan.get().getLoanId());
        assertTrue(returnResult, "Loan should be returned successfully");
        
        Optional<Loan> returnedLoan = loanRepository.findById(loan.get().getLoanId());
        assertTrue(returnedLoan.isPresent());
        assertEquals(LoanStatus.RETURNED, returnedLoan.get().getStatus());
        assertNotNull(returnedLoan.get().getActualReturnDate());
    }

    @Test
    void testGetStudentLoanHistory() {
        bookRepository.addBook(new Book("B_HISTORY", "History Book", "Author", "444-444444", 2023, "Pub", 2));
        userRepository.addUser(new Student("S_HISTORY", "historystudent", "pass", "History Student", "ST_HISTORY"));
        
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(15);
        
        loanService.requestLoan("B_HISTORY", "S_HISTORY", startDate, endDate);
        
        List<Loan> history = loanService.getStudentLoanHistory("S_HISTORY");
        assertFalse(history.isEmpty(), "Student should have loan history");
        assertEquals("S_HISTORY", history.get(0).getStudentId());
    }

    @Test
    void testGetPendingLoans() {
        bookRepository.addBook(new Book("B_PENDING", "Pending Book", "Author", "555-555555", 2023, "Pub", 2));
        userRepository.addUser(new Student("S_PENDING", "pendingstudent", "pass", "Pending Student", "ST_PENDING"));
        
        loanService.requestLoan("B_PENDING", "S_PENDING", 
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(15));
        
        List<Loan> pendingLoans = loanService.getPendingLoans();
        assertFalse(pendingLoans.isEmpty(), "There should be pending loans");
    }

    @Test
    void testLoanStatistics() {
        bookRepository.addBook(new Book("B_STATS", "Stats Book", "Author", "666-666666", 2023, "Pub", 2));
        userRepository.addUser(new Student("S_STATS", "statsstudent", "pass", "Stats Student", "ST_STATS"));
        
        // ایجاد چند وام برای آمار
        loanService.requestLoan("B_STATS", "S_STATS", 
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(15));
        
        LoanStatistics stats = loanService.getStudentLoanStatistics("S_STATS");
        assertNotNull(stats, "Statistics should not be null");
        assertEquals(1, stats.getTotalLoans());
    }
}
