package com.university.library.model;

public class Employee extends User {
    private String employeeId;
    
    public Employee(String userId, String username, String password, String fullName, String employeeId) {
        super(userId, username, password, fullName);
        this.employeeId = employeeId;
    }
    
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
}
