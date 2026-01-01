package com.university.library.repository;

import com.university.library.model.User;
import com.university.library.model.Student;
import com.university.library.model.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserRepository {
    private List<User> users;
    private static UserRepository instance;

    private UserRepository() {
        this.users = new ArrayList<>();
        // Add some sample data
        initializeSampleData();
    }

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    private void initializeSampleData() {
        users.add(new Student("S1", "student1", "pass123", "ST001", "علی محمدی", "ali@university.com"));
        users.add(new Student("S2", "student2", "pass123", "ST002", "فاطمه احمدی", "fatemeh@university.com"));
        
        users.add(new Employee("E1", "emp1", "emp123", "EMP001", "کارمند یک"));
        users.add(new Employee("E2", "emp2", "emp123", "EMP002", "کارمند دو"));
    }

    // متد save (همان addUser با نام جدید)
    public User save(User user) {
        users.add(user);
        return user;
    }

    public Optional<User> findByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    public Optional<User> findById(String userId) {
        return users.stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst();
    }

    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    // متدهای مخصوص دانشجو
    public Optional<Student> findStudentById(String studentId) {
        return users.stream()
                .filter(user -> user instanceof Student && user.getUserId().equals(studentId))
                .map(user -> (Student) user)
                .findFirst();
    }

    public boolean isStudentActive(String studentId) {
        return users.stream()
                .filter(user -> user instanceof Student && user.getUserId().equals(studentId))
                .map(user -> (Student) user)
                .findFirst()
                .map(Student::isActive)
                .orElse(false);
    }

    public List<Student> findAllStudents() {
        return users.stream()
                .filter(user -> user instanceof Student)
                .map(user -> (Student) user)
                .collect(Collectors.toList());
    }

    // متدهای اضافی
    public void delete(String userId) {
        users.removeIf(user -> user.getUserId().equals(userId));
    }

    public List<User> findByType(String userType) {
        return users.stream()
                .filter(user -> user.getUserType().equals(userType))
                .collect(Collectors.toList());
    }

    // متدهای موجود قبلی با نام‌های جدید
    public void addUser(User user) {
        save(user);
    }

    public List<Student> getAllStudents() {
        return findAllStudents();
    }

    public List<Employee> getAllEmployees() {
        return users.stream()
                .filter(user -> user instanceof Employee)
                .map(user -> (Employee) user)
                .collect(Collectors.toList());
    }

    public boolean updateUser(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(updatedUser.getUserId())) {
                users.set(i, updatedUser);
                return true;
            }
        }
        return false;
    }

    public int getTotalStudentsCount() {
        return (int) users.stream()
                .filter(user -> user instanceof Student)
                .count();
    }
}