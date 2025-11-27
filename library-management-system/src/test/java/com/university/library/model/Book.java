package com.university.library.model;

public class Book {
    private String bookId;
    private String title;
    private String author;
    private String isbn;
    private int publicationYear;
    private String publisher;
    private int availableCopies;
    private int totalCopies;

    // Constructor کامل
    public Book(String bookId, String title, String author, String isbn, 
                int publicationYear, String publisher, int totalCopies) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.publisher = publisher;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies; // در ابتدا همه کپی‌ها موجود هستند
    }

    // Getter methods
    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public int getPublicationYear() { return publicationYear; }
    public String getPublisher() { return publisher; }
    public int getAvailableCopies() { return availableCopies; }
    public int getTotalCopies() { return totalCopies; }

    // Setter methods
    public void setBookId(String bookId) { this.bookId = bookId; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setPublicationYear(int publicationYear) { this.publicationYear = publicationYear; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }

    // متد برای بررسی موجود بودن کتاب
    public boolean isAvailable() {
        return availableCopies > 0;
    }

    // متد برای کاهش تعداد کپی‌های موجود هنگام امانت
    public void borrowCopy() {
        if (availableCopies > 0) {
            availableCopies--;
        }
    }

    // متد برای افزایش تعداد کپی‌های موجود هنگام بازگشت
    public void returnCopy() {
        if (availableCopies < totalCopies) {
            availableCopies++;
        }
    }

    @Override
    public String toString() {
        return String.format("Book{id='%s', title='%s', author='%s', year=%d, available=%d/%d}", 
                bookId, title, author, publicationYear, availableCopies, totalCopies);
    }
}