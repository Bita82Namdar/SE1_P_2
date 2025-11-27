package com.university.library.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

    @Test
    void testBookCreation() {
        Book book = new Book("B1", "Test Book", "Test Author", "123-4567890", 2023, "Test Publisher", 5);
        
        assertEquals("B1", book.getBookId());
        assertEquals("Test Book", book.getTitle());
        assertEquals("Test Author", book.getAuthor());
        assertEquals("123-4567890", book.getIsbn());
        assertEquals(2023, book.getPublicationYear());
        assertEquals("Test Publisher", book.getPublisher());
        assertEquals(5, book.getTotalCopies());
        assertEquals(5, book.getAvailableCopies());
        assertTrue(book.isAvailable());
    }

    @Test
    void testBorrowAndReturn() {
        Book book = new Book("B2", "Test Book 2", "Author 2", "123-4567891", 2023, "Publisher", 3);
        
        // تست امانت گرفتن
        assertTrue(book.isAvailable());
        book.borrowCopy();
        assertEquals(2, book.getAvailableCopies());
        book.borrowCopy();
        assertEquals(1, book.getAvailableCopies());
        book.borrowCopy();
        assertEquals(0, book.getAvailableCopies());
        assertFalse(book.isAvailable());
        
        // تست بازگرداندن
        book.returnCopy();
        assertEquals(1, book.getAvailableCopies());
        assertTrue(book.isAvailable());
    }

    @Test
    void testToString() {
        Book book = new Book("B3", "Java Programming", "Java Author", "123-4567892", 2023, "Java Pub", 2);
        String toString = book.toString();
        
        assertTrue(toString.contains("Java Programming"));
        assertTrue(toString.contains("Java Author"));
        assertTrue(toString.contains("2023"));
        assertTrue(toString.contains("2/2")); // available/total
    }
}