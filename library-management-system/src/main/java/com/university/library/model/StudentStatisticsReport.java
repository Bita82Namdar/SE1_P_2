package com.university.library.model;

import java.util.List;

public class StudentStatisticsReport {
    private int totalStudents;
    private int activeStudents;
    private int inactiveStudents;
    private double averageLoansPerStudent;
    private List<StudentDelayReport> topDelayedStudents;
    
    public StudentStatisticsReport(int totalStudents, int activeStudents, 
                                  int inactiveStudents, double averageLoansPerStudent,
                                  List<StudentDelayReport> topDelayedStudents) {
        this.totalStudents = totalStudents;
        this.activeStudents = activeStudents;
        this.inactiveStudents = inactiveStudents;
        this.averageLoansPerStudent = averageLoansPerStudent;
        this.topDelayedStudents = topDelayedStudents;
    }
    
    // Getters
    public int getTotalStudents() { return totalStudents; }
    public int getActiveStudents() { return activeStudents; }
    public int getInactiveStudents() { return inactiveStudents; }
    public double getAverageLoansPerStudent() { return averageLoansPerStudent; }
    public List<StudentDelayReport> getTopDelayedStudents() { return topDelayedStudents; }
    
    @Override
    public String toString() {
        return String.format("StudentStatisticsReport{totalStudents=%d, active=%d, " +
                           "inactive=%d, avgLoans=%.2f, topDelayed=%d}",
            totalStudents, activeStudents, inactiveStudents, 
            averageLoansPerStudent, topDelayedStudents.size());
    }
}