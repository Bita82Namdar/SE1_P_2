package com.university.library.model;

public class Student extends User {
    private String studentId;
    
    public Student(String userId, String username, String password, String fullName, String studentId) {
        super(userId, username, password, fullName);
        this.studentId = studentId;
    }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
}
