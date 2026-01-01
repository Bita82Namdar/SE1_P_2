package com.university.library.controller;

import org.springframework.stereotype.Service;
import com.university.library.dto.StudentRegistrationRequest;

@Service
public class AuthenticationController {

    // امضای فعلی شما (مثال)
    public boolean register(String username, String password, String firstName,
                            String lastName, String email, String studentId) {
        // منطق ثبت‌نام واقعی شما اینجاست
        // ...
        return true;
    }

    // متد جدید برای پذیرش DTO و فراخوانی متد اصلی
    public boolean register(StudentRegistrationRequest request) {
        return register(
            request.getUsername(),
            request.getPassword(),
            request.getFirstName(),
            request.getLastName(),
            request.getEmail(),
            request.getStudentId()
        );
    }

    // برای سازگاری با کنترلر شما
    public boolean login(String username, String password) {
        // منطق لاگین
        // ...
        return true;
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        // منطق تغییر رمز
        // ...
        return true;
    }
}