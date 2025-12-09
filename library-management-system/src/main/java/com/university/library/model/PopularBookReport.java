package com.university.library.model;

public class PopularBookReport {
    private String bookId;
    private String title;
    private String author;
    private int loanCount;
    
    public PopularBookReport(String bookId, String title, String author, int loanCount) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.loanCount = loanCount;
    }
    
    // Getters
    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getLoanCount() { return loanCount; }
}
