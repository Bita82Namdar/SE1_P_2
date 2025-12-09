package com.university.library.model;

public class Student extends User {
    private String studentId;
    private String fullName;
    private String major;
    private int enrollmentYear;
    private boolean active;
    
    // constructor با 6 پارامتر
    public Student(String userId, String username, String password, 
                   String studentId, String fullName, String email) {
        super(userId, username, password, email, UserType.STUDENT);
        this.studentId = studentId;
        this.fullName = fullName;
        this.major = "Unknown";
        this.enrollmentYear = 2023;
        this.active = true;
    }
    
    // constructor با 7 پارامتر (برای تست)
    public Student(String userId, String username, String password, String email,
                   String studentId, String major, int enrollmentYear) {
        super(userId, username, password, email, UserType.STUDENT);
        this.studentId = studentId;
        this.fullName = username; // استفاده از username به عنوان fullName
        this.major = major;
        this.enrollmentYear = enrollmentYear;
        this.active = true;
    }
    
    // constructor با 8 پارامتر (کامل)
    public Student(String userId, String username, String password, String email,
                   String studentId, String fullName, String major, int enrollmentYear) {
        super(userId, username, password, email, UserType.STUDENT);
        this.studentId = studentId;
        this.fullName = fullName;
        this.major = major;
        this.enrollmentYear = enrollmentYear;
        this.active = true;
    }
    
    // constructor با 5 پارامتر
    public Student(String userId, String username, String password, 
                   String fullName, String studentId) {
        super(userId, username, password, "", UserType.STUDENT);
        this.studentId = studentId;
        this.fullName = fullName;
        this.major = "Unknown";
        this.enrollmentYear = 2023;
        this.active = true;
    }
    
    // getters and setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
    
    public int getEnrollmentYear() { return enrollmentYear; }
    public void setEnrollmentYear(int enrollmentYear) { this.enrollmentYear = enrollmentYear; }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    @Override
    public String toString() {
        return String.format("Student{id='%s', username='%s', name='%s', active=%s}",
                studentId, getUsername(), fullName, active);
    }
}