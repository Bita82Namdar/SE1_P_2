package com.university.library.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

    @Test
    void testBookCreation() {
        // ایجاد کتاب با constructor واقعی
        Book book = new Book("B001", "Java Programming", "James Gosling", 2023);
        
        // تست مقادیر اولیه
        assertEquals("B001", book.getBookId());
        assertEquals("Java Programming", book.getTitle());
        assertEquals("James Gosling", book.getAuthor());
        assertEquals(2023, book.getPublicationYear());
        assertTrue(book.isAvailable()); // باید به طور پیش‌فرض available باشد
    }

    @Test
    void testSettersAndGetters() {
        // ایجاد کتاب
        Book book = new Book("B001", "Initial Title", "Initial Author", 2000);
        
        // تست setterها
        book.setBookId("B002");
        assertEquals("B002", book.getBookId());
        
        book.setTitle("New Title");
        assertEquals("New Title", book.getTitle());
        
        book.setAuthor("New Author");
        assertEquals("New Author", book.getAuthor());
        
        book.setPublicationYear(2024);
        assertEquals(2024, book.getPublicationYear());
        
        book.setAvailable(false);
        assertFalse(book.isAvailable());
        
        book.setAvailable(true);
        assertTrue(book.isAvailable());
    }

    @Test
    void testToString() {
        Book book = new Book("B003", "Test Book", "Test Author", 2023);
        String toString = book.toString();
        
        // تست اینکه toString اطلاعات مهم را شامل می‌شود
        assertTrue(toString.contains("B003"));
        assertTrue(toString.contains("Test Book"));
        assertTrue(toString.contains("Test Author"));
        assertTrue(toString.contains("available=true"));
    }
    
    @Test
    void testBookAvailabilityToggle() {
        Book book = new Book("B004", "Toggle Book", "Toggle Author", 2022);
        
        // تست تغییر وضعیت availability
        assertTrue(book.isAvailable());
        
        book.setAvailable(false);
        assertFalse(book.isAvailable());
        
        book.setAvailable(true);
        assertTrue(book.isAvailable());
    }
    
    @Test
    void testMultipleBooks() {
        // تست ایجاد چندین کتاب
        Book book1 = new Book("B101", "Book One", "Author One", 2021);
        Book book2 = new Book("B102", "Book Two", "Author Two", 2022);
        Book book3 = new Book("B103", "Book Three", "Author Three", 2023);
        
        assertEquals("B101", book1.getBookId());
        assertEquals("Book Two", book2.getTitle());
        assertEquals("Author Three", book3.getAuthor());
        assertEquals(2023, book3.getPublicationYear());
        
        // تست اینکه همه به طور پیش‌فرض available هستند
        assertTrue(book1.isAvailable());
        assertTrue(book2.isAvailable());
        assertTrue(book3.isAvailable());
    }
    
    @Test
    void testBookWithDifferentYears() {
        // تست کتاب‌ها با سال‌های انتشار مختلف
        Book oldBook = new Book("OLD001", "Old Book", "Old Author", 1990);
        Book newBook = new Book("NEW001", "New Book", "New Author", 2024);
        
        assertEquals(1990, oldBook.getPublicationYear());
        assertEquals(2024, newBook.getPublicationYear());
        
        // تست availability
        assertTrue(oldBook.isAvailable());
        assertTrue(newBook.isAvailable());
        
        // غیرفعال کردن یکی
        oldBook.setAvailable(false);
        assertFalse(oldBook.isAvailable());
        assertTrue(newBook.isAvailable());
    }
    
    @Test
    void testBookEquality() {
        Book book1 = new Book("B201", "Same Book", "Same Author", 2023);
        Book book2 = new Book("B201", "Same Book", "Same Author", 2023);
        Book book3 = new Book("B202", "Different Book", "Different Author", 2024);
        
        // توجه: دو کتاب حتی با مقادیر یکسان، شیءهای مختلفی هستند
        // مگر اینکه equals() را override کرده باشید
        assertNotSame(book1, book2); // شیءهای مختلف
        assertNotSame(book1, book3);
        
        // تست مقایسه محتوا
        assertEquals(book1.getBookId(), book2.getBookId());
        assertEquals(book1.getTitle(), book2.getTitle());
        assertEquals(book1.getAuthor(), book2.getAuthor());
        assertEquals(book1.getPublicationYear(), book2.getPublicationYear());
        assertEquals(book1.isAvailable(), book2.isAvailable());
        
        // کتاب سوم باید متفاوت باشد
        assertNotEquals(book1.getBookId(), book3.getBookId());
    }
}