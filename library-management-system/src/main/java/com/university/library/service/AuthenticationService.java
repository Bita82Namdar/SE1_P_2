package com.university.library.service;

import com.university.library.model.User;
import com.university.library.model.Student;
import com.university.library.model.Employee;
import com.university.library.model.Librarian;
import com.university.library.repository.UserRepository;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

public class AuthenticationService {
    private UserRepository userRepository;

    public AuthenticationService() {
        this.userRepository = UserRepository.getInstance();
    }

    public Optional<User> login(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return Optional.empty();
        }
        
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password) && user.get().isActive()) {
            return user;
        }
        return Optional.empty();
    }

    // ========== متدهای جدید اضافه شده برای سناریوهای تست ==========
    
    /**
     * متد عمومی ثبت‌نام برای انواع کاربران
     * مناسب برای سناریوهای 1-1 و 1-2
     */
    public boolean register(String username, String password) {
        return registerStudent(username, password, null, username, null);
    }
    
    /**
     * متد عمومی ثبت‌نام با جزئیات بیشتر
     */
    public boolean register(String username, String password, String userType, 
                           String id, String fullName, String email) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return false;
        }
        
        // بررسی تکراری نبودن نام کاربری
        if (userRepository.findByUsername(username).isPresent()) {
            return false;
        }
        
        // ایجاد کاربر بر اساس نوع
        boolean registrationResult = false;
        
        switch (userType.toUpperCase()) {
            case "STUDENT":
                registrationResult = registerStudent(username, password, id, 
                    fullName != null ? fullName : username, email);
                break;
                
            case "EMPLOYEE":
                registrationResult = registerEmployee(username, password, id, 
                    fullName != null ? fullName : username, email);
                break;
                
            case "LIBRARIAN":
                registrationResult = registerLibrarian(username, password, 
                    fullName != null ? fullName : username, email);
                break;
                
            default:
                // نوع کاربر پیش‌فرض: Student
                registrationResult = registerStudent(username, password, id, 
                    fullName != null ? fullName : username, email);
                break;
        }
        
        return registrationResult;
    }
    
    /**
     * بررسی وجود کاربر با نام کاربری مشخص
     * مفید برای تست‌ها
     */
    public boolean userExists(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        return userRepository.findByUsername(username).isPresent();
    }
    
    /**
     * بررسی وضعیت فعال بودن کاربر
     */
    public boolean isUserActive(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent() && user.get().isActive();
    }
    
    /**
     * غیرفعال کردن کاربر
     */
    public boolean deactivateUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            user.get().setActive(false);
            return userRepository.updateUser(user.get());
        }
        return false;
    }
    
    /**
     * فعال کردن کاربر
     */
    public boolean activateUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            user.get().setActive(true);
            return userRepository.updateUser(user.get());
        }
        return false;
    }
    // ========== پایان متدهای جدید ==========

    // متدهای قبلی با اصلاح خطاها
    public boolean registerStudent(String username, String password, String studentId, String fullName, String email) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty() || 
            userRepository.findByUsername(username).isPresent()) {
            return false;
        }

        String userId = "S" + (userRepository.findAll().size() + 1);
        String finalStudentId = (studentId == null || studentId.isEmpty()) ? 
            "ST" + (getStudentsCount() + 1) : studentId;
            
        Student student;
        if (email != null && !email.isEmpty()) {
            // استفاده از constructor با 6 پارامتر
            student = new Student(userId, username, password, finalStudentId, fullName, email);
        } else {
            // استفاده از constructor با 5 پارامتر
            student = new Student(userId, username, password, fullName, finalStudentId);
        }
        
        student.setActive(true); // فعال به صورت پیش‌فرض
        userRepository.addUser(student);
        return true;
    }

    public boolean registerEmployee(String username, String password, String employeeId, String fullName, String email) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty() || 
            userRepository.findByUsername(username).isPresent()) {
            return false;
        }

        String userId = "E" + (userRepository.findAll().size() + 1);
        String finalEmployeeId = (employeeId == null || employeeId.isEmpty()) ? 
            "EMP" + (getEmployeesCount() + 1) : employeeId;
            
        Employee employee;
        if (email != null && !email.isEmpty()) {
            // استفاده از constructor با 6 پارامتر
            employee = new Employee(userId, username, password, fullName, finalEmployeeId, email);
        } else {
            // استفاده از constructor با 5 پارامتر
            employee = new Employee(userId, username, password, fullName, finalEmployeeId);
        }
        
        employee.setActive(true); // فعال به صورت پیش‌فرض
        userRepository.addUser(employee);
        return true;
    }

    public boolean registerLibrarian(String username, String password, String fullName, String email) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty() || 
            userRepository.findByUsername(username).isPresent()) {
            return false;
        }

        String userId = "L" + (userRepository.findAll().size() + 1);
        String employeeId = "LIB" + (getEmployeesCount() + 1);
            
        Librarian librarian;
        if (email != null && !email.isEmpty()) {
            // استفاده از constructor با 6 پارامتر
            librarian = new Librarian(userId, username, password, fullName, employeeId, email);
        } else {
            // استفاده از constructor با 5 پارامتر
            librarian = new Librarian(userId, username, password, fullName, employeeId);
        }
        
        librarian.setActive(true); // فعال به صورت پیش‌فرض
        userRepository.addUser(librarian);
        return true;
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        Optional<User> user = login(username, oldPassword);
        if (user.isPresent()) {
            user.get().setPassword(newPassword);
            return userRepository.updateUser(user.get());
        }
        return false;
    }
    
    /**
     * دریافت تعداد کاربران ثبت‌نام شده
     * مفید برای گزارش‌گیری
     */
    public int getRegisteredUsersCount() {
        return userRepository.findAll().size();
    }
    
    /**
     * دریافت تعداد دانشجویان (با فیلتر کردن از لیست همه کاربران)
     */
    public int getStudentsCount() {
        List<User> allUsers = userRepository.findAll();
        return (int) allUsers.stream()
                .filter(user -> user instanceof Student)
                .count();
    }
    
    /**
     * دریافت تعداد کارمندان (با فیلتر کردن از لیست همه کاربران)
     */
    public int getEmployeesCount() {
        List<User> allUsers = userRepository.findAll();
        return (int) allUsers.stream()
                .filter(user -> user instanceof Employee || user instanceof Librarian)
                .count();
    }
    
    /**
     * دریافت لیست دانشجویان
     */
    public List<Student> getAllStudents() {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream()
                .filter(user -> user instanceof Student)
                .map(user -> (Student) user)
                .collect(Collectors.toList());
    }
    
    /**
     * دریافت لیست کارمندان
     */
    public List<Employee> getAllEmployees() {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream()
                .filter(user -> user instanceof Employee)
                .map(user -> (Employee) user)
                .collect(Collectors.toList());
    }
    
    /**
     * دریافت لیست کتابداران
     */
    public List<Librarian> getAllLibrarians() {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream()
                .filter(user -> user instanceof Librarian)
                .map(user -> (Librarian) user)
                .collect(Collectors.toList());
    }
    
    /**
     * دریافت کاربر بر اساس ID
     */
    public Optional<User> getUserById(String userId) {
        return userRepository.findById(userId);
    }
    
    /**
     * به‌روزرسانی اطلاعات کاربر
     */
    public boolean updateUser(User user) {
        return userRepository.updateUser(user);
    }
}