package com.university.library.repository;

import com.university.library.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class BookRepositoryTest {
    
    private BookRepository bookRepository;
    
    @BeforeEach
    void setUp() {
        // ریست کردن Singleton برای تست
        bookRepository = BookRepository.getInstance();
        // پاک کردن داده‌های قبلی و اضافه کردن داده‌های تست
        // برای این کار نیاز به متد reset داریم، یا از reflection استفاده کنیم
        // برای سادگی، از همان instance استفاده می‌کنیم
    }
    
    @Test
    void testSearchByTitleOnly_Scenario1_2() {
        // سناریو 1-2: جستجو فقط با عنوان
        List<Book> result = bookRepository.searchByTitleOnly("نرم‌افزار");
        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(book -> book.getTitle().contains("نرم‌افزار")));
    }
    
    @Test
    void testSearchByTitleOnly_EmptyTitle_ReturnsAll() {
        // اگر عنوان خالی باشد، همه کتاب‌ها را برگرداند
        List<Book> result = bookRepository.searchByTitleOnly("");
        assertFalse(result.isEmpty());
        assertEquals(5, result.size()); // 5 کتاب در داده‌های نمونه داریم
    }
    
    @Test
    void testSearchByAuthorAndYear_Scenario2_2() {
        // سناریو 2-2: جستجو با ترکیب نویسنده و سال انتشار
        List<Book> result = bookRepository.searchByAuthorAndYear("احمد رضایی", 2020);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size()); // 2 کتاب از احمد رضایی در سال 2020 داریم
        assertTrue(result.stream().allMatch(book -> 
            book.getAuthor().equals("احمد رضایی") && book.getPublicationYear() == 2020
        ));
    }
    
    @Test
    void testSearchWithoutCriteria_Scenario3_2() {
        // سناریو 3-2: جستجو بدون هیچ معیاری
        List<Book> result = bookRepository.searchWithoutCriteria();
        assertFalse(result.isEmpty());
        assertEquals(5, result.size()); // همه 5 کتاب
    }
    
    @Test
    void testSearchWithNoMatches_Scenario4_2() {
        // سناریو 4-2: جستجویی که هیچ کتابی مطابقت ندارد
        List<Book> result = bookRepository.searchWithNoMatches("هیچ‌کتابی‌بااین‌عنوان");
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
    }
    
    @Test
    void testSearchBooksIntegrated_AllNull_Scenario3_2() {
        // سناریو 3-2: همه پارامترها null
        List<Book> result = bookRepository.searchBooksIntegrated(null, null, null);
        assertFalse(result.isEmpty());
        assertEquals(5, result.size());
    }
    
    @Test
    void testSearchBooksIntegrated_TitleOnly_Scenario1_2() {
        // سناریو 1-2: فقط عنوان
        List<Book> result = bookRepository.searchBooksIntegrated("پایگاه", null, null);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(book -> book.getTitle().contains("پایگاه")));
    }
    
    @Test
    void testSearchBooksIntegrated_AuthorAndYear_Scenario2_2() {
        // سناریو 2-2: نویسنده و سال
        List<Book> result = bookRepository.searchBooksIntegrated(null, "مریم کریمی", 2019);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size()); // 2 کتاب از مریم کریمی در سال 2019
    }
    
    @Test
    void testSearchBooksIntegrated_NoMatch_Scenario4_2() {
        // سناریو 4-2: هیچ مطابقتی پیدا نمی‌شود
        List<Book> result = bookRepository.searchBooksIntegrated("هیچ‌کتابی", "نویسنده‌ناشناس", 9999);
        assertTrue(result.isEmpty());
    }
}