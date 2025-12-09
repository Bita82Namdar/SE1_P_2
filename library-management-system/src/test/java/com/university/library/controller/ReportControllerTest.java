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
    private StatisticsService statisticsService;
    private StudentService studentService;
    private BookService bookService;
    private LoanService loanService;
    
    @BeforeEach
    void setUp() {
        statisticsService = Mockito.mock(StatisticsService.class);
        studentService = Mockito.mock(StudentService.class);
        bookService = Mockito.mock(BookService.class);
        loanService = Mockito.mock(LoanService.class);
        
        reportController = new ReportController(
            statisticsService, loanService, studentService, bookService);
    }
    
    @Test
    void testGetStudentReport() {
        // Arrange
        String studentId = "ST001";
        StudentReport mockReport = new StudentReport(5, 1, 1);
        when(studentService.getStudentById(studentId)).thenReturn(new Student());
        when(statisticsService.generateStudentReport(studentId)).thenReturn(mockReport);
        
        // Act
        StudentReport result = reportController.getStudentReport(studentId);
        
        // Assert
        assertNotNull(result);
        assertEquals(5, result.getTotalLoans());
        assertEquals(1, result.getNotReturnedCount());
        assertEquals(1, result.getDelayedLoansCount());
    }
    
    @Test
    void testGetGuestStatistics() {
        // Arrange
        GuestStatistics mockStats = new GuestStatistics(100, 500, 300, 50);
        when(statisticsService.generateGuestStatistics()).thenReturn(mockStats);
        
        // Act
        GuestStatistics result = reportController.getGuestStatistics();
        
        // Assert
        assertNotNull(result);
        assertEquals(100, result.getTotalStudents());
        assertEquals(500, result.getTotalBooks());
    }
    
    @Test
    void testGetLoanStatisticsReport() {
        // Arrange
        LoanStatisticsReport mockReport = new LoanStatisticsReport(
            100, 80, 70, 10, 20, 14.5, new ArrayList<>());
        when(statisticsService.generateLoanStatisticsReport()).thenReturn(mockReport);
        
        // Act
        LoanStatisticsReport result = reportController.getLoanStatisticsReport();
        
        // Assert
        assertNotNull(result);
        assertEquals(100, result.getTotalRequests());
        assertEquals(80, result.getTotalApprovedLoans());
        assertEquals(14.5, result.getAverageLoanDays());
    }
    
    @Test
    void testGetTop10DelayedStudents() {
        // Arrange
        List<StudentDelayReport> mockList = Arrays.asList(
            new StudentDelayReport("ST001", 5, 2),
            new StudentDelayReport("ST002", 3, 1)
        );
        when(statisticsService.getTop10StudentsWithMostDelays()).thenReturn(mockList);
        
        // Act
        List<StudentDelayReport> result = reportController.getTop10DelayedStudents();
        
        // Assert
        assertEquals(2, result.size());
        assertEquals("ST001", result.get(0).getStudentId());
        assertEquals(5, result.get(0).getDelayCount());
    }
    
    @Test
    void testGetLibraryStatistics() {
        // Arrange
        LibraryStats mockStats = new LibraryStats(14.5, 100, 500, 300);
        when(statisticsService.generateLibraryStats()).thenReturn(mockStats);
        
        // Act
        LibraryStats result = reportController.getLibraryStatistics();
        
        // Assert
        assertNotNull(result);
        assertEquals(14.5, result.getAverageLoanDays(), 0.01);
        assertEquals(100, result.getTotalStudents());
        assertEquals(500, result.getTotalBooks());
    }
}
