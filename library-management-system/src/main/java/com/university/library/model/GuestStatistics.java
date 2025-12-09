package com.university.library.model;

public class GuestStatistics {
    private int totalStudents;
    private int totalBooks;
    private int totalLoans;
    private int currentLoans;
    
    public GuestStatistics(int totalStudents, int totalBooks, 
                          int totalLoans, int currentLoans) {
        this.totalStudents = totalStudents;
        this.totalBooks = totalBooks;
        this.totalLoans = totalLoans;
        this.currentLoans = currentLoans;
    }
    
    // Getters
    public int getTotalStudents() { return totalStudents; }
    public int getTotalBooks() { return totalBooks; }
    public int getTotalLoans() { return totalLoans; }
    public int getCurrentLoans() { return currentLoans; }
}
