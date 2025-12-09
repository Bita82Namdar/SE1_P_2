package com.university.library.service;

import com.university.library.model.Student;
import com.university.library.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

public class StudentService {
    private UserRepository userRepository;
    
    public StudentService() {
        this.userRepository = UserRepository.getInstance();
    }
    
    public StudentService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * بررسی می‌کند که دانشجو فعال است یا نه
     * @param studentId شناسه دانشجو
     * @return true اگر دانشجو فعال باشد
     */
    public boolean isStudentActive(String studentId) {
        Optional<Student> studentOptional = userRepository.findStudentById(studentId);
        if (studentOptional.isEmpty()) {
            throw new IllegalArgumentException("دانشجو با این شناسه یافت نشد: " + studentId);
        }
        return studentOptional.get().isActive();
    }
    
    /**
     * دریافت اطلاعات دانشجو
     * @param studentId شناسه دانشجو
     * @return Student object یا null اگر یافت نشود
     */
    public Student getStudentById(String studentId) {
        Optional<Student> studentOptional = userRepository.findStudentById(studentId);
        return studentOptional.orElse(null);
    }
    
    /**
     * دریافت اطلاعات دانشجو با Exception
     * @param studentId شناسه دانشجو
     * @return Student object
     * @throws IllegalArgumentException اگر دانشجو یافت نشود
     */
    public Student getStudentByIdOrThrow(String studentId) {
        Optional<Student> studentOptional = userRepository.findStudentById(studentId);
        if (studentOptional.isEmpty()) {
            throw new IllegalArgumentException("دانشجو با این شناسه یافت نشد: " + studentId);
        }
        return studentOptional.get();
    }
    
    /**
     * فعال کردن دانشجو
     */
    public boolean activateStudent(String studentId) {
        Optional<Student> studentOptional = userRepository.findStudentById(studentId);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            student.setActive(true);
            return userRepository.updateUser(student);
        }
        return false;
    }
    
    /**
     * غیرفعال کردن دانشجو
     */
    public boolean deactivateStudent(String studentId) {
        Optional<Student> studentOptional = userRepository.findStudentById(studentId);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            student.setActive(false);
            return userRepository.updateUser(student);
        }
        return false;
    }
    
    // ========== متدهای جدید برای گزارش‌گیری (سناریوی ۴) ==========
    
    /**
     * تعداد کل دانشجویان را برمی‌گرداند
     * @return تعداد دانشجویان
     */
    public int getTotalStudentsCount() {
        // پیاده‌سازی بر اساس repository
        return userRepository.countStudents();
    }
    
    /**
     * لیست تمام شناسه‌های دانشجویی را برمی‌گرداند
     * @return لیست شناسه‌های دانشجویی
     */
    public List<String> getAllStudentIds() {
        // پیاده‌سازی بر اساس repository
        return userRepository.getAllStudentIds();
    }
    
    /**
     * دریافت تعداد دانشجویان فعال
     * @return تعداد دانشجویان فعال
     */
    public int getActiveStudentsCount() {
        List<Student> allStudents = userRepository.findAllStudents();
        int activeCount = 0;
        for (Student student : allStudents) {
            if (student.isActive()) {
                activeCount++;
            }
        }
        return activeCount;
    }
    
    /**
     * دریافت تعداد دانشجویان غیرفعال
     * @return تعداد دانشجویان غیرفعال
     */
    public int getInactiveStudentsCount() {
        return getTotalStudentsCount() - getActiveStudentsCount();
    }
    
    /**
     * دریافت لیست دانشجویان فعال
     * @return لیست دانشجویان فعال
     */
    public List<Student> getActiveStudents() {
        List<Student> allStudents = userRepository.findAllStudents();
        List<Student> activeStudents = new ArrayList<>();
        for (Student student : allStudents) {
            if (student.isActive()) {
                activeStudents.add(student);
            }
        }
        return activeStudents;
    }
    
    /**
     * دریافت لیست دانشجویان غیرفعال
     * @return لیست دانشجویان غیرفعال
     */
    public List<Student> getInactiveStudents() {
        List<Student> allStudents = userRepository.findAllStudents();
        List<Student> inactiveStudents = new ArrayList<>();
        for (Student student : allStudents) {
            if (!student.isActive()) {
                inactiveStudents.add(student);
            }
        }
        return inactiveStudents;
    }
}