package com.university.library.repository;

import com.university.library.model.User;
import com.university.library.model.Student;
import com.university.library.model.Employee;
import java.util.*;
import java.util.stream.Collectors;

public class UserRepository {
    private static UserRepository instance;
    private Map<String, User> users;
    
    private UserRepository() {
        this.users = new HashMap<>();
        initializeSampleData();
    }
    
    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }
    
    private void initializeSampleData() {
        // اضافه کردن کاربران نمونه
        Student student1 = new Student("S1", "student1", "pass123", "John Doe", "ST001");
        student1.setEmail("john@university.com");
        
        Student student2 = new Student("S2", "student2", "pass456", "Jane Smith", "ST002");
        student2.setEmail("jane@university.com");
        
        Employee employee1 = new Employee("E1", "employee1", "emp123", "Ali Rezaei", "EMP001");
        employee1.setEmail("ali@university.com");
        
        users.put(student1.getUserId(), student1);
        users.put(student2.getUserId(), student2);
        users.put(employee1.getUserId(), employee1);
    }
    
    public Optional<User> findByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }
    
    public boolean addUser(User user) {
        if (users.containsKey(user.getUserId()) || findByUsername(user.getUsername()).isPresent()) {
            return false;
        }
        users.put(user.getUserId(), user);
        return true;
    }
    
    public boolean updateUser(User user) {
        if (!users.containsKey(user.getUserId())) {
            return false;
        }
        users.put(user.getUserId(), user);
        return true;
    }
    
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
    
    public List<Student> getAllStudents() {
        return users.values().stream()
                .filter(user -> user instanceof Student)
                .map(user -> (Student) user)
                .collect(Collectors.toList());
    }
    
    public List<Employee> getAllEmployees() {
        return users.values().stream()
                .filter(user -> user instanceof Employee)
                .map(user -> (Employee) user)
                .collect(Collectors.toList());
    }
    
    public Optional<User> findById(String userId) {
        return Optional.ofNullable(users.get(userId));
    }
    
    public boolean removeUser(String userId) {
        return users.remove(userId) != null;
    }
    
    // اضافه کردن متد clear برای تست
    public void clear() {
        users.clear();
        // بعد از clear، داده‌های نمونه را دوباره اضافه نکنیم تا تست‌ها تمیز باشند
    }
    
    // متد برای ریست کردن و اضافه کردن داده‌های نمونه
    public void resetWithSampleData() {
        users.clear();
        initializeSampleData();
    }
}