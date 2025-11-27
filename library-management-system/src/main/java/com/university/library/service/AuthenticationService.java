package com.university.library.service;

import com.university.library.model.User;
import com.university.library.model.Student;
import com.university.library.model.Employee;
import com.university.library.model.Librarian;
import com.university.library.repository.UserRepository;

import java.util.Optional;

public class AuthenticationService {
    private UserRepository userRepository;

    public AuthenticationService() {
        this.userRepository = UserRepository.getInstance();
    }

    public Optional<User> login(String username, String password) {
        if (username == null || password == null) {
            return Optional.empty();
        }
        
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password) && user.get().isActive()) {
            return user;
        }
        return Optional.empty();
    }

    public boolean registerStudent(String username, String password, String studentId, String fullName, String email) {
        if (username == null || password == null || userRepository.findByUsername(username).isPresent()) {
            return false;
        }

        String userId = "S" + (userRepository.findAll().size() + 1);
        String finalStudentId = (studentId == null || studentId.isEmpty()) ? 
            "ST" + (userRepository.getAllStudents().size() + 1) : studentId;
            
        Student student = new Student(userId, username, password, fullName, finalStudentId);
        student.setEmail(email);
        return userRepository.addUser(student);
    }

    public boolean registerEmployee(String username, String password, String employeeId, String fullName, String email) {
        if (username == null || password == null || userRepository.findByUsername(username).isPresent()) {
            return false;
        }

        String userId = "E" + (userRepository.findAll().size() + 1);
        String finalEmployeeId = (employeeId == null || employeeId.isEmpty()) ? 
            "EMP" + (userRepository.getAllEmployees().size() + 1) : employeeId;
            
        Employee employee = new Employee(userId, username, password, fullName, finalEmployeeId);
        employee.setEmail(email);
        return userRepository.addUser(employee);
    }

    public boolean registerLibrarian(String username, String password, String fullName, String email) {
        if (username == null || password == null || userRepository.findByUsername(username).isPresent()) {
            return false;
        }

        String userId = "L" + (userRepository.findAll().size() + 1);
        String employeeId = "LIB" + (userRepository.getAllEmployees().size() + 1);
            
        Librarian librarian = new Librarian(userId, username, password, fullName, employeeId);
        librarian.setEmail(email);
        return userRepository.addUser(librarian);
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        Optional<User> user = login(username, oldPassword);
        if (user.isPresent()) {
            user.get().setPassword(newPassword);
            return userRepository.updateUser(user.get());
        }
        return false;
    }
}
