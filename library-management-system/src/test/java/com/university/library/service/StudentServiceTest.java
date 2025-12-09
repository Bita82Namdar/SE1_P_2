package com.university.library.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.university.library.model.Student;
import com.university.library.repository.UserRepository;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    private StudentService studentService;
    
    @BeforeEach
    void setUp() {
        studentService = new StudentService(userRepository);
    }
    
    @Test
    void testIsStudentActive_WhenStudentExistsAndActive() {
        // Arrange
        String studentId = "ST001"; // تغییر از S001 به ST001 (همانطور که در UserRepository استفاده شده)
        Student activeStudent = new Student("U001", "john_doe", "pass123", 
                                           studentId, "John Doe", "john@uni.edu");
        activeStudent.setActive(true);
        
        when(userRepository.findStudentById(studentId))
            .thenReturn(Optional.of(activeStudent));
        
        // Act
        boolean result = studentService.isStudentActive(studentId);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    void testIsStudentActive_WhenStudentExistsAndInactive() {
        // Arrange
        String studentId = "ST002";
        Student inactiveStudent = new Student("U002", "jane_doe", "pass123", 
                                             studentId, "Jane Doe", "jane@uni.edu");
        inactiveStudent.setActive(false);
        
        when(userRepository.findStudentById(studentId))
            .thenReturn(Optional.of(inactiveStudent));
        
        // Act
        boolean result = studentService.isStudentActive(studentId);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    void testIsStudentActive_WhenStudentNotFound() {
        // Arrange
        String studentId = "S999";
        
        when(userRepository.findStudentById(studentId))
            .thenReturn(Optional.empty());
        
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            studentService.isStudentActive(studentId);
        });
        
        assertTrue(exception.getMessage().contains("دانشجو با این شناسه یافت نشد"));
    }
    
    @Test
    void testGetStudentById_WhenStudentExists() {
        // Arrange
        String studentId = "ST001";
        Student expectedStudent = new Student("U001", "john_doe", "pass123", 
                                             studentId, "John Doe", "john@uni.edu");
        
        when(userRepository.findStudentById(studentId))
            .thenReturn(Optional.of(expectedStudent));
        
        // Act
        Student result = studentService.getStudentById(studentId);
        
        // Assert
        assertNotNull(result);
        assertEquals(studentId, result.getStudentId());
        assertEquals("john_doe", result.getUsername());
    }
    
    @Test
    void testGetStudentById_WhenStudentNotFound() {
        // Arrange
        String studentId = "S999";
        
        when(userRepository.findStudentById(studentId))
            .thenReturn(Optional.empty());
        
        // Act
        Student result = studentService.getStudentById(studentId);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    void testGetStudentByIdOrThrow_WhenStudentExists() {
        // Arrange
        String studentId = "ST001";
        Student expectedStudent = new Student("U001", "john_doe", "pass123", 
                                             studentId, "John Doe", "john@uni.edu");
        
        when(userRepository.findStudentById(studentId))
            .thenReturn(Optional.of(expectedStudent));
        
        // Act
        Student result = studentService.getStudentByIdOrThrow(studentId);
        
        // Assert
        assertNotNull(result);
        assertEquals(studentId, result.getStudentId());
    }
    
    @Test
    void testGetStudentByIdOrThrow_WhenStudentNotFound() {
        // Arrange
        String studentId = "S999";
        
        when(userRepository.findStudentById(studentId))
            .thenReturn(Optional.empty());
        
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            studentService.getStudentByIdOrThrow(studentId);
        });
        
        assertTrue(exception.getMessage().contains("دانشجو با این شناسه یافت نشد"));
    }
    
    @Test
    void testActivateStudent_WhenStudentExists() {
        // Arrange
        String studentId = "ST001";
        Student student = new Student("U001", "john_doe", "pass123", 
                                     studentId, "John Doe", "john@uni.edu");
        student.setActive(false);
        
        when(userRepository.findStudentById(studentId))
            .thenReturn(Optional.of(student));
        when(userRepository.updateUser(student)).thenReturn(true);
        
        // Act
        boolean result = studentService.activateStudent(studentId);
        
        // Assert
        assertTrue(result);
        assertTrue(student.isActive());
    }
    
    @Test
    void testDeactivateStudent_WhenStudentExists() {
        // Arrange
        String studentId = "ST001";
        Student student = new Student("U001", "john_doe", "pass123", 
                                     studentId, "John Doe", "john@uni.edu");
        student.setActive(true);
        
        when(userRepository.findStudentById(studentId))
            .thenReturn(Optional.of(student));
        when(userRepository.updateUser(student)).thenReturn(true);
        
        // Act
        boolean result = studentService.deactivateStudent(studentId);
        
        // Assert
        assertTrue(result);
        assertFalse(student.isActive());
    }
}