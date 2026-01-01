package com.university.library.controller;

import java.util.List;
import java.util.ArrayList;

// Simple REST Controller without Spring dependencies
public class BookController {
    
    // Simple Book class
    public static class Book {
        private String id;
        private String title;
        private String author;
        private Integer year;
        
        public Book() {}
        
        public Book(String id, String title, String author, Integer year) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.year = year;
        }
        
        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
        
        public Integer getYear() { return year; }
        public void setYear(Integer year) { this.year = year; }
    }
    
    // Simple Response class
    public static class ResponseEntity<T> {
        private T body;
        private int statusCode;
        
        public ResponseEntity(T body, int statusCode) {
            this.body = body;
            this.statusCode = statusCode;
        }
        
        public T getBody() { return body; }
        public int getStatusCode() { return statusCode; }
    }
    
    // Mock data
    private List<Book> books = new ArrayList<>();
    
    public BookController() {
        // Initialize with sample data
        books.add(new Book("1", "Effective Java", "Joshua Bloch", 2018));
        books.add(new Book("2", "Clean Code", "Robert Martin", 2008));
        books.add(new Book("3", "Design Patterns", "Gang of Four", 1994));
    }
    
    // GET /api/books
    public ResponseEntity<List<Book>> getBooks(String title, String author) {
        List<Book> result = new ArrayList<>();
        
        for (Book book : books) {
            boolean matches = true;
            
            if (title != null && !book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                matches = false;
            }
            if (author != null && !book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                matches = false;
            }
            
            if (matches) {
                result.add(book);
            }
        }
        
        return new ResponseEntity<>(result, 200); // HTTP 200 OK
    }
    
    // GET /api/books/{id}
    public ResponseEntity<Book> getBookById(String id) {
        for (Book book : books) {
            if (book.getId().equals(id)) {
                return new ResponseEntity<>(book, 200); // HTTP 200 OK
            }
        }
        return new ResponseEntity<>(null, 404); // HTTP 404 Not Found
    }
    
    // POST /api/books
    public ResponseEntity<Book> createBook(Book book) {
        // Generate new ID
        String newId = String.valueOf(books.size() + 1);
        book.setId(newId);
        books.add(book);
        
        return new ResponseEntity<>(book, 201); // HTTP 201 Created
    }
    
    // PUT /api/books/{id}
    public ResponseEntity<Book> updateBook(String id, Book updatedBook) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId().equals(id)) {
                updatedBook.setId(id);
                books.set(i, updatedBook);
                return new ResponseEntity<>(updatedBook, 200); // HTTP 200 OK
            }
        }
        return new ResponseEntity<>(null, 404); // HTTP 404 Not Found
    }
    
    // GET /api/books/search
    public ResponseEntity<List<Book>> searchBooks(String title, String author, Integer year) {
        List<Book> result = new ArrayList<>();
        
        for (Book book : books) {
            boolean matches = true;
            
            if (title != null && !book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                matches = false;
            }
            if (author != null && !book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                matches = false;
            }
            if (year != null && !year.equals(book.getYear())) {
                matches = false;
            }
            
            if (matches) {
                result.add(book);
            }
        }
        
        return new ResponseEntity<>(result, 200); // HTTP 200 OK
    }
    
    // Test method
    public static void main(String[] args) {
        BookController controller = new BookController();
        
        // Test get all books
        ResponseEntity<List<Book>> allBooks = controller.getBooks(null, null);
        System.out.println("All books: " + allBooks.getBody().size());
        
        // Test search by title
        ResponseEntity<List<Book>> javaBooks = controller.getBooks("Java", null);
        System.out.println("Java books: " + javaBooks.getBody().size());
        
        // Test get by ID
        ResponseEntity<Book> book = controller.getBookById("1");
        if (book.getStatusCode() == 200) {
            System.out.println("Found book: " + book.getBody().getTitle());
        }
    }
}