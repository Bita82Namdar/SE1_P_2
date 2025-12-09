package com.university.library.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.university.library.model.*;
import com.university.library.service.*;
import java.util.*;

class ReportControllerTest {
    
    private ReportController reportController;
    private StudentService studentService;
    private BookService bookService;
    private LoanService loanService;
    
    @BeforeEach
    void setUp() {
        studentService = Mockito.mock(StudentService.class);
        bookService = Mockito.mock(BookService.class);
        loanService = Mockito.mock(LoanService.class);
        
        // استفاده از constructor پیش‌فرض
        reportController = new ReportController();
    }
    
    // تست‌های اصلی
    
    @Test
    void testGetRegisteredStudentsCount() {
        // Arrange
        when(studentService.getTotalStudentsCount()).thenReturn(100);
        
        // Act
        int result = reportController.getRegisteredStudentsCount();
        
        // Assert
        assertEquals(100, result);
    }
    
    @Test
    void testGetTotalBooksCount() {
        // Arrange
        when(bookService.getTotalBooksCount()).thenReturn(500);
        
        // Act
        int result = reportController.getTotalBooksCount();
        
        // Assert
        assertEquals(500, result);
    }
    
    @Test
    void testGetTotalLoansCount() {
        // Arrange
        List<Loan> mockLoans = Arrays.asList(
            Mockito.mock(Loan.class),
            Mockito.mock(Loan.class)
        );
        when(loanService.getAllLoans()).thenReturn(mockLoans);
        
        // Act
        int result = reportController.getTotalLoansCount();
        
        // Assert
        assertEquals(2, result);
    }
    
    @Test
    void testGetCurrentLoansCount() {
        // Arrange
        when(bookService.getBorrowedBooksCount()).thenReturn(50);
        
        // Act
        int result = reportController.getCurrentLoansCount();
        
        // Assert
        assertEquals(50, result);
    }
    
    @Test
    void testIsStudentActive() {
        // Arrange
        String studentId = "ST001";
        when(studentService.isStudentActive(studentId)).thenReturn(true);
        
        // Act
        boolean result = reportController.isStudentActive(studentId);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    void testGetStudentReport() {
        // Arrange
        String studentId = "ST001";
        Student mockStudent = Mockito.mock(Student.class);
        when(studentService.getStudentById(studentId)).thenReturn(mockStudent);
        
        List<Loan> mockLoans = Arrays.asList(
            createMockLoan(true, false, true),   // تأخیر دارد
            createMockLoan(true, false, false),  // بدون تأخیر
            createMockLoan(true, true, false)    // بازگردانده شده
        );
        when(loanService.getLoansByStudent(studentId)).thenReturn(mockLoans);
        
        // Act
        StudentReport result = reportController.getStudentReport(studentId);
        
        // Assert
        assertNotNull(result);
        assertEquals(3, result.getTotalLoans());
        assertEquals(2, result.getNotReturnedCount()); // 2 تا تحویل داده نشده
        assertEquals(1, result.getDelayedLoansCount()); // 1 تا تأخیر دارد
    }
    
    @Test
    void testGetStudentReportThrowsExceptionWhenStudentNotFound() {
        // Arrange
        String studentId = "UNKNOWN";
        when(studentService.getStudentById(studentId)).thenReturn(null);
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            reportController.getStudentReport(studentId);
        });
    }
    
    @Test
    void testGetTop10DelayedStudents() {
        // Arrange
        List<String> studentIds = Arrays.asList("ST001", "ST002", "ST003");
        when(studentService.getAllStudentIds()).thenReturn(studentIds);
        
        // دانشجوی ۱: تأخیر دارد
        when(studentService.getStudentById("ST001")).thenReturn(Mockito.mock(Student.class));
        List<Loan> loans1 = Arrays.asList(
            createMockLoan(true, false, true)  // تأخیر دارد
        );
        when(loanService.getLoansByStudent("ST001")).thenReturn(loans1);
        
        // دانشجوی ۲: بدون تأخیر
        when(studentService.getStudentById("ST002")).thenReturn(Mockito.mock(Student.class));
        List<Loan> loans2 = Arrays.asList(
            createMockLoan(true, false, false) // بدون تأخیر
        );
        when(loanService.getLoansByStudent("ST002")).thenReturn(loans2);
        
        // دانشجوی ۳: بدون امانت
        when(studentService.getStudentById("ST003")).thenReturn(Mockito.mock(Student.class));
        when(loanService.getLoansByStudent("ST003")).thenReturn(new ArrayList<>());
        
        // Act
        List<StudentDelayReport> result = reportController.getTop10DelayedStudents();
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size()); // فقط ST001 باید در لیست باشد
        assertEquals("ST001", result.get(0).getStudentId());
        assertEquals(1, result.get(0).getDelayCount());
        assertEquals(1, result.get(0).getNotReturnedCount());
    }
    
    @Test
    void testGetLibraryStatistics() {
        // Arrange
        List<Loan> mockLoans = new ArrayList<>();
        
        // امانت کامل شده (۱۰ روز)
        Loan returnedLoan = Mockito.mock(Loan.class);
        when(returnedLoan.isReturned()).thenReturn(true);
        when(returnedLoan.getActualReturnDate()).thenReturn(java.time.LocalDate.of(2024, 1, 10));
        when(returnedLoan.getStartDate()).thenReturn(java.time.LocalDate.of(2024, 1, 1));
        mockLoans.add(returnedLoan);
        
        // امانت فعال
        Loan activeLoan = Mockito.mock(Loan.class);
        when(activeLoan.isReturned()).thenReturn(false);
        mockLoans.add(activeLoan);
        
        when(loanService.getAllLoans()).thenReturn(mockLoans);
        when(studentService.getTotalStudentsCount()).thenReturn(150);
        when(bookService.getTotalBooksCount()).thenReturn(500);
        
        // Act
        LibraryStats result = reportController.getLibraryStatistics();
        
        // Assert
        assertNotNull(result);
        assertEquals(150, result.getTotalStudents());
        assertEquals(500, result.getTotalBooks());
        assertEquals(2, result.getTotalLoans());
        assertTrue(result.getAverageLoanDays() >= 0);
    }
    
    @Test
    void testGetTotalBorrowRequests() {
        // Arrange
        List<Loan> mockLoans = Arrays.asList(
            Mockito.mock(Loan.class),
            Mockito.mock(Loan.class),
            Mockito.mock(Loan.class)
        );
        when(loanService.getAllLoans()).thenReturn(mockLoans);
        
        // Act
        int result = reportController.getTotalBorrowRequests();
        
        // Assert
        assertEquals(3, result);
    }
    
    @Test
    void testGetTotalApprovedLoans() {
        // Arrange
        Loan approved1 = Mockito.mock(Loan.class);
        when(approved1.isApproved()).thenReturn(true);
        
        Loan approved2 = Mockito.mock(Loan.class);
        when(approved2.isBorrowed()).thenReturn(true);
        
        Loan pending = Mockito.mock(Loan.class);
        when(pending.isApproved()).thenReturn(false);
        when(pending.isBorrowed()).thenReturn(false);
        
        List<Loan> mockLoans = Arrays.asList(approved1, approved2, pending);
        when(loanService.getAllLoans()).thenReturn(mockLoans);
        
        // Act
        int result = reportController.getTotalApprovedLoans();
        
        // Assert
        assertEquals(2, result); // 2 تا تأیید شده
    }
    
    @Test
    void testGetAvailableBooks() {
        // Arrange
        List<Book> mockBooks = Arrays.asList(
            Mockito.mock(Book.class),
            Mockito.mock(Book.class)
        );
        when(bookService.getAvailableBooks()).thenReturn(mockBooks);
        
        // Act
        List<Book> result = reportController.getAvailableBooks();
        
        // Assert
        assertEquals(2, result.size());
    }
    
    @Test
    void testGetBorrowedBooks() {
        // Arrange
        List<Book> mockBooks = Collections.singletonList(Mockito.mock(Book.class));
        when(bookService.getBorrowedBooks()).thenReturn(mockBooks);
        
        // Act
        List<Book> result = reportController.getBorrowedBooks();
        
        // Assert
        assertEquals(1, result.size());
    }
    
    @Test
    void testGetActiveStudents() {
        // Arrange
        List<Student> mockStudents = Arrays.asList(
            Mockito.mock(Student.class),
            Mockito.mock(Student.class)
        );
        when(studentService.getActiveStudents()).thenReturn(mockStudents);
        
        // Act
        List<Student> result = reportController.getActiveStudents();
        
        // Assert
        assertEquals(2, result.size());
    }
    
    @Test
    void testGetInactiveStudents() {
        // Arrange
        List<Student> mockStudents = Collections.singletonList(Mockito.mock(Student.class));
        when(studentService.getInactiveStudents()).thenReturn(mockStudents);
        
        // Act
        List<Student> result = reportController.getInactiveStudents();
        
        // Assert
        assertEquals(1, result.size());
    }
    
    // متد کمکی برای ایجاد Loan تستی
    private Loan createMockLoan(boolean isBorrowed, boolean isReturned, boolean isOverdue) {
        Loan loan = Mockito.mock(Loan.class);
        
        when(loan.isBorrowed()).thenReturn(isBorrowed);
        when(loan.isReturned()).thenReturn(isReturned);
        
        if (isOverdue) {
            when(loan.getEndDate()).thenReturn(java.time.LocalDate.now().minusDays(1));
        } else {
            when(loan.getEndDate()).thenReturn(java.time.LocalDate.now().plusDays(1));
        }
        
        return loan;
    }
}