package com.university.library.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.university.library.model.*;
import com.university.library.repository.*;
import java.time.LocalDate;
import java.util.*;

class LoanStatisticsTest {
    
    private LoanService loanService;
    private LoanRepository loanRepository;
    private StudentService studentService;
    private BookService bookService;
    
    @BeforeEach
    void setUp() {
        loanRepository = Mockito.mock(LoanRepository.class);
        studentService = Mockito.mock(StudentService.class);
        bookService = Mockito.mock(BookService.class);
        
        loanService = new LoanService(loanRepository, 
            Mockito.mock(BookRepository.class),
            Mockito.mock(UserRepository.class),
            studentService,
            bookService);
    }
    
    @Test
    void testGenerateStudentReport() {
        // Arrange
        String studentId = "ST001";
        List<Loan> mockLoans = createMockLoans(studentId);
        Mockito.when(loanRepository.findByStudentId(studentId)).thenReturn(mockLoans);
        
        // Act
        StudentReport report = loanService.generateStudentReport(studentId);
        
        // Assert
        assertEquals(5, report.getTotalLoans()); // 5 امانت
        assertEquals(1, report.getNotReturnedCount()); // 1 کتاب تحویل داده نشده
        assertEquals(1, report.getDelayedLoansCount()); // 1 امانت با تاخیر
    }
    
    @Test
    void testGenerateLibraryStats() {
        // Arrange
        List<Loan> mockLoans = createMockLoansForStats();
        Mockito.when(loanRepository.findAll()).thenReturn(mockLoans);
        Mockito.when(studentService.getTotalStudentsCount()).thenReturn(100);
        Mockito.when(bookService.getTotalBooksCount()).thenReturn(500);
        
        // Act
        LibraryStats stats = loanService.generateLibraryStats();
        
        // Assert
        assertEquals(100, stats.getTotalStudents());
        assertEquals(500, stats.getTotalBooks());
        assertEquals(3, stats.getTotalLoans());
        assertEquals(10.0, stats.getAverageLoanDays(), 0.01); // میانگین 10 روز
    }
    
    private List<Loan> createMockLoans(String studentId) {
        // ایجاد امانت‌های تستی
        List<Loan> loans = new ArrayList<>();
        
        // امانت کامل شده
        Loan loan1 = new Loan("L1", studentId, "B1", 
            LocalDate.now().minusDays(15), LocalDate.now().minusDays(5));
        loan1.markAsBorrowed();
        loan1.returnBook(LocalDate.now().minusDays(5));
        
        // امانت با تاخیر
        Loan loan2 = new Loan("L2", studentId, "B2", 
            LocalDate.now().minusDays(10), LocalDate.now().minusDays(2));
        loan2.markAsBorrowed();
        
        // امانت تحویل داده نشده (بدون تاخیر)
        Loan loan3 = new Loan("L3", studentId, "B3", 
            LocalDate.now().minusDays(5), LocalDate.now().plusDays(5));
        loan3.markAsBorrowed();
        
        // امانت‌های دیگر...
        
        loans.add(loan1);
        loans.add(loan2);
        loans.add(loan3);
        return loans;
    }
    
    private List<Loan> createMockLoansForStats() {
        // ایجاد امانت‌ها برای آمار کلی
        List<Loan> loans = new ArrayList<>();
        
        Loan loan1 = new Loan("L1", "ST1", "B1", 
            LocalDate.now().minusDays(15), LocalDate.now().minusDays(5));
        loan1.markAsBorrowed();
        loan1.returnBook(LocalDate.now().minusDays(5)); // 10 روز
        
        Loan loan2 = new Loan("L2", "ST2", "B2", 
            LocalDate.now().minusDays(20), LocalDate.now().minusDays(10));
        loan2.markAsBorrowed();
        loan2.returnBook(LocalDate.now().minusDays(10)); // 10 روز
        
        loans.add(loan1);
        loans.add(loan2);
        return loans;
    }
}