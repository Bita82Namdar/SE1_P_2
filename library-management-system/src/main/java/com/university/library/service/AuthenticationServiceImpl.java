package com.university.library.service;

public class AuthenticationServiceImpl implements AuthenticationService {
    
    @Override
    public boolean register(String username, String password, String firstName, 
                           String lastName, String email, String studentId) {
        // پیاده‌سازی ساده
        System.out.println("ثبت‌نام کاربر: " + username);
        return true;
    }
    
    @Override
    public boolean login(String username, String password) {
        // پیاده‌سازی ساده
        System.out.println("ورود کاربر: " + username);
        return username.equals("admin") && password.equals("admin123");
    }
    
    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        // پیاده‌سازی ساده
        System.out.println("تغییر رمز کاربر: " + username);
        return true;
    }
}