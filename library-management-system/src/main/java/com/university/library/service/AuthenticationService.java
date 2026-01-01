package com.university.library.service;

public interface AuthenticationService {
    boolean register(String username, String password, String firstName, 
                    String lastName, String email, String studentId);
    boolean login(String username, String password);
    boolean changePassword(String username, String oldPassword, String newPassword);
}