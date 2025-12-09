package com.university.library.service;

import com.university.library.model.Book;
import com.university.library.repository.BookRepository;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

public class BookService {
    private BookRepository bookRepository;
    
    public BookService() {
        this.bookRepository = BookRepository.getInstance();
    }
    
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    
    /**
     * بررسی می‌کند که کتاب موجود است یا نه
     * @param bookId شناسه کتاب
     * @return true اگر کتاب موجود باشد
     */
    public boolean isBookAvailable(String bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            return false;
        }
        return bookOptional.get().isAvailable();
    }
    
    /**
     * علامت‌گذاری کتاب به عنوان امانت داده شده
     */
    public void markBookAsBorrowed(String bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            book.setAvailable(false);
            bookRepository.save(book);
        }
    }
    
    /**
     * علامت‌گذاری کتاب به عنوان برگردانده شده
     */
    public void markBookAsReturned(String bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            book.setAvailable(true);
            bookRepository.save(book);
        }
    }
    
    // ========== متدهای جدید برای گزارش‌گیری (سناریوی ۴) ==========
    
    /**
     * تعداد کل کتاب‌ها را برمی‌گرداند
     * @return تعداد کل کتاب‌ها
     */
    public int getTotalBooksCount() {
        return bookRepository.countBooks();
    }
    
    /**
     * تعداد کتاب‌های موجود برای امانت را برمی‌گرداند
     * @return تعداد کتاب‌های موجود
     */
    public int getAvailableBooksCount() {
        List<Book> allBooks = bookRepository.findAll();
        int availableCount = 0;
        for (Book book : allBooks) {
            if (book.isAvailable()) {
                availableCount++;
            }
        }
        return availableCount;
    }
    
    /**
     * تعداد کتاب‌های امانت داده شده را برمی‌گرداند
     * @return تعداد کتاب‌های امانت داده شده
     */
    public int getBorrowedBooksCount() {
        return getTotalBooksCount() - getAvailableBooksCount();
    }
    
    /**
     * لیست کتاب‌های موجود را برمی‌گرداند
     * @return لیست کتاب‌های موجود
     */
    public List<Book> getAvailableBooks() {
        List<Book> allBooks = bookRepository.findAll();
        List<Book> availableBooks = new ArrayList<>();
        for (Book book : allBooks) {
            if (book.isAvailable()) {
                availableBooks.add(book);
            }
        }
        return availableBooks;
    }
    
    /**
     * لیست کتاب‌های امانت داده شده را برمی‌گرداند
     * @return لیست کتاب‌های امانت داده شده
     */
    public List<Book> getBorrowedBooks() {
        List<Book> allBooks = bookRepository.findAll();
        List<Book> borrowedBooks = new ArrayList<>();
        for (Book book : allBooks) {
            if (!book.isAvailable()) {
                borrowedBooks.add(book);
            }
        }
        return borrowedBooks;
    }
    
    /**
     * جستجوی کتاب بر اساس عنوان
     * @param title عنوان کتاب
     * @return لیست کتاب‌های مطابق
     */
    public List<Book> searchBooksByTitle(String title) {
        List<Book> allBooks = bookRepository.findAll();
        List<Book> matchingBooks = new ArrayList<>();
        for (Book book : allBooks) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                matchingBooks.add(book);
            }
        }
        return matchingBooks;
    }
    
    /**
     * جستجوی کتاب بر اساس نویسنده
     * @param author نام نویسنده
     * @return لیست کتاب‌های مطابق
     */
    public List<Book> searchBooksByAuthor(String author) {
        List<Book> allBooks = bookRepository.findAll();
        List<Book> matchingBooks = new ArrayList<>();
        for (Book book : allBooks) {
            if (book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                matchingBooks.add(book);
            }
        }
        return matchingBooks;
    }
    
    /**
     * جستجوی کتاب بر اساس سال انتشار
     * @param year سال انتشار
     * @return لیست کتاب‌های مطابق
     */
    public List<Book> searchBooksByYear(int year) {
        List<Book> allBooks = bookRepository.findAll();
        List<Book> matchingBooks = new ArrayList<>();
        for (Book book : allBooks) {
            if (book.getPublicationYear() == year) {
                matchingBooks.add(book);
            }
        }
        return matchingBooks;
    }
    
    /**
     * دریافت اطلاعات کتاب
     * @param bookId شناسه کتاب
     * @return Book object یا null اگر یافت نشود
     */
    public Book getBookById(String bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        return bookOptional.orElse(null);
    }
}