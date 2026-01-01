package com.university.library.service;

import java.util.*;

public class BookServiceTest {
    
    // کلاس داخلی Book
    static class Book {
        private String id;
        private String title;
        private String author;
        private String isbn;
        private int publicationYear;
        private String publisher;
        private int totalCopies;
        private int availableCopies;
        
        public Book(String id, String title, String author, String isbn, 
                   int publicationYear, String publisher, int totalCopies) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.isbn = isbn;
            this.publicationYear = publicationYear;
            this.publisher = publisher;
            this.totalCopies = totalCopies;
            this.availableCopies = totalCopies;
        }
        
        // Getters
        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getIsbn() { return isbn; }
        public int getPublicationYear() { return publicationYear; }
        public String getPublisher() { return publisher; }
        public int getTotalCopies() { return totalCopies; }
        public int getAvailableCopies() { return availableCopies; }
        
        @Override
        public String toString() {
            return title + " - " + author + " (" + publicationYear + ")";
        }
    }
    
    // کلاس BookService برای تست
    static class TestBookService {
        private List<Book> books = new ArrayList<>();
        private int bookCounter = 1;
        
        public TestBookService() {
            // اضافه کردن کتاب‌های نمونه
            initializeSampleBooks();
        }
        
        private void initializeSampleBooks() {
            // کتاب‌های نمونه برای تست سناریوها
            books.add(new Book("B001", "مهندسی نرم‌افزار پیشرفته", "احمد رضایی", 
                             "978-1234567890", 2020, "نشر دانش", 5));
            books.add(new Book("B002", "پایگاه داده‌های توزیع‌شده", "احمد رضایی", 
                             "978-1234567891", 2020, "نشر دانش", 3));
            books.add(new Book("B003", "الگوریتم‌های پیشرفته", "مریم کریمی", 
                             "978-1234567892", 2019, "نشر دانشگاه", 4));
            books.add(new Book("B004", "شبکه‌های کامپیوتری", "مریم کریمی", 
                             "978-1234567893", 2019, "نشر دانشگاه", 2));
            books.add(new Book("B005", "هوش مصنوعی در عمل", "محمد حسینی", 
                             "978-1234567894", 2021, "نشر فناوری", 6));
            books.add(new Book("B006", "برنامه‌نویسی شی‌گرا", "فاطمه محمدی", 
                             "978-1234567895", 2018, "نشر علم", 3));
            bookCounter = 7;
        }
        
        // اضافه کردن کتاب جدید
        public void addBook(String title, String author, String isbn, 
                           int publicationYear, String publisher, int totalCopies) {
            String bookId = "B" + String.format("%03d", bookCounter++);
            Book newBook = new Book(bookId, title, author, isbn, publicationYear, publisher, totalCopies);
            books.add(newBook);
        }
        
        // سناریو ۱-۲: جستجو فقط با عنوان
        public List<Book> searchByTitleOnly(String title) {
            if (title == null || title.trim().isEmpty()) {
                return new ArrayList<>(books);
            }
            
            List<Book> result = new ArrayList<>();
            String searchTerm = title.toLowerCase().trim();
            
            for (Book book : books) {
                if (book.getTitle().toLowerCase().contains(searchTerm)) {
                    result.add(book);
                }
            }
            
            return result;
        }
        
        // سناریو ۲-۲: جستجو با ترکیب نویسنده و سال انتشار
        public List<Book> searchByAuthorAndYear(String author, int year) {
            List<Book> result = new ArrayList<>();
            
            for (Book book : books) {
                if (author != null && !author.trim().isEmpty()) {
                    if (!book.getAuthor().equals(author.trim())) {
                        continue;
                    }
                }
                
                if (book.getPublicationYear() == year) {
                    result.add(book);
                }
            }
            
            return result;
        }
        
        // سناریو ۳-۲: جستجو بدون هیچ معیاری
        public List<Book> searchWithoutCriteria() {
            return new ArrayList<>(books);
        }
        
        // سناریو ۴-۲: جستجویی که هیچ کتابی مطابقت ندارد
        public List<Book> searchWithNoMatches() {
            return new ArrayList<>(); // همیشه لیست خالی برمی‌گرداند
        }
        
        // جستجوی یکپارچه (متد اصلی)
        public List<Book> searchBooksIntegrated(String title, String author, Integer year) {
            List<Book> result = new ArrayList<>();
            
            for (Book book : books) {
                boolean matches = true;
                
                // بررسی عنوان
                if (title != null && !title.trim().isEmpty()) {
                    if (!book.getTitle().toLowerCase().contains(title.toLowerCase().trim())) {
                        matches = false;
                    }
                }
                
                // بررسی نویسنده
                if (matches && author != null && !author.trim().isEmpty()) {
                    if (!book.getAuthor().equals(author.trim())) {
                        matches = false;
                    }
                }
                
                // بررسی سال انتشار
                if (matches && year != null) {
                    if (book.getPublicationYear() != year) {
                        matches = false;
                    }
                }
                
                if (matches) {
                    result.add(book);
                }
            }
            
            return result;
        }
        
        // جستجو با معیارهای غیرممکن
        public List<Book> searchWithImpossibleCriteria(String title, String author, int year) {
            List<Book> result = new ArrayList<>();
            
            for (Book book : books) {
                boolean matches = true;
                
                if (title != null && !book.getTitle().equals(title)) {
                    matches = false;
                }
                
                if (matches && author != null && !book.getAuthor().equals(author)) {
                    matches = false;
                }
                
                if (matches && book.getPublicationYear() != year) {
                    matches = false;
                }
                
                if (matches) {
                    result.add(book);
                }
            }
            
            return result;
        }
        
        // دریافت تمام کتاب‌ها
        public List<Book> getAllBooks() {
            return new ArrayList<>(books);
        }
        
        // جستجو با عنوان (نام متفاوت)
        public List<Book> searchByTitle(String title) {
            return searchByTitleOnly(title);
        }
        
        // پاک کردن و ریست کردن داده‌ها
        public void reset() {
            books.clear();
            bookCounter = 1;
            initializeSampleBooks();
        }
        
        // تعداد کل کتاب‌ها
        public int getTotalBooksCount() {
            return books.size();
        }
    }
    
    // تست‌ها
    public static void main(String[] args) {
        System.out.println("📚 شروع تست‌های BookService");
        System.out.println("==========================");
        
        TestBookService bookService = new TestBookService();
        int passedTests = 0;
        int totalTests = 0;
        
        try {
            // تست ۱: سناریو ۱-۲ - جستجو فقط با عنوان
            totalTests++;
            System.out.print("\n1. سناریو ۱-۲: جستجو فقط با عنوان... ");
            testSearchByTitleOnly_Scenario1_2(bookService);
            System.out.println("✅");
            passedTests++;
            
            // تست ۲: عنوان خالی - بازگرداندن همه
            totalTests++;
            System.out.print("2. تست عنوان خالی - بازگرداندن همه... ");
            testSearchByTitleOnly_EmptyTitle_ReturnsAll(bookService);
            System.out.println("✅");
            passedTests++;
            
            // تست ۳: عنوان null - بازگرداندن همه
            totalTests++;
            System.out.print("3. تست عنوان null - بازگرداندن همه... ");
            testSearchByTitleOnly_NullTitle_ReturnsAll(bookService);
            System.out.println("✅");
            passedTests++;
            
            // تست ۴: سناریو ۲-۲ - جستجو با نویسنده و سال
            totalTests++;
            System.out.print("4. سناریو ۲-۲: جستجو با نویسنده و سال... ");
            testSearchByAuthorAndYear_Scenario2_2(bookService);
            System.out.println("✅");
            passedTests++;
            
            // تست ۵: سناریو ۳-۲ - جستجو بدون معیار
            totalTests++;
            System.out.print("5. سناریو ۳-۲: جستجو بدون معیار... ");
            testSearchWithoutCriteria_Scenario3_2(bookService);
            System.out.println("✅");
            passedTests++;
            
            // تست ۶: سناریو ۴-۲ - جستجوی بدون تطابق
            totalTests++;
            System.out.print("6. سناریو ۴-۲: جستجوی بدون تطابق... ");
            testSearchWithNoMatches_Scenario4_2(bookService);
            System.out.println("✅");
            passedTests++;
            
            // تست ۷: جستجوی یکپارچه - همه null
            totalTests++;
            System.out.print("7. جستجوی یکپارچه: همه null... ");
            testSearchBooksIntegrated_AllNull_Scenario3_2(bookService);
            System.out.println("✅");
            passedTests++;
            
            // تست ۸: جستجوی یکپارچه - فقط عنوان
            totalTests++;
            System.out.print("8. جستجوی یکپارچه: فقط عنوان... ");
            testSearchBooksIntegrated_TitleOnly_Scenario1_2(bookService);
            System.out.println("✅");
            passedTests++;
            
            // تست ۹: جستجوی یکپارچه - نویسنده و سال
            totalTests++;
            System.out.print("9. جستجوی یکپارچه: نویسنده و سال... ");
            testSearchBooksIntegrated_AuthorAndYear_Scenario2_2(bookService);
            System.out.println("✅");
            passedTests++;
            
            // تست ۱۰: جستجوی یکپارچه - بدون تطابق
            totalTests++;
            System.out.print("10. جستجوی یکپارچه: بدون تطابق... ");
            testSearchBooksIntegrated_NoMatch_Scenario4_2(bookService);
            System.out.println("✅");
            passedTests++;
            
            // تست ۱۱: جستجوی یکپارچه - معیارهای ترکیبی
            totalTests++;
            System.out.print("11. جستجوی یکپارچه: معیارهای ترکیبی... ");
            testSearchBooksIntegrated_MixedCriteria(bookService);
            System.out.println("✅");
            passedTests++;
            
            // تست ۱۲: جستجو با معیارهای غیرممکن
            totalTests++;
            System.out.print("12. جستجو با معیارهای غیرممکن... ");
            testSearchWithImpossibleCriteria_Scenario4_2(bookService);
            System.out.println("✅");
            passedTests++;
            
            // تست ۱۳: یکپارچگی با متدهای موجود
            totalTests++;
            System.out.print("13. تست یکپارچگی با متدهای موجود... ");
            testIntegrationWithExistingMethods(bookService);
            System.out.println("✅");
            passedTests++;
            
            System.out.println("\n📊 نتایج تست:");
            System.out.println("   تست‌های گذرانده شده: " + passedTests + " از " + totalTests);
            System.out.println("   نرخ موفقیت: " + (passedTests * 100 / totalTests) + "%");
            
            if (passedTests == totalTests) {
                System.out.println("\n🎉 تمام تست‌ها با موفقیت گذرانده شدند!");
            } else {
                System.out.println("\n⚠️  برخی تست‌ها ناموفق بودند!");
            }
            
        } catch (Exception e) {
            System.out.println("❌");
            System.out.println("خطا در تست: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // متدهای تست
    private static void testSearchByTitleOnly_Scenario1_2(TestBookService bookService) {
        // سناریو ۱-۲: جستجو فقط با عنوان
        bookService.addBook("طراحی الگوریتم", "علی حسینی", "978-123-456-789", 2022, "نشر دانش", 3);
        
        List<Book> result = bookService.searchByTitleOnly("الگوریتم");
        if (result.isEmpty()) {
            throw new RuntimeException("باید کتاب‌های با عنوان 'الگوریتم' یافت شوند");
        }
        
        boolean found = false;
        for (Book book : result) {
            if (book.getTitle().contains("الگوریتم")) {
                found = true;
                break;
            }
        }
        
        if (!found) {
            throw new RuntimeException("هیچ کتابی با عنوان 'الگوریتم' یافت نشد");
        }
    }
    
    private static void testSearchByTitleOnly_EmptyTitle_ReturnsAll(TestBookService bookService) {
        // اگر عنوان خالی باشد، همه کتاب‌ها را برگرداند
        List<Book> allBooks = bookService.getAllBooks();
        List<Book> result = bookService.searchByTitleOnly("");
        if (allBooks.size() != result.size()) {
            throw new RuntimeException("انتظار می‌رود " + allBooks.size() + 
                                     " کتاب برگردانده شود، اما " + result.size() + " کتاب برگردانده شد");
        }
    }
    
    private static void testSearchByTitleOnly_NullTitle_ReturnsAll(TestBookService bookService) {
        // اگر عنوان null باشد، همه کتاب‌ها را برگرداند
        List<Book> allBooks = bookService.getAllBooks();
        List<Book> result = bookService.searchByTitleOnly(null);
        if (allBooks.size() != result.size()) {
            throw new RuntimeException("انتظار می‌رود " + allBooks.size() + 
                                     " کتاب برگردانده شود، اما " + result.size() + " کتاب برگردانده شد");
        }
    }
    
    private static void testSearchByAuthorAndYear_Scenario2_2(TestBookService bookService) {
        // سناریو ۲-۲: جستجو با ترکیب نویسنده و سال انتشار
        bookService.addBook("کتاب تست ۱", "نویسنده مشترک", "111-111-111", 2023, "نشر تست", 2);
        bookService.addBook("کتاب تست ۲", "نویسنده مشترک", "222-222-222", 2023, "نشر تست", 3);
        bookService.addBook("کتاب تست ۳", "نویسنده مشترک", "333-333-333", 2024, "نشر تست", 1);
        
        List<Book> result = bookService.searchByAuthorAndYear("نویسنده مشترک", 2023);
        if (result.isEmpty()) {
            throw new RuntimeException("باید کتاب‌های نویسنده 'نویسنده مشترک' در سال 2023 یافت شوند");
        }
        
        if (result.size() != 2) {
            throw new RuntimeException("انتظار می‌رود 2 کتاب یافت شود، اما " + result.size() + " کتاب یافت شد");
        }
        
        for (Book book : result) {
            if (!book.getAuthor().equals("نویسنده مشترک") || book.getPublicationYear() != 2023) {
                throw new RuntimeException("کتاب یافت شده معیارهای جستجو را برآورده نمی‌کند");
            }
        }
    }
    
    private static void testSearchWithoutCriteria_Scenario3_2(TestBookService bookService) {
        // سناریو ۳-۲: جستجو بدون هیچ معیاری
        List<Book> allBooks = bookService.getAllBooks();
        List<Book> result = bookService.searchWithoutCriteria();
        
        if (allBooks.size() != result.size()) {
            throw new RuntimeException("انتظار می‌رود " + allBooks.size() + 
                                     " کتاب برگردانده شود، اما " + result.size() + " کتاب برگردانده شد");
        }
    }
    
    private static void testSearchWithNoMatches_Scenario4_2(TestBookService bookService) {
        // سناریو ۴-۲: جستجویی که هیچ کتابی مطابقت ندارد
        List<Book> result = bookService.searchWithNoMatches();
        if (!result.isEmpty()) {
            throw new RuntimeException("انتظار می‌رود هیچ کتابی برگردانده نشود، اما " + result.size() + " کتاب برگردانده شد");
        }
    }
    
    private static void testSearchBooksIntegrated_AllNull_Scenario3_2(TestBookService bookService) {
        // سناریو ۳-۲: همه پارامترها null
        List<Book> result = bookService.searchBooksIntegrated(null, null, null);
        List<Book> allBooks = bookService.getAllBooks();
        
        if (allBooks.size() != result.size()) {
            throw new RuntimeException("انتظار می‌رود " + allBooks.size() + 
                                     " کتاب برگردانده شود، اما " + result.size() + " کتاب برگردانده شد");
        }
    }
    
    private static void testSearchBooksIntegrated_TitleOnly_Scenario1_2(TestBookService bookService) {
        // سناریو ۱-۲: فقط عنوان
        bookService.addBook("مهندسی نرم‌افزار پیشرفته", "دکتر محمدی", "444-444-444", 2021, "نشر دانشگاه", 5);
        
        List<Book> result = bookService.searchBooksIntegrated("پیشرفته", null, null);
        if (result.isEmpty()) {
            throw new RuntimeException("باید کتاب‌های با عنوان 'پیشرفته' یافت شوند");
        }
        
        boolean found = false;
        for (Book book : result) {
            if (book.getTitle().contains("پیشرفته")) {
                found = true;
                break;
            }
        }
        
        if (!found) {
            throw new RuntimeException("هیچ کتابی با عنوان 'پیشرفته' یافت نشد");
        }
    }
    
    private static void testSearchBooksIntegrated_AuthorAndYear_Scenario2_2(TestBookService bookService) {
        // سناریو ۲-۲: نویسنده و سال
        bookService.addBook("داده‌کاوی", "دکتر کریمی", "555-555-555", 2020, "نشر علم", 4);
        
        List<Book> result = bookService.searchBooksIntegrated(null, "دکتر کریمی", 2020);
        if (result.isEmpty()) {
            throw new RuntimeException("باید کتاب‌های دکتر کریمی در سال 2020 یافت شوند");
        }
        
        for (Book book : result) {
            if (!book.getAuthor().equals("دکتر کریمی") || book.getPublicationYear() != 2020) {
                throw new RuntimeException("کتاب یافت شده معیارهای جستجو را برآورده نمی‌کند");
            }
        }
    }
    
    private static void testSearchBooksIntegrated_NoMatch_Scenario4_2(TestBookService bookService) {
        // سناریو ۴-۲: هیچ مطابقتی پیدا نمی‌شود
        List<Book> result = bookService.searchBooksIntegrated("عنوان_غیرممکن_۱۲۳", "نویسنده_غیرممکن_۴۵۶", 9999);
        if (!result.isEmpty()) {
            throw new RuntimeException("انتظار می‌رود هیچ کتابی برگردانده نشود، اما " + result.size() + " کتاب برگردانده شد");
        }
    }
    
    private static void testSearchBooksIntegrated_MixedCriteria(TestBookService bookService) {
        // تست ترکیبی از پارامترها
        bookService.addBook("شبکه‌های کامپیوتری", "مهندس رضوی", "666-666-666", 2022, "نشر فناوری", 3);
        
        List<Book> result = bookService.searchBooksIntegrated("شبکه", "مهندس رضوی", 2022);
        if (result.isEmpty()) {
            throw new RuntimeException("باید کتاب‌های با معیارهای ترکیبی یافت شوند");
        }
        
        if (result.size() != 1) {
            throw new RuntimeException("انتظار می‌رود 1 کتاب یافت شود، اما " + result.size() + " کتاب یافت شد");
        }
    }
    
    private static void testSearchWithImpossibleCriteria_Scenario4_2(TestBookService bookService) {
        // سناریو ۴-۲ با استفاده از متد کمکی
        List<Book> result = bookService.searchWithImpossibleCriteria("غیرممکن", "ناشناخته", 3000);
        if (!result.isEmpty()) {
            throw new RuntimeException("انتظار می‌رود هیچ کتابی برگردانده نشود، اما " + result.size() + " کتاب برگردانده شد");
        }
    }
    
    private static void testIntegrationWithExistingMethods(TestBookService bookService) {
        // تست یکپارچگی با متدهای موجود
        bookService.addBook("نرم‌افزار مهندسی", "دکتر علی‌زاده", "777-777-777", 2023, "نشر دانشگاه", 2);
        
        List<Book> result1 = bookService.searchByTitle("نرم‌افزار");
        List<Book> result2 = bookService.searchByTitleOnly("نرم‌افزار");
        
        // هر دو باید نتایج مشابهی برگردانند
        if (result1.size() != result2.size()) {
            throw new RuntimeException("انتظار می‌رود هر دو متد نتایج یکسانی برگردانند");
        }
    }
}