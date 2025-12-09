package com.university.library.service;

import com.university.library.model.Book;
import com.university.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class BookServiceTest {
    
    private BookService bookService;
    private BookRepository bookRepository;
    
    @BeforeEach
    void setUp() {
        bookService = new BookService();
        bookRepository = BookRepository.getInstance();
        
        // پاک کردن و بازنشانی داده‌های تست
        // در صورت نیاز می‌توانید از reflection برای دسترسی به لیست books در Repository استفاده کنید
        // یا یک متد reset در Repository اضافه کنید
    }
    
    @Test
    void testSearchByTitleOnly_Scenario1_2() {
        // سناریو 1-2: جستجو فقط با عنوان
        // اضافه کردن کتاب برای تست
        bookService.addBook("طراحی الگوریتم", "علی حسینی", "978-123-456-789", 2022, "نشر دانش", 3);
        
        List<Book> result = bookService.searchByTitleOnly("الگوریتم");
        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(book -> book.getTitle().contains("الگوریتم")));
    }
    
    @Test
    void testSearchByTitleOnly_EmptyTitle_ReturnsAll() {
        // اگر عنوان خالی باشد، همه کتاب‌ها را برگرداند
        List<Book> allBooks = bookService.getAllBooks();
        List<Book> result = bookService.searchByTitleOnly("");
        assertEquals(allBooks.size(), result.size());
    }
    
    @Test
    void testSearchByTitleOnly_NullTitle_ReturnsAll() {
        // اگر عنوان null باشد، همه کتاب‌ها را برگرداند
        List<Book> allBooks = bookService.getAllBooks();
        List<Book> result = bookService.searchByTitleOnly(null);
        assertEquals(allBooks.size(), result.size());
    }
    
    @Test
    void testSearchByAuthorAndYear_Scenario2_2() {
        // سناریو 2-2: جستجو با ترکیب نویسنده و سال انتشار
        // اضافه کردن کتاب‌های تست
        bookService.addBook("کتاب تست 1", "نویسنده مشترک", "111-111-111", 2023, "نشر تست", 2);
        bookService.addBook("کتاب تست 2", "نویسنده مشترک", "222-222-222", 2023, "نشر تست", 3);
        bookService.addBook("کتاب تست 3", "نویسنده مشترک", "333-333-333", 2024, "نشر تست", 1);
        
        List<Book> result = bookService.searchByAuthorAndYear("نویسنده مشترک", 2023);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size()); // باید ۲ کتاب از این نویسنده در سال 2023 داشته باشیم
        assertTrue(result.stream().allMatch(book -> 
            book.getAuthor().equals("نویسنده مشترک") && book.getPublicationYear() == 2023
        ));
    }
    
    @Test
    void testSearchWithoutCriteria_Scenario3_2() {
        // سناریو 3-2: جستجو بدون هیچ معیاری
        List<Book> allBooks = bookService.getAllBooks();
        List<Book> result = bookService.searchWithoutCriteria();
        
        assertEquals(allBooks.size(), result.size());
        assertFalse(result.isEmpty());
    }
    
    @Test
    void testSearchWithNoMatches_Scenario4_2() {
        // سناریو 4-2: جستجویی که هیچ کتابی مطابقت ندارد
        List<Book> result = bookService.searchWithNoMatches();
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
    }
    
    @Test
    void testSearchBooksIntegrated_AllNull_Scenario3_2() {
        // سناریو 3-2: همه پارامترها null
        List<Book> result = bookService.searchBooksIntegrated(null, null, null);
        List<Book> allBooks = bookService.getAllBooks();
        
        assertEquals(allBooks.size(), result.size());
        assertFalse(result.isEmpty());
    }
    
    @Test
    void testSearchBooksIntegrated_TitleOnly_Scenario1_2() {
        // سناریو 1-2: فقط عنوان
        bookService.addBook("مهندسی نرم‌افزار پیشرفته", "دکتر محمدی", "444-444-444", 2021, "نشر دانشگاه", 5);
        
        List<Book> result = bookService.searchBooksIntegrated("پیشرفته", null, null);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(book -> book.getTitle().contains("پیشرفته")));
    }
    
    @Test
    void testSearchBooksIntegrated_AuthorAndYear_Scenario2_2() {
        // سناریو 2-2: نویسنده و سال
        bookService.addBook("داده‌کاوی", "دکتر کریمی", "555-555-555", 2020, "نشر علم", 4);
        
        List<Book> result = bookService.searchBooksIntegrated(null, "دکتر کریمی", 2020);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(book -> 
            book.getAuthor().equals("دکتر کریمی") && book.getPublicationYear() == 2020
        ));
    }
    
    @Test
    void testSearchBooksIntegrated_NoMatch_Scenario4_2() {
        // سناریو 4-2: هیچ مطابقتی پیدا نمی‌شود
        List<Book> result = bookService.searchBooksIntegrated("عنوان_غیرممکن_۱۲۳", "نویسنده_غیرممکن_۴۵۶", 9999);
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testSearchBooksIntegrated_MixedCriteria() {
        // تست ترکیبی از پارامترها
        bookService.addBook("شبکه‌های کامپیوتری", "مهندس رضوی", "666-666-666", 2022, "نشر فناوری", 3);
        
        List<Book> result = bookService.searchBooksIntegrated("شبکه", "مهندس رضوی", 2022);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
    
    @Test
    void testSearchWithImpossibleCriteria_Scenario4_2() {
        // سناریو 4-2 با استفاده از متد کمکی
        List<Book> result = bookService.searchWithImpossibleCriteria("غیرممکن", "ناشناخته", 3000);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
    }
    
    @Test
    void testIntegrationWithExistingMethods() {
        // تست یکپارچگی با متدهای موجود
        List<Book> result1 = bookService.searchByTitle("نرم‌افزار");
        List<Book> result2 = bookService.searchByTitleOnly("نرم‌افزار");
        
        // هر دو باید نتایج مشابهی برگردانند
        assertEquals(result1.size(), result2.size());
    }
}