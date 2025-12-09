package com.university.library.repository;

import com.university.library.model.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookRepository {
    private List<Book> books;
    private static BookRepository instance;
    
    private BookRepository() {
        this.books = new ArrayList<>();
        initializeSampleData();
    }
    
    public static synchronized BookRepository getInstance() {
        if (instance == null) {
            instance = new BookRepository();
        }
        return instance;
    }
    
    private void initializeSampleData() {
        books.add(new Book("B1", "طراحی نرم‌افزار", "ایرج صادقی", 2020));
        books.add(new Book("B2", "پایگاه داده", "مریم کریمی", 2019));
        books.add(new Book("B3", "الگوریتم‌ها", "رضا محمودی", 2021));
    }
    
    public Book save(Book book) {
        books.removeIf(b -> b.getBookId().equals(book.getBookId()));
        books.add(book);
        return book;
    }
    
    public Optional<Book> findById(String bookId) {
        return books.stream()
                .filter(book -> book.getBookId().equals(bookId))
                .findFirst();
    }
    
    public List<Book> findByTitle(String title) {
        return books.stream()
                .filter(book -> book.getTitle().contains(title))
                .collect(Collectors.toList());
    }
    
    public List<Book> findByAuthor(String author) {
        return books.stream()
                .filter(book -> book.getAuthor().contains(author))
                .collect(Collectors.toList());
    }
    
    public List<Book> findAll() {
        return new ArrayList<>(books);
    }
    
    public void delete(String bookId) {
        books.removeIf(book -> book.getBookId().equals(bookId));
    }
}