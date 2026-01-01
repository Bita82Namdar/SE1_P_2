package com.university.library.repository;

import java.util.*;

public class BookRepositoryTest {
    
    // کلاس داخلی Book برای تست
    static class Book {
        private String id;
        private String title;
        private String author;
        private int publicationYear;
        private String isbn;
        private boolean available;
        
        public Book(String id, String title, String author, int publicationYear, String isbn) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.publicationYear = publicationYear;
            this.isbn = isbn;
            this.available = true;
        }
        
        // Getters
        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public int getPublicationYear() { return publicationYear; }
        public String getIsbn() { return isbn; }
        public boolean isAvailable() { return available; }
        
        @Override
        public String toString() {
            return title + " - " + author + " (" + publicationYear + ")";
        }
    }
    
    // کلاس BookRepository برای تست
    static class TestBookRepository {
        private List<Book> books = new ArrayList<>();
        
        public TestBookRepository() {
            // اضافه کردن کتاب‌های نمونه برای تست سناریوها
            books.add(new Book("B001", "مهندسی نرم‌افزار پیشرفته", "احمد رضایی", 2020, "978-1234567890"));
            books.add(new Book("B002", "پایگاه داده‌های توزیع‌شده", "احمد رضایی", 2020, "978-1234567891"));
            books.add(new Book("B003", "الگوریتم‌های پیشرفته", "مریم کریمی", 2019, "978-1234567892"));
            books.add(new Book("B004", "شبکه‌های کامپیوتری", "مریم کریمی", 2019, "978-1234567893"));
            books.add(new Book("B005", "هوش مصنوعی در عمل", "محمد حسینی", 2021, "978-1234567894"));
            books.add(new Book("B006", "برنامه‌نویسی شی‌گرا", "فاطمه محمدی", 2018, "978-1234567895"));
            books.add(new Book("B007", "امنیت نرم‌افزار", "علی اکبری", 2022, "978-1234567896"));
        }
        
        // سناریو ۱-۲: جستجو فقط با عنوان
        public List<Book> searchByTitleOnly(String title) {
            if (title == null || title.trim().isEmpty()) {
                return new ArrayList<>(books); // بازگرداندن همه کتاب‌ها
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
                    if (!book.getAuthor().equals(author)) {
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
        public List<Book> searchWithNoMatches(String impossibleTitle) {
            List<Book> result = new ArrayList<>();
            
            for (Book book : books) {
                if (book.getTitle().contains(impossibleTitle)) {
                    result.add(book);
                }
            }
            
            return result;
        }
        
        // متد یکپارچه برای جستجو
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
        
        // متد کمکی: دریافت تعداد کل کتاب‌ها
        public int getTotalBooksCount() {
            return books.size();
        }
        
        // متد کمکی: پاک کردن و ریست کردن داده‌ها
        public void reset() {
            books.clear();
            // اضافه کردن مجدد کتاب‌های نمونه
            books.add(new Book("B001", "مهندسی نرم‌افزار پیشرفته", "احمد رضایی", 2020, "978-1234567890"));
            books.add(new Book("B002", "پایگاه داده‌های توزیع‌شده", "احمد رضایی", 2020, "978-1234567891"));
            books.add(new Book("B003", "الگوریتم‌های پیشرفته", "مریم کریمی", 2019, "978-1234567892"));
            books.add(new Book("B004", "شبکه‌های کامپیوتری", "مریم کریمی", 2019, "978-1234567893"));
            books.add(new Book("B005", "هوش مصنوعی در عمل", "محمد حسینی", 2021, "978-1234567894"));
            books.add(new Book("B006", "برنامه‌نویسی شی‌گرا", "فاطمه محمدی", 2018, "978-1234567895"));
            books.add(new Book("B007", "امنیت نرم‌افزار", "علی اکبری", 2022, "978-1234567896"));
        }
    }
    
    // تست‌ها
    public static void main(String[] args) {
        System.out.println("🧪 شروع تست‌های BookRepository");
        System.out.println("==============================");
        
        TestBookRepository repository = new TestBookRepository();
        int passedTests = 0;
        int totalTests = 0;
        
        try {
            totalTests++;
            System.out.print("\n📚 تست ۱: سناریو ۱-۲ (جستجو فقط با عنوان)... ");
            testSearchByTitleOnly(repository);
            System.out.println("✅");
            passedTests++;
            
            totalTests++;
            System.out.print("📚 تست ۲: سناریو ۱-۲ (عنوان خالی)... ");
            testSearchByTitleOnly_EmptyTitle(repository);
            System.out.println("✅");
            passedTests++;
            
            totalTests++;
            System.out.print("📚 تست ۳: سناریو ۲-۲ (نویسنده و سال)... ");
            testSearchByAuthorAndYear(repository);
            System.out.println("✅");
            passedTests++;
            
            totalTests++;
            System.out.print("📚 تست ۴: سناریو ۳-۲ (بدون معیار)... ");
            testSearchWithoutCriteria(repository);
            System.out.println("✅");
            passedTests++;
            
            totalTests++;
            System.out.print("📚 تست ۵: سناریو ۴-۲ (بدون تطابق)... ");
            testSearchWithNoMatches(repository);
            System.out.println("✅");
            passedTests++;
            
            totalTests++;
            System.out.print("📚 تست ۶: جستجوی یکپارچه (همه null)... ");
            testSearchBooksIntegrated_AllNull(repository);
            System.out.println("✅");
            passedTests++;
            
            totalTests++;
            System.out.print("📚 تست ۷: جستجوی یکپارچه (فقط عنوان)... ");
            testSearchBooksIntegrated_TitleOnly(repository);
            System.out.println("✅");
            passedTests++;
            
            totalTests++;
            System.out.print("📚 تست ۸: جستجوی یکپارچه (نویسنده و سال)... ");
            testSearchBooksIntegrated_AuthorAndYear(repository);
            System.out.println("✅");
            passedTests++;
            
            totalTests++;
            System.out.print("📚 تست ۹: جستجوی یکپارچه (بدون تطابق)... ");
            testSearchBooksIntegrated_NoMatch(repository);
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
    private static void testSearchByTitleOnly(TestBookRepository repository) {
        // سناریو ۱-۲: جستجو فقط با عنوان
        List<Book> result = repository.searchByTitleOnly("نرم‌افزار");
        if (result.isEmpty()) {
            throw new RuntimeException("انتظار می‌رود کتاب‌های با عنوان 'نرم‌افزار' یافت شوند");
        }
        
        boolean found = false;
        for (Book book : result) {
            if (book.getTitle().contains("نرم‌افزار")) {
                found = true;
                break;
            }
        }
        
        if (!found) {
            throw new RuntimeException("هیچ کتابی با عنوان 'نرم‌افزار' یافت نشد");
        }
    }
    
    private static void testSearchByTitleOnly_EmptyTitle(TestBookRepository repository) {
        // اگر عنوان خالی باشد، همه کتاب‌ها را برگرداند
        List<Book> result = repository.searchByTitleOnly("");
        if (result.isEmpty()) {
            throw new RuntimeException("انتظار می‌رود همه کتاب‌ها برگردانده شوند");
        }
        
        if (result.size() != repository.getTotalBooksCount()) {
            throw new RuntimeException("انتظار می‌رود " + repository.getTotalBooksCount() + 
                                     " کتاب برگردانده شود، اما " + result.size() + " کتاب برگردانده شد");
        }
    }
    
    private static void testSearchByAuthorAndYear(TestBookRepository repository) {
        // سناریو ۲-۲: جستجو با ترکیب نویسنده و سال انتشار
        List<Book> result = repository.searchByAuthorAndYear("احمد رضایی", 2020);
        if (result.isEmpty()) {
            throw new RuntimeException("انتظار می‌رود کتاب‌های احمد رضایی در سال 2020 یافت شوند");
        }
        
        if (result.size() != 2) {
            throw new RuntimeException("انتظار می‌رود 2 کتاب یافت شود، اما " + result.size() + " کتاب یافت شد");
        }
        
        for (Book book : result) {
            if (!book.getAuthor().equals("احمد رضایی") || book.getPublicationYear() != 2020) {
                throw new RuntimeException("کتاب یافت شده معیارهای جستجو را برآورده نمی‌کند");
            }
        }
    }
    
    private static void testSearchWithoutCriteria(TestBookRepository repository) {
        // سناریو ۳-۲: جستجو بدون هیچ معیاری
        List<Book> result = repository.searchWithoutCriteria();
        if (result.isEmpty()) {
            throw new RuntimeException("انتظار می‌رود همه کتاب‌ها برگردانده شوند");
        }
        
        if (result.size() != repository.getTotalBooksCount()) {
            throw new RuntimeException("انتظار می‌رود " + repository.getTotalBooksCount() + 
                                     " کتاب برگردانده شود، اما " + result.size() + " کتاب برگردانده شد");
        }
    }
    
    private static void testSearchWithNoMatches(TestBookRepository repository) {
        // سناریو ۴-۲: جستجویی که هیچ کتابی مطابقت ندارد
        List<Book> result = repository.searchWithNoMatches("هیچ‌کتابی‌بااین‌عنوان");
        if (!result.isEmpty()) {
            throw new RuntimeException("انتظار می‌رود هیچ کتابی یافت نشود، اما " + result.size() + " کتاب یافت شد");
        }
    }
    
    private static void testSearchBooksIntegrated_AllNull(TestBookRepository repository) {
        // سناریو ۳-۲: همه پارامترها null
        List<Book> result = repository.searchBooksIntegrated(null, null, null);
        if (result.isEmpty()) {
            throw new RuntimeException("انتظار می‌رود همه کتاب‌ها برگردانده شوند");
        }
        
        if (result.size() != repository.getTotalBooksCount()) {
            throw new RuntimeException("انتظار می‌رود " + repository.getTotalBooksCount() + 
                                     " کتاب برگردانده شود، اما " + result.size() + " کتاب برگردانده شد");
        }
    }
    
    private static void testSearchBooksIntegrated_TitleOnly(TestBookRepository repository) {
        // سناریو ۱-۲: فقط عنوان
        List<Book> result = repository.searchBooksIntegrated("پایگاه", null, null);
        if (result.isEmpty()) {
            throw new RuntimeException("انتظار می‌رود کتاب‌های با عنوان 'پایگاه' یافت شوند");
        }
        
        boolean found = false;
        for (Book book : result) {
            if (book.getTitle().contains("پایگاه")) {
                found = true;
                break;
            }
        }
        
        if (!found) {
            throw new RuntimeException("هیچ کتابی با عنوان 'پایگاه' یافت نشد");
        }
    }
    
    private static void testSearchBooksIntegrated_AuthorAndYear(TestBookRepository repository) {
        // سناریو ۲-۲: نویسنده و سال
        List<Book> result = repository.searchBooksIntegrated(null, "مریم کریمی", 2019);
        if (result.isEmpty()) {
            throw new RuntimeException("انتظار می‌رود کتاب‌های مریم کریمی در سال 2019 یافت شوند");
        }
        
        if (result.size() != 2) {
            throw new RuntimeException("انتظار می‌رود 2 کتاب یافت شود، اما " + result.size() + " کتاب یافت شد");
        }
        
        for (Book book : result) {
            if (!book.getAuthor().equals("مریم کریمی") || book.getPublicationYear() != 2019) {
                throw new RuntimeException("کتاب یافت شده معیارهای جستجو را برآورده نمی‌کند");
            }
        }
    }
    
    private static void testSearchBooksIntegrated_NoMatch(TestBookRepository repository) {
        // سناریو ۴-۲: هیچ مطابقتی پیدا نمی‌شود
        List<Book> result = repository.searchBooksIntegrated("هیچ‌کتابی", "نویسنده‌ناشناس", 9999);
        if (!result.isEmpty()) {
            throw new RuntimeException("انتظار می‌رود هیچ کتابی یافت نشود، اما " + result.size() + " کتاب یافت شد");
        }
    }
}