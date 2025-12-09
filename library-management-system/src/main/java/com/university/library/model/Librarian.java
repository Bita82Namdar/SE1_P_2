package com.university.library.model;

public class Librarian extends Employee {
    
    // constructor با 5 پارامتر
    public Librarian(String userId, String username, String password,
                     String fullName, String employeeId) {
        super(userId, username, password, fullName, employeeId);
    }
    
    // constructor با 6 پارامتر
    public Librarian(String userId, String username, String password,
                     String fullName, String employeeId, String email) {
        super(userId, username, password, fullName, employeeId, email);
    }
    
    @Override
    public String toString() {
        return String.format("Librarian{id='%s', username='%s', name='%s', active=%s}", 
                getEmployeeId(), getUsername(), getFullName(), isActive());
    }
}