package com.university.library.service;

import com.university.library.model.Book;
import com.university.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

public class BookServiceTest {
    private BookService bookService;
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository = BookRepository.getInstance();
        // پاک کردن و ریست کردن داده‌ها برای تست تمیز
        bookRepository.findAll().clear();
        bookService = new BookService();
    }

    @Test
    void testAddBook_Success() {
        boolean result = bookService.addBook("Test Book", "Test Author", "123-4567890", 2023, "Test Publisher", 5);
        assertTrue(result, "Book should be added successfully");
        
        List<Book> books = bookService.getAllBooks();
        assertEquals(1, books.size(), "There should be 1 book in the repository");
        assertEquals("Test Book", books.get(0).getTitle(), "Book title should match");
    }

    @Test
    void testSearchBooks() {
        // اضافه کردن کتاب‌های تست
        bookService.addBook("Java Programming", "John Doe", "111-1111111", 2020, "Tech Pub", 3);
        bookService.addBook("Python Basics", "Jane Smith", "222-2222222", 2021, "Code Pub", 2);
        bookService.addBook("Advanced Java", "John Doe", "333-3333333", 2022, "Tech Pub", 4);

        // جستجو بر اساس عنوان
        List<Book> javaBooks = bookService.searchByTitle("Java");
        assertEquals(2, javaBooks.size(), "Should find 2 Java books");

        // جستجو بر اساس نویسنده
        List<Book> johnBooks = bookService.searchByAuthor("John Doe");
        assertEquals(2, johnBooks.size(), "Should find 2 books by John Doe");

        // جستجو بر اساس سال
        List<Book> year2021Books = bookService.searchByPublicationYear(2021);
        assertEquals(1, year2021Books.size(), "Should find 1 book from 2021");

        // جستجوی ترکیبی
        List<Book> advancedSearch = bookService.searchBooks("Java", "John Doe", 2022);
        assertEquals(1, advancedSearch.size(), "Should find 1 specific book");
    }

    @Test
    void testGetBookById() {
        bookService.addBook("Specific Book", "Specific Author", "444-4444444", 2023, "Specific Pub", 1);
        
        List<Book> allBooks = bookService.getAllBooks();
        String bookId = allBooks.get(0).getBookId();
        
        Optional<Book> foundBook = bookService.getBookById(bookId);
        assertTrue(foundBook.isPresent(), "Book should be found by ID");
        assertEquals("Specific Book", foundBook.get().getTitle(), "Book title should match");
    }

    @Test
    void testUpdateBook() {
        bookService.addBook("Old Title", "Old Author", "555-5555555", 2020, "Old Pub", 2);
        
        List<Book> books = bookService.getAllBooks();
        String bookId = books.get(0).getBookId();
        
        boolean updateResult = bookService.updateBook(bookId, "New Title", "New Author", 
                                                    "555-5555555", 2023, "New Pub", 3);
        assertTrue(updateResult, "Book should be updated successfully");
        
        Optional<Book> updatedBook = bookService.getBookById(bookId);
        assertTrue(updatedBook.isPresent(), "Updated book should exist");
        assertEquals("New Title", updatedBook.get().getTitle(), "Title should be updated");
        assertEquals("New Author", updatedBook.get().getAuthor(), "Author should be updated");
        assertEquals(2023, updatedBook.get().getPublicationYear(), "Year should be updated");
    }

    @Test
    void testBookAvailability() {
        bookService.addBook("Available Book", "Test Author", "666-6666666", 2023, "Test Pub", 2);
        
        List<Book> books = bookService.getAllBooks();
        String bookId = books.get(0).getBookId();
        
        assertTrue(bookService.isBookAvailable(bookId), "Book should be available initially");
        assertEquals(2, bookService.getAvailableCopies(bookId), "Should have 2 available copies");
    }

    @Test
    void testBorrowAndReturnBook() {
        bookService.addBook("Borrowable Book", "Test Author", "777-7777777", 2023, "Test Pub", 2);
        
        List<Book> books = bookService.getAllBooks();
        String bookId = books.get(0).getBookId();
        
        // تست امانت گرفتن
        boolean borrowResult = bookService.borrowBook(bookId);
        assertTrue(borrowResult, "Book should be borrowed successfully");
        assertEquals(1, bookService.getAvailableCopies(bookId), "Should have 1 available copy after borrowing");
        
        // تست بازگرداندن
        boolean returnResult = bookService.returnBook(bookId);
        assertTrue(returnResult, "Book should be returned successfully");
        assertEquals(2, bookService.getAvailableCopies(bookId), "Should have 2 available copies after return");
    }

    @Test
    void testBorrowUnavailableBook() {
        bookService.addBook("Unavailable Book", "Test Author", "888-8888888", 2023, "Test Pub", 0);
        
        List<Book> books = bookService.getAllBooks();
        String bookId = books.get(0).getBookId();
        
        boolean borrowResult = bookService.borrowBook(bookId);
        assertFalse(borrowResult, "Should not be able to borrow unavailable book");
    }

    @Test
    void testGetTotalBooksCount() {
        assertEquals(0, bookService.getTotalBooksCount(), "Initially should have 0 books");
        
        bookService.addBook("Book 1", "Author 1", "999-9999999", 2023, "Pub 1", 1);
        bookService.addBook("Book 2", "Author 2", "000-0000000", 2023, "Pub 2", 1);
        
        assertEquals(2, bookService.getTotalBooksCount(), "Should have 2 books after additions");
    }
}
