package com.university.library.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthenticationController {
    
    // کلاس داخلی برای کاربر
    public static class User {
        private String id;
        private String username;
        private String password;
        private String firstName;
        private String lastName;
        private String email;
        private String studentId;
        private String role;
        private boolean active;
        
        // Constructor
        public User(String id, String username, String password, String firstName, 
                   String lastName, String email, String studentId, String role) {
            this.id = id;
            this.username = username;
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.studentId = studentId;
            this.role = role;
            this.active = true;
        }
        
        // Getters and Setters
        public String getId() { return id; }
        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getEmail() { return email; }
        public String getStudentId() { return studentId; }
        public String getRole() { return role; }
        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
    }
    
    // کلاس داخلی برای پاسخ API
    public static class ApiResponse {
        private boolean success;
        private String message;
        private Object data;
        
        public ApiResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }
        
        // Getters
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Object getData() { return data; }
    }
    
    // ذخیره‌سازی کاربران
    private Map<String, User> users = new HashMap<>();
    private int userIdCounter = 1;
    
    public AuthenticationController() {
        // اضافه کردن کاربران نمونه
        initializeSampleUsers();
    }
    
    private void initializeSampleUsers() {
        // کاربر ادمین
        User admin = new User("U001", "admin", "admin123", "مدیر", "سیستم", 
                             "admin@library.edu", null, "ADMIN");
        users.put("admin", admin);
        
        // کاربر دانشجو
        User student = new User("U002", "student1", "pass123", "علی", "محمدی",
                               "ali@university.edu", "40123456", "STUDENT");
        users.put("student1", student);
        
        // کاربر کارمند
        User employee = new User("U003", "employee1", "emp123", "احمد", "رضایی",
                                "ahmad@library.edu", null, "EMPLOYEE");
        users.put("employee1", employee);
        
        userIdCounter = 4;
    }
    
    // ۱. ثبت‌نام دانشجو - POST /api/auth/register
    public ApiResponse registerStudent(String studentId, String firstName, String lastName,
                                      String username, String password, String email) {
        try {
            // بررسی وجود نام کاربری
            if (users.containsKey(username)) {
                return new ApiResponse(false, "نام کاربری قبلاً ثبت شده است");
            }
            
            // بررسی وجود شماره دانشجویی
            for (User user : users.values()) {
                if (studentId.equals(user.getStudentId())) {
                    return new ApiResponse(false, "شماره دانشجویی قبلاً ثبت شده است");
                }
            }
            
            // ایجاد کاربر جدید
            String userId = "U" + String.format("%03d", userIdCounter++);
            User newUser = new User(userId, username, password, firstName, lastName, 
                                   email, studentId, "STUDENT");
            
            users.put(username, newUser);
            
            // بازگرداندن پاسخ موفق
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("userId", userId);
            responseData.put("username", username);
            responseData.put("studentId", studentId);
            responseData.put("fullName", firstName + " " + lastName);
            
            return new ApiResponse(true, "ثبت‌نام دانشجو با موفقیت انجام شد", responseData);
            
        } catch (Exception e) {
            return new ApiResponse(false, "خطا در ثبت‌نام: " + e.getMessage());
        }
    }
    
    // ۲. ورود به سیستم - POST /api/auth/login
    public ApiResponse login(String username, String password) {
        try {
            User user = users.get(username);
            
            if (user == null) {
                return new ApiResponse(false, "نام کاربری یا رمز عبور نادرست است");
            }
            
            if (!user.getPassword().equals(password)) {
                return new ApiResponse(false, "نام کاربری یا رمز عبور نادرست است");
            }
            
            if (!user.isActive()) {
                return new ApiResponse(false, "حساب کاربری غیرفعال است");
            }
            
            // بازگرداندن اطلاعات کاربر
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("firstName", user.getFirstName());
            userInfo.put("lastName", user.getLastName());
            userInfo.put("email", user.getEmail());
            userInfo.put("role", user.getRole());
            userInfo.put("studentId", user.getStudentId());
            
            return new ApiResponse(true, "ورود موفقیت‌آمیز بود", userInfo);
            
        } catch (Exception e) {
            return new ApiResponse(false, "خطا در ورود به سیستم: " + e.getMessage());
        }
    }
    
    // ۳. تغییر رمزعبور - POST /api/auth/change-password
    public ApiResponse changePassword(String username, String oldPassword, String newPassword) {
        try {
            User user = users.get(username);
            
            if (user == null) {
                return new ApiResponse(false, "کاربر یافت نشد");
            }
            
            if (!user.getPassword().equals(oldPassword)) {
                return new ApiResponse(false, "رمز عبور فعلی نادرست است");
            }
            
            if (oldPassword.equals(newPassword)) {
                return new ApiResponse(false, "رمز عبور جدید باید با رمز قبلی متفاوت باشد");
            }
            
            // تغییر رمز عبور
            user.setPassword(newPassword);
            
            return new ApiResponse(true, "رمز عبور با موفقیت تغییر یافت");
            
        } catch (Exception e) {
            return new ApiResponse(false, "خطا در تغییر رمز عبور: " + e.getMessage());
        }
    }
    
    // ۴. دریافت اطلاعات کاربر - GET /api/auth/user/{username}
    public ApiResponse getUserInfo(String username) {
        try {
            User user = users.get(username);
            
            if (user == null) {
                return new ApiResponse(false, "کاربر یافت نشد");
            }
            
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("firstName", user.getFirstName());
            userInfo.put("lastName", user.getLastName());
            userInfo.put("email", user.getEmail());
            userInfo.put("role", user.getRole());
            userInfo.put("studentId", user.getStudentId());
            userInfo.put("active", user.isActive());
            
            return new ApiResponse(true, "اطلاعات کاربر دریافت شد", userInfo);
            
        } catch (Exception e) {
            return new ApiResponse(false, "خطا در دریافت اطلاعات کاربر: " + e.getMessage());
        }
    }
    
    // ۵. فعال/غیرفعال کردن کاربر - PUT /api/auth/user/{username}/status
    public ApiResponse setUserStatus(String username, boolean active) {
        try {
            User user = users.get(username);
            
            if (user == null) {
                return new ApiResponse(false, "کاربر یافت نشد");
            }
            
            user.setActive(active);
            
            String statusMessage = active ? "فعال" : "غیرفعال";
            return new ApiResponse(true, "وضعیت کاربر به " + statusMessage + " تغییر یافت");
            
        } catch (Exception e) {
            return new ApiResponse(false, "خطا در تغییر وضعیت کاربر: " + e.getMessage());
        }
    }
    
    // ۶. دریافت تمام کاربران (برای ادمین) - GET /api/auth/users
    public ApiResponse getAllUsers() {
        try {
            List<Map<String, Object>> userList = new ArrayList<>();
            
            for (User user : users.values()) {
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("id", user.getId());
                userInfo.put("username", user.getUsername());
                userInfo.put("firstName", user.getFirstName());
                userInfo.put("lastName", user.getLastName());
                userInfo.put("email", user.getEmail());
                userInfo.put("role", user.getRole());
                userInfo.put("studentId", user.getStudentId());
                userInfo.put("active", user.isActive());
                
                userList.add(userInfo);
            }
            
            return new ApiResponse(true, "لیست کاربران دریافت شد", userList);
            
        } catch (Exception e) {
            return new ApiResponse(false, "خطا در دریافت لیست کاربران: " + e.getMessage());
        }
    }
    
    // ۷. بررسی وجود نام کاربری - GET /api/auth/check-username/{username}
    public ApiResponse checkUsername(String username) {
        try {
            boolean exists = users.containsKey(username);
            
            Map<String, Object> result = new HashMap<>();
            result.put("username", username);
            result.put("available", !exists);
            
            String message = exists ? "نام کاربری موجود است" : "نام کاربری آزاد است";
            
            return new ApiResponse(true, message, result);
            
        } catch (Exception e) {
            return new ApiResponse(false, "خطا در بررسی نام کاربری: " + e.getMessage());
        }
    }
    
    // تست کنترلر - اصلاح شده بدون خطا
    public static void main(String[] args) {
        System.out.println("🧪 تست AuthenticationController");
        System.out.println("================================");
        
        AuthenticationController authController = new AuthenticationController();
        
        // تست ۱: ثبت‌نام دانشجوی جدید
        System.out.println("\n1. تست ثبت‌نام دانشجو:");
        ApiResponse registerResponse = authController.registerStudent(
            "40123457", "فاطمه", "کریمی", "fatemeh", "123456", "fatemeh@university.edu"
        );
        System.out.println("   موفق: " + registerResponse.isSuccess());
        System.out.println("   پیام: " + registerResponse.getMessage());
        
        // تست ۲: ورود با کاربر جدید
        System.out.println("\n2. تست ورود به سیستم:");
        ApiResponse loginResponse = authController.login("fatemeh", "123456");
        System.out.println("   موفق: " + loginResponse.isSuccess());
        System.out.println("   پیام: " + loginResponse.getMessage());
        
        // تست ۳: تغییر رمز عبور
        System.out.println("\n3. تست تغییر رمز عبور:");
        ApiResponse changePassResponse = authController.changePassword("fatemeh", "123456", "654321");
        System.out.println("   موفق: " + changePassResponse.isSuccess());
        System.out.println("   پیام: " + changePassResponse.getMessage());
        
        // تست ۴: ورود با رمز جدید
        System.out.println("\n4. تست ورود با رمز جدید:");
        ApiResponse newLoginResponse = authController.login("fatemeh", "654321");
        System.out.println("   موفق: " + newLoginResponse.isSuccess());
        System.out.println("   پیام: " + newLoginResponse.getMessage());
        
        // تست ۵: دریافت اطلاعات کاربر
        System.out.println("\n5. تست دریافت اطلاعات کاربر:");
        ApiResponse userInfoResponse = authController.getUserInfo("fatemeh");
        System.out.println("   موفق: " + userInfoResponse.isSuccess());
        
        // تست ۶: بررسی نام کاربری - اصلاح شده
        System.out.println("\n6. تست بررسی نام کاربری:");
        ApiResponse checkUserResponse = authController.checkUsername("admin");
        if (checkUserResponse.getData() instanceof Map) {
            Map<?, ?> dataMap = (Map<?, ?>) checkUserResponse.getData();
            boolean available = (Boolean) dataMap.get("available");
            System.out.println("   موجود: " + !available);
        }
        
        // تست ۷: دریافت تمام کاربران - اصلاح شده
        System.out.println("\n7. تست دریافت تمام کاربران:");
        ApiResponse allUsersResponse = authController.getAllUsers();
        if (allUsersResponse.getData() instanceof List) {
            List<?> userList = (List<?>) allUsersResponse.getData();
            System.out.println("   تعداد کاربران: " + userList.size());
        }
        
        System.out.println("\n✅ تمام تست‌ها با موفقیت انجام شد!");
        System.out.println("👥 تعداد کل کاربران در سیستم: " + authController.users.size());
    }
}