package com.university.library.model;

public class LibraryStats {
    private double averageLoanDays;
    private int totalStudents;
    private int totalBooks;
    private int totalLoans;
    
    public LibraryStats(double averageLoanDays, int totalStudents, 
                       int totalBooks, int totalLoans) {
        this.averageLoanDays = averageLoanDays;
        this.totalStudents = totalStudents;
        this.totalBooks = totalBooks;
        this.totalLoans = totalLoans;
    }
    
    // Getters
    public double getAverageLoanDays() { return averageLoanDays; }
    public int getTotalStudents() { return totalStudents; }
    public int getTotalBooks() { return totalBooks; }
    public int getTotalLoans() { return totalLoans; }
    
    @Override
    public String toString() {
        return String.format("LibraryStats{averageLoanDays=%.2f, totalStudents=%d, " +
                           "totalBooks=%d, totalLoans=%d}",
            averageLoanDays, totalStudents, totalBooks, totalLoans);
    }
}
