package com.university.library.service;

import com.university.library.model.User;
import com.university.library.model.Student;
import com.university.library.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

public class AuthenticationServiceTest {
    private AuthenticationService authService;
    private UserRepository userRepository;
    
    @BeforeEach
    void setUp() {
        userRepository = UserRepository.getInstance();
        userRepository.clear(); // حالا این متد وجود دارد
        authService = new AuthenticationService();
    }
    
    @Test
    void testRegisterStudent_Success() {
        boolean result = authService.registerStudent("newstudent", "password", "ST003", "New Student", "new@university.com");
        assertTrue(result, "Student registration should be successful");
    }
    
    @Test
    void testRegisterStudent_DuplicateUsername() {
        // ثبت کاربر اول
        boolean firstRegistration = authService.registerStudent("testuser", "password", "ST001", "Test User", "test@university.com");
        assertTrue(firstRegistration, "First registration should be successful");
        
        // تلاش برای ثبت کاربر دوم با همان نام کاربری
        boolean secondRegistration = authService.registerStudent("testuser", "password2", "ST002", "Test User2", "test2@university.com");
        assertFalse(secondRegistration, "Second registration with same username should fail");
    }
    
    @Test
    void testRegisterStudent_NullUsername() {
        boolean result = authService.registerStudent(null, "password", "ST003", "New Student", "new@university.com");
        assertFalse(result, "Registration with null username should fail");
    }
    
    @Test
    void testRegisterStudent_NullPassword() {
        boolean result = authService.registerStudent("newuser", null, "ST003", "New Student", "new@university.com");
        assertFalse(result, "Registration with null password should fail");
    }
    
    @Test
    void testLogin_Success() {
        // اول کاربر را ثبت‌نام می‌کنیم
        authService.registerStudent("loginuser", "mypassword", "ST004", "Login User", "login@university.com");
        
        // سپس لاگین می‌کنیم
        Optional<User> result = authService.login("loginuser", "mypassword");
        assertTrue(result.isPresent(), "Login should be successful");
        assertEquals("loginuser", result.get().getUsername(), "Username should match");
    }
    
    @Test
    void testLogin_WrongPassword() {
        authService.registerStudent("user1", "password", "ST005", "User One", "user1@university.com");
        Optional<User> result = authService.login("user1", "wrongpassword");
        assertFalse(result.isPresent(), "Login with wrong password should fail");
    }
    
    @Test
    void testLogin_NonExistentUser() {
        Optional<User> result = authService.login("nonexistent", "password");
        assertFalse(result.isPresent(), "Login with non-existent user should fail");
    }
    
    @Test
    void testLogin_NullCredentials() {
        Optional<User> result1 = authService.login(null, "password");
        assertFalse(result1.isPresent(), "Login with null username should fail");
        
        Optional<User> result2 = authService.login("user", null);
        assertFalse(result2.isPresent(), "Login with null password should fail");
    }
    
    @Test
    void testChangePassword_Success() {
        // ثبت کاربر و تغییر رمز عبور
        authService.registerStudent("changepass", "oldpass", "ST006", "Change Pass", "change@university.com");
        boolean result = authService.changePassword("changepass", "oldpass", "newpass");
        assertTrue(result, "Password change should be successful");
        
        // بررسی می‌کنیم که با رمز جدید می‌توان لاگین کرد
        Optional<User> user = authService.login("changepass", "newpass");
        assertTrue(user.isPresent(), "Login with new password should be successful");
    }
    
    @Test
    void testChangePassword_WrongOldPassword() {
        authService.registerStudent("user2", "password", "ST007", "User Two", "user2@university.com");
        boolean result = authService.changePassword("user2", "wrongold", "newpass");
        assertFalse(result, "Password change with wrong old password should fail");
    }
    
    @Test
    void testChangePassword_NonExistentUser() {
        boolean result = authService.changePassword("nonexistent", "oldpass", "newpass");
        assertFalse(result, "Password change for non-existent user should fail");
    }
    
    @Test
    void testUserIsActiveAfterRegistration() {
        authService.registerStudent("activeuser", "password", "ST008", "Active User", "active@university.com");
        Optional<User> user = authService.login("activeuser", "password");
        
        assertTrue(user.isPresent(), "User should be able to login");
        assertTrue(user.get().isActive(), "Newly registered user should be active");
    }
}