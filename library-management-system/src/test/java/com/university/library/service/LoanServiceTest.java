package com.university.library.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.university.library.model.Loan;
import com.university.library.model.LoanStatus;
import com.university.library.repository.LoanRepository;
import com.university.library.repository.BookRepository;
import com.university.library.repository.UserRepository;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {
    
    @Mock private LoanRepository loanRepository;
    @Mock private BookRepository bookRepository;
    @Mock private UserRepository userRepository;
    @Mock private StudentService studentService;
    @Mock private BookService bookService;
    
    private LoanService loanService;
    
    @BeforeEach
    void setUp() {
        loanService = new LoanService(loanRepository, bookRepository, userRepository, 
                                     studentService, bookService);
    }
    
    @Test
    void testCreateBorrowRequest_ActiveStudent_AvailableBook() {
        // سناریو 1-3
        // Arrange
        String loanId = "L001";
        String studentId = "S001";
        String bookId = "B001";
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(14);
        
        when(studentService.isStudentActive(studentId)).thenReturn(true);
        when(bookService.isBookAvailable(bookId)).thenReturn(true);
        
        // Act
        Loan result = loanService.createBorrowRequest(loanId, studentId, bookId, startDate, endDate);
        
        // Assert
        assertNotNull(result);
        assertEquals(LoanStatus.REQUESTED, result.getStatus());
        assertEquals(studentId, result.getStudentId());
        assertEquals(bookId, result.getBookId());
    }
    
    @Test
    void testCreateBorrowRequest_InactiveStudent_ThrowsException() {
        // سناریو 3-2
        // Arrange
        String loanId = "L002";
        String studentId = "S002"; // غیرفعال
        String bookId = "B002";
        
        when(studentService.isStudentActive(studentId)).thenReturn(false);
        
        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            loanService.createBorrowRequest(loanId, studentId, bookId, 
                                          LocalDate.now(), LocalDate.now().plusDays(7));
        });
        
        assertTrue(exception.getMessage().contains("دانشجو غیرفعال است"));
    }
    
    @Test
    void testCreateBorrowRequest_UnavailableBook_ThrowsException() {
        // سناریو 3-3
        // Arrange
        String loanId = "L003";
        String studentId = "S003";
        String bookId = "B003"; // کتاب امانت داده شده
        
        when(studentService.isStudentActive(studentId)).thenReturn(true);
        when(bookService.isBookAvailable(bookId)).thenReturn(false);
        
        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            loanService.createBorrowRequest(loanId, studentId, bookId, 
                                          LocalDate.now(), LocalDate.now().plusDays(7));
        });
        
        assertTrue(exception.getMessage().contains("کتاب موجود نیست"));
    }
    
    @Test
    void testApproveBorrowRequest_ValidRequest() {
        // سناریو 3-4
        // Arrange
        String loanId = "L004";
        String employeeId = "E001";
        
        Loan pendingLoan = new Loan(loanId, "S004", "B004", null, 
                                   LocalDate.now(), LocalDate.now().plusDays(7));
        
        when(loanRepository.findById(loanId)).thenReturn(java.util.Optional.of(pendingLoan));
        when(bookService.isBookAvailable("B004")).thenReturn(true);
        
        // Act
        Loan result = loanService.approveBorrowRequest(loanId, employeeId);
        
        // Assert
        assertEquals(LoanStatus.APPROVED, result.getStatus());
        assertEquals(employeeId, result.getEmployeeId());
        verify(bookService).markBookAsBorrowed("B004");
    }
    
    @Test
    void testApproveBorrowRequest_AlreadyApproved_ThrowsException() {
        // سناریو 3-5
        // Arrange
        String loanId = "L005";
        String employeeId = "E001";
        
        Loan approvedLoan = new Loan(loanId, "S005", "B005", employeeId, 
                                    LocalDate.now(), LocalDate.now().plusDays(7));
        approvedLoan.approve(employeeId); // وضعیت APPROVED
        
        when(loanRepository.findById(loanId)).thenReturn(java.util.Optional.of(approvedLoan));
        
        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            loanService.approveBorrowRequest(loanId, employeeId);
        });
        
        assertTrue(exception.getMessage().contains("این درخواست قبلاً تایید شده است"));
    }
}