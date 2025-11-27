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
        books.add(new Book("B1", "طراحی نرم‌افزار", "احمد رضایی", "978-964-1234-56-7", 2020, "نشر دانشگاهی", 5));
        books.add(new Book("B2", "پایگاه داده", "مریم کریمی", "978-964-1234-57-4", 2019, "نشر فنی", 3));
        books.add(new Book("B3", "هوش مصنوعی", "رضا محمدی", "978-964-1234-58-1", 2021, "نشر علمی", 4));
        books.add(new Book("B4", "مهندسی اینترنت", "سارا احمدی", "978-964-1234-59-8", 2022, "نشر الکترونیک", 2));
        books.add(new Book("B5", "الگوریتم‌های پیشرفته", "محمد حسینی", "978-964-1234-60-4", 2018, "نشر محاسبات", 6));
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public Optional<Book> findById(String bookId) {
        return books.stream()
                .filter(book -> book.getBookId().equals(bookId))
                .findFirst();
    }

    public List<Book> findAll() {
        return new ArrayList<>(books);
    }

    public List<Book> searchByTitle(String title) {
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> searchByAuthor(String author) {
        return books.stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> searchByPublicationYear(int year) {
        return books.stream()
                .filter(book -> book.getPublicationYear() == year)
                .collect(Collectors.toList());
    }

    public List<Book> advancedSearch(String title, String author, Integer year) {
        return books.stream()
                .filter(book -> 
                    (title == null || book.getTitle().toLowerCase().contains(title.toLowerCase())) &&
                    (author == null || book.getAuthor().toLowerCase().contains(author.toLowerCase())) &&
                    (year == null || book.getPublicationYear() == year)
                )
                .collect(Collectors.toList());
    }

    public boolean updateBook(Book updatedBook) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getBookId().equals(updatedBook.getBookId())) {
                books.set(i, updatedBook);
                return true;
            }
        }
        return false;
    }

    public int getTotalBooksCount() {
        return books.size();
    }

    // متد جدید برای جستجوی ترکیبی پیشرفته‌تر
    public List<Book> searchBooks(String title, String author, Integer year, Boolean availableOnly) {
        return books.stream()
                .filter(book -> 
                    (title == null || book.getTitle().toLowerCase().contains(title.toLowerCase())) &&
                    (author == null || book.getAuthor().toLowerCase().contains(author.toLowerCase())) &&
                    (year == null || book.getPublicationYear() == year) &&
                    (availableOnly == null || !availableOnly || book.isAvailable())
                )
                .collect(Collectors.toList());
    }
}