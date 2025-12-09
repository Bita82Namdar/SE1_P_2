package com.university.library.model;

public class StudentDelayReport {
    private String studentId;
    private int delayCount;
    private int notReturnedCount;
    
    public StudentDelayReport(String studentId, int delayCount, int notReturnedCount) {
        this.studentId = studentId;
        this.delayCount = delayCount;
        this.notReturnedCount = notReturnedCount;
    }
    
    // Getters
    public String getStudentId() { return studentId; }
    public int getDelayCount() { return delayCount; }
    public int getNotReturnedCount() { return notReturnedCount; }
    
    @Override
    public String toString() {
        return String.format("StudentDelayReport{studentId='%s', delayCount=%d, notReturned=%d}",
            studentId, delayCount, notReturnedCount);
    }
}
