package com.university.library.service;

import java.util.*;

public class AuthenticationServiceTest {
    
    // کلاس داخلی User
    static class User {
        private String id;
        private String username;
        private String password;
        private String firstName;
        private String lastName;
        private String email;
        private String studentId;
        private boolean active;
        private String role;
        
        public User(String username, String password, String firstName, String lastName, 
                   String email, String studentId, String role) {
            this.username = username;
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.studentId = studentId;
            this.role = role;
            this.active = true;
        }
        
        // Getters
        public String getId() { return id; }
        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getEmail() { return email; }
        public String getStudentId() { return studentId; }
        public boolean isActive() { return active; }
        public String getRole() { return role; }
        
        // Setters
        public void setId(String id) { this.id = id; }
        public void setPassword(String password) { this.password = password; }
        public void setActive(boolean active) { this.active = active; }
        
        @Override
        public String toString() {
            return username + " (" + firstName + " " + lastName + ")";
        }
    }
    
    // کلاس AuthenticationService برای تست
    static class TestAuthenticationService {
        private Map<String, User> users = new HashMap<>();
        private int userCounter = 1;
        
        public TestAuthenticationService() {
            // اضافه کردن کاربران نمونه
            addSampleUsers();
        }
        
        private void addSampleUsers() {
            // کاربر ادمین
            User admin = new User("admin", "admin123", "مدیر", "سیستم", 
                                 "admin@library.edu", null, "ADMIN");
            admin.setId("U001");
            users.put("admin", admin);
            
            // کاربر دانشجو
            User student = new User("student1", "pass123", "علی", "محمدی",
                                   "ali@university.edu", "40123456", "STUDENT");
            student.setId("U002");
            users.put("student1", student);
            
            userCounter = 3;
        }
        
        // ثبت‌نام دانشجو
        public boolean registerStudent(String username, String password, String studentId, 
                                      String fullName, String email) {
            if (username == null || username.trim().isEmpty()) {
                return false;
            }
            
            if (password == null || password.trim().isEmpty()) {
                return false;
            }
            
            if (users.containsKey(username)) {
                return false;
            }
            
            // تقسیم نام کامل به نام و نام خانوادگی
            String[] nameParts = fullName.split(" ");
            String firstName = nameParts.length > 0 ? nameParts[0] : fullName;
            String lastName = nameParts.length > 1 ? nameParts[nameParts.length - 1] : "";
            
            User newUser = new User(username, password, firstName, lastName, email, studentId, "STUDENT");
            newUser.setId("U" + String.format("%03d", userCounter++));
            
            users.put(username, newUser);
            return true;
        }
        
        // ثبت‌نام عمومی
        public boolean register(String username, String password, String firstName, 
                               String lastName, String email, String role) {
            if (username == null || username.trim().isEmpty()) {
                return false;
            }
            
            if (password == null || password.trim().isEmpty()) {
                return false;
            }
            
            if (users.containsKey(username)) {
                return false;
            }
            
            User newUser = new User(username, password, firstName, lastName, email, null, role);
            newUser.setId("U" + String.format("%03d", userCounter++));
            
            users.put(username, newUser);
            return true;
        }
        
        // لاگین
        public Optional<User> login(String username, String password) {
            if (username == null || password == null) {
                return Optional.empty();
            }
            
            User user = users.get(username);
            if (user == null) {
                return Optional.empty();
            }
            
            if (!user.getPassword().equals(password)) {
                return Optional.empty();
            }
            
            if (!user.isActive()) {
                return Optional.empty();
            }
            
            return Optional.of(user);
        }
        
        // تغییر رمز عبور
        public boolean changePassword(String username, String oldPassword, String newPassword) {
            User user = users.get(username);
            if (user == null) {
                return false;
            }
            
            if (!user.getPassword().equals(oldPassword)) {
                return false;
            }
            
            user.setPassword(newPassword);
            return true;
        }
        
        // پاک کردن همه کاربران (برای تست)
        public void clear() {
            users.clear();
            userCounter = 1;
            addSampleUsers();
        }
        
        // بررسی وجود کاربر
        public boolean userExists(String username) {
            return users.containsKey(username);
        }
        
        // دریافت تعداد کاربران
        public int getUserCount() {
            return users.size();
        }
        
        // دریافت کاربر
        public Optional<User> getUser(String username) {
            return Optional.ofNullable(users.get(username));
        }
    }
    
    // تست‌ها
    public static void main(String[] args) {
        System.out.println("🔐 شروع تست‌های AuthenticationService");
        System.out.println("=====================================");
        
        TestAuthenticationService authService = new TestAuthenticationService();
        int passedTests = 0;
        int totalTests = 0;
        
        try {
            // تست 1-1: ثبت‌نام کاربر جدید با نام کاربری منحصربه‌فرد
            totalTests++;
            System.out.print("\n1. سناریو 1-1: ثبت‌نام کاربر جدید منحصربه‌فرد... ");
            testRegisterNewUserWithUniqueUsername_ReturnsTrue(authService);
            System.out.println("✅");
            passedTests++;
            
            // تست 1-2: ثبت‌نام با نام کاربری تکراری
            totalTests++;
            System.out.print("2. سناریو 1-2: ثبت‌نام با نام کاربری تکراری... ");
            testRegisterWithDuplicateUsername_ReturnsFalse(authService);
            System.out.println("✅");
            passedTests++;
            
            // تست 1-3: ورود با نام کاربری و رمز عبور صحیح
            totalTests++;
            System.out.print("3. سناریو 1-3: ورود با اطلاعات صحیح... ");
            testLoginWithCorrectCredentials_ReturnsTrue(authService);
            System.out.println("✅");
            passedTests++;
            
            // تست 1-4: ورود با نام کاربری صحیح اما رمز عبور نادرست
            totalTests++;
            System.out.print("4. سناریو 1-4: ورود با رمز عبور نادرست... ");
            testLoginWithCorrectUsernameWrongPassword_ReturnsFalse(authService);
            System.out.println("✅");
            passedTests++;
            
            // تست 1-5: ورود با نام کاربری که وجود ندارد
            totalTests++;
            System.out.print("5. سناریو 1-5: ورود با کاربری ناموجود... ");
            testLoginWithNonExistentUsername_ReturnsFalse(authService);
            System.out.println("✅");
            passedTests++;
            
            // تست اضافی: ثبت‌نام دانشجو موفق
            totalTests++;
            System.out.print("6. تست ثبت‌نام دانشجو موفق... ");
            testRegisterStudent_Success(authService);
            System.out.println("✅");
            passedTests++;
            
            // تست اضافی: تغییر رمز عبور موفق
            totalTests++;
            System.out.print("7. تست تغییر رمز عبور موفق... ");
            testChangePassword_Success(authService);
            System.out.println("✅");
            passedTests++;
            
            // تست اضافی: تغییر رمز عبور با رمز قدیمی نادرست
            totalTests++;
            System.out.print("8. تست تغییر رمز با رمز قدیمی نادرست... ");
            testChangePassword_WrongOldPassword(authService);
            System.out.println("✅");
            passedTests++;
            
            // تست اضافی: ثبت‌نام با نام کاربری null
            totalTests++;
            System.out.print("9. تست ثبت‌نام با نام کاربری null... ");
            testRegisterStudent_NullUsername(authService);
            System.out.println("✅");
            passedTests++;
            
            // تست اضافی: ثبت‌نام با رمز عبور null
            totalTests++;
            System.out.print("10. تست ثبت‌نام با رمز عبور null... ");
            testRegisterStudent_NullPassword(authService);
            System.out.println("✅");
            passedTests++;
            
            System.out.println("\n📊 نتایج تست:");
            System.out.println("   تست‌های گذرانده شده: " + passedTests + " از " + totalTests);
            System.out.println("   نرخ موفقیت: " + (passedTests * 100 / totalTests) + "%");
            
            if (passedTests == totalTests) {
                System.out.println("\n🎉 تمام تست‌ها با موفقیت گذرانده شدند!");
            } else {
                System.out.println("\n⚠️  برخی تست‌ها ناموفق بودند!");
            }
            
        } catch (Exception e) {
            System.out.println("❌");
            System.out.println("خطا در تست: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // متدهای تست
    private static void testRegisterNewUserWithUniqueUsername_ReturnsTrue(TestAuthenticationService authService) {
        // سناریو 1-1: ثبت‌نام یک کاربر جدید با نام کاربری منحصربه‌فرد
        boolean result = authService.registerStudent("uniqueuser", "password123", "ST009", 
                                                    "Unique User", "unique@university.com");
        if (!result) {
            throw new RuntimeException("ثبت‌نام با نام کاربری منحصربه‌فرد باید true برگرداند");
        }
    }
    
    private static void testRegisterWithDuplicateUsername_ReturnsFalse(TestAuthenticationService authService) {
        // سناریو 1-2: ثبت‌نام با نام کاربری تکراری
        // ثبت اولیه
        boolean firstResult = authService.registerStudent("duplicateuser", "pass1", "ST010", 
                                                         "First User", "first@university.com");
        if (!firstResult) {
            throw new RuntimeException("ثبت اولیه باید موفق باشد");
        }
        
        // تلاش برای ثبت با نام کاربری تکراری
        boolean secondResult = authService.registerStudent("duplicateuser", "pass2", "ST011", 
                                                          "Second User", "second@university.com");
        if (secondResult) {
            throw new RuntimeException("ثبت با نام کاربری تکراری باید false برگرداند");
        }
    }
    
    private static void testLoginWithCorrectCredentials_ReturnsTrue(TestAuthenticationService authService) {
        // سناریو 1-3: ورود با نام کاربری و رمز عبور صحیح
        // ثبت کاربر
        boolean registered = authService.registerStudent("correctuser", "correctpass", "ST012", 
                                                        "Correct User", "correct@university.com");
        if (!registered) {
            throw new RuntimeException("ثبت کاربر برای تست لاگین باید موفق باشد");
        }
        
        // ورود با اطلاعات صحیح
        Optional<User> result = authService.login("correctuser", "correctpass");
        if (!result.isPresent()) {
            throw new RuntimeException("لاگین با اطلاعات صحیح باید موفق باشد");
        }
    }
    
    private static void testLoginWithCorrectUsernameWrongPassword_ReturnsFalse(TestAuthenticationService authService) {
        // سناریو 1-4: ورود با نام کاربری صحیح اما رمز عبور نادرست
        // ثبت کاربر
        boolean registered = authService.registerStudent("user123", "rightpassword", "ST013", 
                                                        "User 123", "user123@university.com");
        if (!registered) {
            throw new RuntimeException("ثبت کاربر برای تست لاگین باید موفق باشد");
        }
        
        // ورود با رمز اشتباه
        Optional<User> result = authService.login("user123", "wrongpassword");
        if (result.isPresent()) {
            throw new RuntimeException("لاگین با رمز اشتباه باید ناموفق باشد");
        }
    }
    
    private static void testLoginWithNonExistentUsername_ReturnsFalse(TestAuthenticationService authService) {
        // سناریو 1-5: ورود با نام کاربری که وجود ندارد
        // ورود با کاربری که اصلاً ثبت‌نام نکرده
        Optional<User> result = authService.login("ghostuser", "anypassword");
        if (result.isPresent()) {
            throw new RuntimeException("لاگین با کاربر ناموجود باید ناموفق باشد");
        }
    }
    
    private static void testRegisterStudent_Success(TestAuthenticationService authService) {
        boolean result = authService.registerStudent("newstudent", "password", "ST003", 
                                                    "New Student", "new@university.com");
        if (!result) {
            throw new RuntimeException("ثبت‌نام دانشجو باید موفق باشد");
        }
    }
    
    private static void testChangePassword_Success(TestAuthenticationService authService) {
        // ثبت کاربر و تغییر رمز عبور
        boolean registered = authService.registerStudent("changepass", "oldpass", "ST006", 
                                                        "Change Pass", "change@university.com");
        if (!registered) {
            throw new RuntimeException("ثبت کاربر برای تست تغییر رمز باید موفق باشد");
        }
        
        boolean result = authService.changePassword("changepass", "oldpass", "newpass");
        if (!result) {
            throw new RuntimeException("تغییر رمز عبور باید موفق باشد");
        }
        
        // بررسی می‌کنیم که با رمز جدید می‌توان لاگین کرد
        Optional<User> user = authService.login("changepass", "newpass");
        if (!user.isPresent()) {
            throw new RuntimeException("لاگین با رمز جدید باید موفق باشد");
        }
    }
    
    private static void testChangePassword_WrongOldPassword(TestAuthenticationService authService) {
        boolean registered = authService.registerStudent("user2", "password", "ST007", 
                                                        "User Two", "user2@university.com");
        if (!registered) {
            throw new RuntimeException("ثبت کاربر برای تست تغییر رمز باید موفق باشد");
        }
        
        boolean result = authService.changePassword("user2", "wrongold", "newpass");
        if (result) {
            throw new RuntimeException("تغییر رمز با رمز قدیمی نادرست باید ناموفق باشد");
        }
    }
    
    private static void testRegisterStudent_NullUsername(TestAuthenticationService authService) {
        boolean result = authService.registerStudent(null, "password", "ST003", 
                                                    "New Student", "new@university.com");
        if (result) {
            throw new RuntimeException("ثبت‌نام با نام کاربری null باید ناموفق باشد");
        }
    }
    
    private static void testRegisterStudent_NullPassword(TestAuthenticationService authService) {
        boolean result = authService.registerStudent("newuser", null, "ST003", 
                                                    "New Student", "new@university.com");
        if (result) {
            throw new RuntimeException("ثبت‌نام با رمز عبور null باید ناموفق باشد");
        }
    }
}