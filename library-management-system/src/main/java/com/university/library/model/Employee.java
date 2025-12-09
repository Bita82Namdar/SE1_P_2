package com.university.library.model;

public class Employee extends User {
    private String employeeId;
    private String fullName;
    
    // constructor با 5 پارامتر
    public Employee(String userId, String username, String password,
                    String fullName, String employeeId) {
        super(userId, username, password, "", UserType.EMPLOYEE); // email خالی
        this.employeeId = employeeId;
        this.fullName = fullName;
    }
    
    // constructor با 6 پارامتر
    public Employee(String userId, String username, String password,
                    String fullName, String employeeId, String email) {
        super(userId, username, password, email, UserType.EMPLOYEE);
        this.employeeId = employeeId;
        this.fullName = fullName;
    }
    
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    @Override
    public String toString() {
        return String.format("Employee{id='%s', username='%s', name='%s', active=%s}", 
                employeeId, getUsername(), fullName, isActive());
    }
}