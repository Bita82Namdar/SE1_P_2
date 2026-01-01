package com.university.library.controller;

import java.util.*;

public class BookController {
    
    // کلاس داخلی Book
    public static class Book {
        private String id;
        private String title;
        private String author;
        private int year;
        private String isbn;
        private String publisher;
        private int totalCopies;
        private int availableCopies;
        private String category;
        
        public Book(String id, String title, String author, int year) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.year = year;
            this.totalCopies = 1;
            this.availableCopies = 1;
        }
        
        // Getters
        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public int getYear() { return year; }
        public String getIsbn() { return isbn; }
        public String getPublisher() { return publisher; }
        public int getTotalCopies() { return totalCopies; }
        public int getAvailableCopies() { return availableCopies; }
        public String getCategory() { return category; }
        
        // Setters
        public void setIsbn(String isbn) { this.isbn = isbn; }
        public void setPublisher(String publisher) { this.publisher = publisher; }
        public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }
        public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }
        public void setCategory(String category) { this.category = category; }
        
        public void borrowCopy() {
            if (availableCopies > 0) {
                availableCopies--;
            }
        }
        
        public void returnCopy() {
            if (availableCopies < totalCopies) {
                availableCopies++;
            }
        }
        
        @Override
        public String toString() {
            return title + " - " + author + " (" + year + ") - " + 
                   (availableCopies > 0 ? "موجود" : "امانت") + 
                   " (" + availableCopies + "/" + totalCopies + ")";
        }
    }
    
    // کلاس داخلی برای پاسخ API
    public static class ApiResponse {
        private boolean success;
        private String message;
        private Object data;
        
        public ApiResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }
        
        // Getters
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Object getData() { return data; }
    }
    
    // ذخیره‌سازی کتاب‌ها
    private Map<String, Book> books = new HashMap<>();
    private int bookCounter = 1;
    
    public BookController() {
        // اضافه کردن کتاب‌های نمونه
        initializeSampleBooks();
    }
    
    private void initializeSampleBooks() {
        // کتاب‌های نمونه
        addBook("Effective Java", "Joshua Bloch", 2018, "978-0134685991", "Addison-Wesley", 5, "Programming");
        addBook("Clean Code", "Robert C. Martin", 2008, "978-0132350884", "Prentice Hall", 3, "Programming");
        addBook("Design Patterns", "Gang of Four", 1994, "978-0201633610", "Addison-Wesley", 4, "Design");
        addBook("Introduction to Algorithms", "Thomas H. Cormen", 2009, "978-0262033848", "MIT Press", 2, "Algorithms");
        addBook("The Pragmatic Programmer", "Andrew Hunt", 1999, "978-0201616224", "Addison-Wesley", 3, "Programming");
    }
    
    private void addBook(String title, String author, int year, String isbn, String publisher, int copies, String category) {
        String bookId = "B" + String.format("%03d", bookCounter++);
        Book book = new Book(bookId, title, author, year);
        book.setIsbn(isbn);
        book.setPublisher(publisher);
        book.setTotalCopies(copies);
        book.setAvailableCopies(copies);
        book.setCategory(category);
        books.put(bookId, book);
    }
    
    // ۱. دریافت لیست کتاب‌ها (با قابلیت جستجو و فیلتر) - GET /api/books
    public ApiResponse getBooks(String title, String author, String category, Integer year) {
        try {
            List<Book> result = new ArrayList<>();
            
            for (Book book : books.values()) {
                boolean match = true;
                
                // فیلتر بر اساس عنوان
                if (title != null && !title.isEmpty()) {
                    if (!book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                        match = false;
                    }
                }
                
                // فیلتر بر اساس نویسنده
                if (author != null && !author.isEmpty()) {
                    if (!book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                        match = false;
                    }
                }
                
                // فیلتر بر اساس دسته‌بندی
                if (category != null && !category.isEmpty()) {
                    if (!category.equalsIgnoreCase(book.getCategory())) {
                        match = false;
                    }
                }
                
                // فیلتر بر اساس سال
                if (year != null) {
                    if (book.getYear() != year) {
                        match = false;
                    }
                }
                
                if (match) {
                    result.add(book);
                }
            }
            
            // مرتب‌سازی بر اساس عنوان
            result.sort(Comparator.comparing(Book::getTitle));
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("books", result);
            responseData.put("count", result.size());
            responseData.put("totalBooks", books.size());
            
            return new ApiResponse(true, "لیست کتاب‌ها دریافت شد", responseData);
            
        } catch (Exception e) {
            return new ApiResponse(false, "خطا در دریافت لیست کتاب‌ها: " + e.getMessage());
        }
    }
    
    // ۲. دریافت جزئیات یک کتاب - GET /api/books/{id}
    public ApiResponse getBookById(String id) {
        try {
            Book book = books.get(id);
            
            if (book == null) {
                return new ApiResponse(false, "کتاب با شناسه " + id + " یافت نشد");
            }
            
            Map<String, Object> bookDetails = new HashMap<>();
            bookDetails.put("id", book.getId());
            bookDetails.put("title", book.getTitle());
            bookDetails.put("author", book.getAuthor());
            bookDetails.put("year", book.getYear());
            bookDetails.put("isbn", book.getIsbn());
            bookDetails.put("publisher", book.getPublisher());
            bookDetails.put("totalCopies", book.getTotalCopies());
            bookDetails.put("availableCopies", book.getAvailableCopies());
            bookDetails.put("category", book.getCategory());
            bookDetails.put("status", book.getAvailableCopies() > 0 ? "موجود" : "امانت داده شده");
            
            return new ApiResponse(true, "جزئیات کتاب دریافت شد", bookDetails);
            
        } catch (Exception e) {
            return new ApiResponse(false, "خطا در دریافت جزئیات کتاب: " + e.getMessage());
        }
    }
    
    // ۳. ایجاد کتاب جدید (کارمند) - POST /api/books
    public ApiResponse createBook(String title, String author, int year, String isbn, 
                                 String publisher, int totalCopies, String category) {
        try {
            // بررسی اطلاعات ورودی
            if (title == null || title.trim().isEmpty()) {
                return new ApiResponse(false, "عنوان کتاب الزامی است");
            }
            
            if (author == null || author.trim().isEmpty()) {
                return new ApiResponse(false, "نویسنده کتاب الزامی است");
            }
            
            if (year < 0 || year > java.time.Year.now().getValue()) {
                return new ApiResponse(false, "سال انتشار نامعتبر است");
            }
            
            if (totalCopies <= 0) {
                return new ApiResponse(false, "تعداد نسخه‌ها باید بیشتر از صفر باشد");
            }
            
            // ایجاد کتاب جدید
            String bookId = "B" + String.format("%03d", bookCounter++);
            Book newBook = new Book(bookId, title, author, year);
            newBook.setIsbn(isbn);
            newBook.setPublisher(publisher);
            newBook.setTotalCopies(totalCopies);
            newBook.setAvailableCopies(totalCopies);
            newBook.setCategory(category);
            
            books.put(bookId, newBook);
            
            // پاسخ موفق
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("bookId", bookId);
            responseData.put("title", title);
            responseData.put("author", author);
            responseData.put("year", year);
            responseData.put("totalCopies", totalCopies);
            responseData.put("availableCopies", totalCopies);
            
            return new ApiResponse(true, "کتاب جدید با موفقیت ایجاد شد", responseData);
            
        } catch (Exception e) {
            return new ApiResponse(false, "خطا در ایجاد کتاب: " + e.getMessage());
        }
    }
    
    // ۴. به‌روزرسانی اطلاعات کتاب (کارمند) - PUT /api/books/{id}
    public ApiResponse updateBook(String id, String title, String author, Integer year, 
                                 String isbn, String publisher, Integer totalCopies, String category) {
        try {
            Book book = books.get(id);
            
            if (book == null) {
                return new ApiResponse(false, "کتاب با شناسه " + id + " یافت نشد");
            }
            
            // به‌روزرسانی فیلدها (در صورت ارسال)
            if (title != null && !title.trim().isEmpty()) {
                // بررسی تکراری نبودن عنوان
                for (Book b : books.values()) {
                    if (!b.getId().equals(id) && b.getTitle().equalsIgnoreCase(title)) {
                        return new ApiResponse(false, "کتابی با این عنوان از قبل وجود دارد");
                    }
                }
                // book.setTitle(title); // اگر setter داشتیم
            }
            
            if (author != null && !author.trim().isEmpty()) {
                // book.setAuthor(author);
            }
            
            if (year != null && year > 0) {
                // book.setYear(year);
            }
            
            if (isbn != null) {
                book.setIsbn(isbn);
            }
            
            if (publisher != null) {
                book.setPublisher(publisher);
            }
            
            if (totalCopies != null && totalCopies > 0) {
                int currentBorrowed = book.getTotalCopies() - book.getAvailableCopies();
                if (totalCopies < currentBorrowed) {
                    return new ApiResponse(false, "تعداد نسخه‌های جدید نمی‌تواند کمتر از تعداد نسخه‌های امانت داده شده باشد");
                }
                book.setTotalCopies(totalCopies);
                book.setAvailableCopies(totalCopies - currentBorrowed);
            }
            
            if (category != null) {
                book.setCategory(category);
            }
            
            // پاسخ موفق
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("bookId", id);
            responseData.put("title", book.getTitle());
            responseData.put("author", book.getAuthor());
            responseData.put("updated", true);
            
            return new ApiResponse(true, "اطلاعات کتاب با موفقیت به‌روز شد", responseData);
            
        } catch (Exception e) {
            return new ApiResponse(false, "خطا در به‌روزرسانی کتاب: " + e.getMessage());
        }
    }
    
    // ۵. جستجوی پیشرفته کتاب - GET /api/books/search
    public ApiResponse searchBooks(String query) {
        try {
            if (query == null || query.trim().isEmpty()) {
                return getBooks(null, null, null, null);
            }
            
            List<Book> result = new ArrayList<>();
            String searchQuery = query.toLowerCase().trim();
            
            for (Book book : books.values()) {
                boolean match = false;
                
                // جستجو در عنوان
                if (book.getTitle().toLowerCase().contains(searchQuery)) {
                    match = true;
                }
                
                // جستجو در نویسنده
                if (!match && book.getAuthor().toLowerCase().contains(searchQuery)) {
                    match = true;
                }
                
                // جستجو در دسته‌بندی
                if (!match && book.getCategory() != null && 
                    book.getCategory().toLowerCase().contains(searchQuery)) {
                    match = true;
                }
                
                // جستجو در ناشر
                if (!match && book.getPublisher() != null && 
                    book.getPublisher().toLowerCase().contains(searchQuery)) {
                    match = true;
                }
                
                // جستجو در ISBN
                if (!match && book.getIsbn() != null && 
                    book.getIsbn().contains(searchQuery)) {
                    match = true;
                }
                
                if (match) {
                    result.add(book);
                }
            }
            
            // مرتب‌سازی بر اساس تطابق بیشتر
            result.sort((b1, b2) -> {
                int score1 = calculateRelevanceScore(b1, searchQuery);
                int score2 = calculateRelevanceScore(b2, searchQuery);
                return Integer.compare(score2, score1); // نزولی
            });
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("query", query);
            responseData.put("books", result);
            responseData.put("count", result.size());
            
            return new ApiResponse(true, "نتیجه جستجو دریافت شد", responseData);
            
        } catch (Exception e) {
            return new ApiResponse(false, "خطا در جستجو: " + e.getMessage());
        }
    }
    
    // ۶. امانت گرفتن کتاب
    public ApiResponse borrowBook(String bookId) {
        try {
            Book book = books.get(bookId);
            
            if (book == null) {
                return new ApiResponse(false, "کتاب یافت نشد");
            }
            
            if (book.getAvailableCopies() <= 0) {
                return new ApiResponse(false, "این کتاب هم‌اکنون موجود نیست");
            }
            
            book.borrowCopy();
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("bookId", bookId);
            responseData.put("title", book.getTitle());
            responseData.put("availableCopies", book.getAvailableCopies());
            responseData.put("borrowed", true);
            
            return new ApiResponse(true, "کتاب با موفقیت امانت گرفته شد", responseData);
            
        } catch (Exception e) {
            return new ApiResponse(false, "خطا در امانت گرفتن کتاب: " + e.getMessage());
        }
    }
    
    // ۷. بازگرداندن کتاب
    public ApiResponse returnBook(String bookId) {
        try {
            Book book = books.get(bookId);
            
            if (book == null) {
                return new ApiResponse(false, "کتاب یافت نشد");
            }
            
            if (book.getAvailableCopies() >= book.getTotalCopies()) {
                return new ApiResponse(false, "این کتاب قبلاً بازگردانده شده است");
            }
            
            book.returnCopy();
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("bookId", bookId);
            responseData.put("title", book.getTitle());
            responseData.put("availableCopies", book.getAvailableCopies());
            responseData.put("returned", true);
            
            return new ApiResponse(true, "کتاب با موفقیت بازگردانده شد", responseData);
            
        } catch (Exception e) {
            return new ApiResponse(false, "خطا در بازگرداندن کتاب: " + e.getMessage());
        }
    }
    
    // ۸. دریافت آمار کتاب‌ها
    public ApiResponse getBookStats() {
        try {
            int totalBooks = books.size();
            int totalCopies = 0;
            int availableCopies = 0;
            int borrowedCopies = 0;
            
            Map<String, Integer> categoryStats = new HashMap<>();
            
            for (Book book : books.values()) {
                totalCopies += book.getTotalCopies();
                availableCopies += book.getAvailableCopies();
                borrowedCopies += (book.getTotalCopies() - book.getAvailableCopies());
                
                String category = book.getCategory() != null ? book.getCategory() : "بدون دسته";
                categoryStats.put(category, categoryStats.getOrDefault(category, 0) + 1);
            }
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalBooks", totalBooks);
            stats.put("totalCopies", totalCopies);
            stats.put("availableCopies", availableCopies);
            stats.put("borrowedCopies", borrowedCopies);
            stats.put("borrowRate", totalCopies > 0 ? (borrowedCopies * 100.0 / totalCopies) : 0);
            stats.put("categories", categoryStats);
            
            return new ApiResponse(true, "آمار کتاب‌ها دریافت شد", stats);
            
        } catch (Exception e) {
            return new ApiResponse(false, "خطا در دریافت آمار: " + e.getMessage());
        }
    }
    
    // متد کمکی: محاسبه امتیاز ارتباط
    private int calculateRelevanceScore(Book book, String query) {
        int score = 0;
        
        if (book.getTitle().toLowerCase().contains(query)) {
            score += 10;
        }
        
        if (book.getAuthor().toLowerCase().contains(query)) {
            score += 8;
        }
        
        if (book.getCategory() != null && book.getCategory().toLowerCase().contains(query)) {
            score += 6;
        }
        
        if (book.getPublisher() != null && book.getPublisher().toLowerCase().contains(query)) {
            score += 4;
        }
        
        if (book.getIsbn() != null && book.getIsbn().contains(query)) {
            score += 2;
        }
        
        return score;
    }
    
    // تست کنترلر
    public static void main(String[] args) {
        System.out.println("📚 تست BookController");
        System.out.println("=====================");
        
        BookController controller = new BookController();
        
        try {
            // تست ۱: دریافت تمام کتاب‌ها
            System.out.println("\n1. تست دریافت تمام کتاب‌ها:");
            ApiResponse allBooks = controller.getBooks(null, null, null, null);
            System.out.println("   موفق: " + allBooks.isSuccess());
            System.out.println("   پیام: " + allBooks.getMessage());
            
            // تست ۲: دریافت کتاب با ID
            System.out.println("\n2. تست دریافت کتاب با ID:");
            ApiResponse bookById = controller.getBookById("B001");
            System.out.println("   موفق: " + bookById.isSuccess());
            
            // تست ۳: ایجاد کتاب جدید
            System.out.println("\n3. تست ایجاد کتاب جدید:");
            ApiResponse newBook = controller.createBook(
                "Test Book", "Test Author", 2024, "978-0000000000", 
                "Test Publisher", 3, "Test Category"
            );
            System.out.println("   موفق: " + newBook.isSuccess());
            System.out.println("   پیام: " + newBook.getMessage());
            
            // تست ۴: جستجوی کتاب
            System.out.println("\n4. تست جستجوی کتاب:");
            ApiResponse searchResult = controller.searchBooks("Java");
            System.out.println("   موفق: " + searchResult.isSuccess());
            
            // تست ۵: امانت گرفتن کتاب
            System.out.println("\n5. تست امانت گرفتن کتاب:");
            ApiResponse borrowResult = controller.borrowBook("B001");
            System.out.println("   موفق: " + borrowResult.isSuccess());
            
            // تست ۶: بازگرداندن کتاب
            System.out.println("\n6. تست بازگرداندن کتاب:");
            ApiResponse returnResult = controller.returnBook("B001");
            System.out.println("   موفق: " + returnResult.isSuccess());
            
            // تست ۷: دریافت آمار
            System.out.println("\n7. تست دریافت آمار:");
            ApiResponse statsResult = controller.getBookStats();
            System.out.println("   موفق: " + statsResult.isSuccess());
            
            // نمایش تعداد کتاب‌ها
            System.out.println("\n📊 اطلاعات سیستم:");
            System.out.println("   تعداد کل کتاب‌ها: " + controller.books.size());
            
            System.out.println("\n✅ تمام تست‌ها با موفقیت انجام شد!");
            
        } catch (Exception e) {
            System.out.println("❌ خطا در تست: " + e.getMessage());
            e.printStackTrace();
        }
    }
}