package com.university.library.model;

public abstract class User {
    private String userId;
    private String username;
    private String password;
    private String email;
    private UserType userType;
    private boolean active;
    
    // Constructor
    public User(String userId, String username, String password, String email, UserType userType) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.userType = userType;
        this.active = true;
    }
    
    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; } // اضافه کردن setter
    
    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    @Override
    public String toString() {
        return String.format("User{id='%s', username='%s', type=%s, active=%s}", 
                userId, username, userType, active);
    }
}