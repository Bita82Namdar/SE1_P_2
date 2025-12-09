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
        assertNotNull(report);
        assertEquals(3, report.getTotalLoans()); // 3 امانت
        assertEquals(2, report.getNotReturnedCount()); // 2 کتاب تحویل داده نشده
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
        assertNotNull(stats);
        assertEquals(100, stats.getTotalStudents());
        assertEquals(500, stats.getTotalBooks());
        assertEquals(2, stats.getTotalLoans());
        assertEquals(10.0, stats.getAverageLoanDays(), 0.01); // میانگین 10 روز
    }
    
    private List<Loan> createMockLoans(String studentId) {
        List<Loan> loans = new ArrayList<>();
        
        // استفاده از Mockito برای ایجاد Loan‌ها
        // امانت کامل شده
        Loan loan1 = Mockito.mock(Loan.class);
        Mockito.when(loan1.getStudentId()).thenReturn(studentId);
        Mockito.when(loan1.isBorrowed()).thenReturn(true);
        Mockito.when(loan1.isReturned()).thenReturn(true);
        Mockito.when(loan1.getActualReturnDate()).thenReturn(LocalDate.now().minusDays(5));
        Mockito.when(loan1.getStartDate()).thenReturn(LocalDate.now().minusDays(15));
        Mockito.when(loan1.getEndDate()).thenReturn(LocalDate.now().minusDays(5));
        
        // امانت با تاخیر
        Loan loan2 = Mockito.mock(Loan.class);
        Mockito.when(loan2.getStudentId()).thenReturn(studentId);
        Mockito.when(loan2.isBorrowed()).thenReturn(true);
        Mockito.when(loan2.isReturned()).thenReturn(false);
        Mockito.when(loan2.getEndDate()).thenReturn(LocalDate.now().minusDays(2));
        
        // امانت تحویل داده نشده (بدون تاخیر)
        Loan loan3 = Mockito.mock(Loan.class);
        Mockito.when(loan3.getStudentId()).thenReturn(studentId);
        Mockito.when(loan3.isBorrowed()).thenReturn(true);
        Mockito.when(loan3.isReturned()).thenReturn(false);
        Mockito.when(loan3.getEndDate()).thenReturn(LocalDate.now().plusDays(5));
        
        loans.add(loan1);
        loans.add(loan2);
        loans.add(loan3);
        return loans;
    }
    
    private List<Loan> createMockLoansForStats() {
        List<Loan> loans = new ArrayList<>();
        
        // امانت ۱: ۱۰ روز
        Loan loan1 = Mockito.mock(Loan.class);
        Mockito.when(loan1.isReturned()).thenReturn(true);
        Mockito.when(loan1.getActualReturnDate()).thenReturn(LocalDate.now().minusDays(5));
        Mockito.when(loan1.getStartDate()).thenReturn(LocalDate.now().minusDays(15));
        
        // امانت ۲: ۱۰ روز
        Loan loan2 = Mockito.mock(Loan.class);
        Mockito.when(loan2.isReturned()).thenReturn(true);
        Mockito.when(loan2.getActualReturnDate()).thenReturn(LocalDate.now().minusDays(10));
        Mockito.when(loan2.getStartDate()).thenReturn(LocalDate.now().minusDays(20));
        
        loans.add(loan1);
        loans.add(loan2);
        return loans;
    }
    
    @Test
    void testGenerateStudentReportWithNoLoans() {
        // Arrange
        String studentId = "ST002";
        Mockito.when(loanRepository.findByStudentId(studentId)).thenReturn(new ArrayList<>());
        
        // Act
        StudentReport report = loanService.generateStudentReport(studentId);
        
        // Assert
        assertNotNull(report);
        assertEquals(0, report.getTotalLoans());
        assertEquals(0, report.getNotReturnedCount());
        assertEquals(0, report.getDelayedLoansCount());
    }
    
    @Test
    void testGenerateLibraryStatsWithNoLoans() {
        // Arrange
        Mockito.when(loanRepository.findAll()).thenReturn(new ArrayList<>());
        Mockito.when(studentService.getTotalStudentsCount()).thenReturn(50);
        Mockito.when(bookService.getTotalBooksCount()).thenReturn(200);
        
        // Act
        LibraryStats stats = loanService.generateLibraryStats();
        
        // Assert
        assertNotNull(stats);
        assertEquals(50, stats.getTotalStudents());
        assertEquals(200, stats.getTotalBooks());
        assertEquals(0, stats.getTotalLoans());
        assertEquals(0.0, stats.getAverageLoanDays(), 0.01);
    }
    
    @Test
    void testGetTop10StudentsWithMostDelays() {
        // Arrange
        List<String> studentIds = Arrays.asList("ST001", "ST002", "ST003");
        Mockito.when(studentService.getAllStudentIds()).thenReturn(studentIds);
        
        // دانشجوی ۱: ۲ امانت با تأخیر
        List<Loan> loans1 = Arrays.asList(
            createMockLoanWithDelay("ST001", true),
            createMockLoanWithDelay("ST001", true),
            createMockLoanWithDelay("ST001", false)
        );
        Mockito.when(loanRepository.findByStudentId("ST001")).thenReturn(loans1);
        
        // دانشجوی ۲: ۱ امانت با تأخیر
        List<Loan> loans2 = Arrays.asList(
            createMockLoanWithDelay("ST002", true),
            createMockLoanWithDelay("ST002", false)
        );
        Mockito.when(loanRepository.findByStudentId("ST002")).thenReturn(loans2);
        
        // دانشجوی ۳: بدون تأخیر
        List<Loan> loans3 = Arrays.asList(
            createMockLoanWithDelay("ST003", false)
        );
        Mockito.when(loanRepository.findByStudentId("ST003")).thenReturn(loans3);
        
        // Act
        List<StudentDelayReport> result = loanService.getTop10StudentsWithMostDelays();
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size()); // فقط ST001 و ST002
        assertEquals("ST001", result.get(0).getStudentId()); // بیشترین تأخیر
        assertEquals(2, result.get(0).getDelayCount());
        assertEquals("ST002", result.get(1).getStudentId());
        assertEquals(1, result.get(1).getDelayCount());
    }
    
    private Loan createMockLoanWithDelay(String studentId, boolean isDelayed) {
        Loan loan = Mockito.mock(Loan.class);
        Mockito.when(loan.getStudentId()).thenReturn(studentId);
        Mockito.when(loan.isBorrowed()).thenReturn(true);
        Mockito.when(loan.isReturned()).thenReturn(false);
        
        if (isDelayed) {
            Mockito.when(loan.getEndDate()).thenReturn(LocalDate.now().minusDays(1));
        } else {
            Mockito.when(loan.getEndDate()).thenReturn(LocalDate.now().plusDays(1));
        }
        
        return loan;
    }
}