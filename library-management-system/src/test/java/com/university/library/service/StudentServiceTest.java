package com.university.library.service;

import com.university.library.model.Student;
import com.university.library.repository.UserRepository;

public class StudentServiceTest {

    private final UserRepository userRepository;

    public StudentServiceTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isStudentActive(String studentId) {
        Student student = userRepository.findStudentById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("دانشجو با این شناسه یافت نشد"));
        return student.isActive();
    }

    public Student getStudentById(String studentId) {
        return userRepository.findStudentById(studentId).orElse(null);
    }

    public Student getStudentByIdOrThrow(String studentId) {
        return userRepository.findStudentById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("دانشجو با این شناسه یافت نشد"));
    }

    public boolean activateStudent(String studentId) {
        Student student = userRepository.findStudentById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("دانشجو با این شناسه یافت نشد"));
        student.setActive(true);
        return userRepository.updateUser(student);
    }

    public boolean deactivateStudent(String studentId) {
        Student student = userRepository.findStudentById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("دانشجو با این شناسه یافت نشد"));
        student.setActive(false);
        return userRepository.updateUser(student);
    }
}