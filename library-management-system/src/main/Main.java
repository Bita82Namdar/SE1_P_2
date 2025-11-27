package com.library;

import com.library.models.*;
import com.library.services.*;
import com.library.utils.DateUtils;
import com.library.enums.UserType;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static LibraryService libraryService = new LibraryService();
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;

    public static void main(String[] args) {
        initializeSampleData();
        showMainMenu();
    }

    private static void initializeSampleData() {
        // افزودن داده‌های نمونه
        libraryService.addStudent(new Student("student1", "pass1", "John Doe", "ST001"));
        libraryService.addStudent(new Student("student2", "pass2", "Jane Smith", "ST002"));
        
        libraryService.addEmployee(new Employee("emp1", "emp123", "Alice Johnson", "EMP001"));
        libraryService.addEmployee(new Employee("emp2", "emp456", "Bob Brown", "EMP002"));
        
        libraryService.addLibrarian(new Librarian("admin", "admin123", "Admin User"));
        
        Book book1 = new Book("B001", "Java Programming", "John Smith", "2020", "1234567890");
        Book book2 = new Book("B002", "Data Structures", "Mary Johnson", "2019", "1234567891");
        libraryService.addBook(book1);
        libraryService.addBook(book2);
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\n=== سیستم مدیریت کتابخانه دانشگاه ===");
            System.out.println("1. کاربر مهمان");
            System.out.println("2. دانشجو");
            System.out.println("3. کارمند کتابخانه");
            System.out.println("4. مدیر سیستم");
            System.out.println("0. خروج");
            System.out.print("لطفاً گزینه مورد نظر را انتخاب کنید: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // مصرف newline

            switch (choice) {
                case 1:
                    showGuestMenu();
                    break;
                case 2:
                    showStudentMenu();
                    break;
                case 3:
                    showEmployeeMenu();
                    break;
                case 4:
                    showLibrarianMenu();
                    break;
                case 0:
                    System.out.println("خروج از سیستم...");
                    return;
                default:
                    System.out.println("گزینه نامعتبر!");
            }
        }
    }

    private static void showGuestMenu() {
        while (true) {
            System.out.println("\n=== منوی کاربر مهمان ===");
            System.out.println("1. مشاهده تعداد دانشجویان ثبت‌نام کرده");
            System.out.println("2. جستجوی کتاب بر اساس نام");
            System.out.println("3. مشاهده اطلاعات آماری");
            System.out.println("0. بازگشت به منوی اصلی");
            System.out.print("لطفاً گزینه مورد نظر را انتخاب کنید: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("تعداد دانشجویان ثبت‌نام کرده: " + libraryService.getStudentCount());
                    break;
                case 2:
                    searchBooksAsGuest();
                    break;
                case 3:
                    showStatistics();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("گزینه نامعتبر!");
            }
        }
    }

    private static void searchBooksAsGuest() {
        System.out.print("نام کتاب برای جستجو: ");
        String title = scanner.nextLine();
        List<Book> results = libraryService.searchBooksByTitle(title);
        
        if (results.isEmpty()) {
            System.out.println("کتابی یافت نشد.");
        } else {
            System.out.println("نتایج جستجو:");
            for (Book book : results) {
                System.out.println("عنوان: " + book.getTitle() + 
                                 ", نویسنده: " + book.getAuthor() + 
                                 ", سال نشر: " + book.getPublicationYear());
            }
        }
    }

    private static void showStatistics() {
        System.out.println("=== اطلاعات آماری ===");
        System.out.println("تعداد کل دانشجویان: " + libraryService.getStudentCount());
        System.out.println("تعداد کل کتاب‌ها: " + libraryService.getBookCount());
        System.out.println("تعداد کل امانت‌ها: " + libraryService.getBorrowCount());
        System.out.println("تعداد کتاب‌های در امانت: " + libraryService.getActiveBorrowCount());
    }

    private static void showStudentMenu() {
        while (true) {
            if (currentUser == null) {
                System.out.println("\n=== منوی دانشجو ===");
                System.out.println("1. ثبت‌نام");
                System.out.println("2. ورود");
                System.out.println("0. بازگشت");
                System.out.print("انتخاب: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        registerStudent();
                        break;
                    case 2:
                        loginStudent();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("گزینه نامعتبر!");
                }
            } else {
                showLoggedInStudentMenu();
            }
        }
    }

    private static void registerStudent() {
        System.out.print("نام کاربری: ");
        String username = scanner.nextLine();
        System.out.print("رمز عبور: ");
        String password = scanner.nextLine();
        System.out.print("نام کامل: ");
        String fullName = scanner.nextLine();
        System.out.print("شماره دانشجویی: ");
        String studentId = scanner.nextLine();

        Student student = new Student(username, password, fullName, studentId);
        if (libraryService.addStudent(student)) {
            System.out.println("ثبت‌نام با موفقیت انجام شد.");
        } else {
            System.out.println("خطا در ثبت‌نام. نام کاربری ممکن است تکراری باشد.");
        }
    }

    private static void loginStudent() {
        System.out.print("نام کاربری: ");
        String username = scanner.nextLine();
        System.out.print("رمز عبور: ");
        String password = scanner.nextLine();

        currentUser = libraryService.authenticateStudent(username, password);
        if (currentUser != null) {
            System.out.println("ورود موفقیت‌آمیز. خوش آمدید " + currentUser.getFullName());
        } else {
            System.out.println("نام کاربری یا رمز عبور اشتباه است.");
        }
    }

    private static void showLoggedInStudentMenu() {
        while (true) {
            System.out.println("\n=== منوی دانشجو (" + currentUser.getFullName() + ") ===");
            System.out.println("1. جستجوی کتاب");
            System.out.println("2. ثبت درخواست امانت");
            System.out.println("3. خروج");
            System.out.print("انتخاب: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    searchBooksAsStudent();
                    break;
                case 2:
                    requestBorrow();
                    break;
                case 3:
                    currentUser = null;
                    return;
                default:
                    System.out.println("گزینه نامعتبر!");
            }
        }
    }

    private static void searchBooksAsStudent() {
        System.out.print("عنوان کتاب (اختیاری): ");
        String title = scanner.nextLine();
        System.out.print("نام نویسنده (اختیاری): ");
        String author = scanner.nextLine();
        System.out.print("سال نشر (اختیاری): ");
        String year = scanner.nextLine();

        List<Book> results = libraryService.searchBooks(title, author, year);
        
        if (results.isEmpty()) {
            System.out.println("کتابی یافت نشد.");
        } else {
            System.out.println("نتایج جستجو:");
            for (Book book : results) {
                String status = book.isAvailable() ? "موجود" : "امانت داده شده";
                System.out.println("عنوان: " + book.getTitle() + 
                                 ", نویسنده: " + book.getAuthor() + 
                                 ", سال: " + book.getPublicationYear() +
                                 ", وضعیت: " + status);
            }
        }
    }

    private static void requestBorrow() {
        System.out.print("شناسه کتاب: ");
        String bookId = scanner.nextLine();
        System.out.print("تاریخ شروع (YYYY-MM-DD): ");
        String startDate = scanner.nextLine();
        System.out.print("تاریخ پایان (YYYY-MM-DD): ");
        String endDate = scanner.nextLine();

        if (libraryService.requestBorrow((Student) currentUser, bookId, startDate, endDate)) {
            System.out.println("درخواست امانت با موفقیت ثبت شد.");
        } else {
            System.out.println("خطا در ثبت درخواست امانت.");
        }
    }

    private static void showEmployeeMenu() {
        System.out.print("نام کاربری: ");
        String username = scanner.nextLine();
        System.out.print("رمز عبور: ");
        String password = scanner.nextLine();

        currentUser = libraryService.authenticateEmployee(username, password);
        if (currentUser == null) {
            System.out.println("احراز هویت ناموفق!");
            return;
        }

        System.out.println("ورود موفقیت‌آمیز. خوش آمدید " + currentUser.getFullName());

        while (true) {
            System.out.println("\n=== منوی کارمند (" + currentUser.getFullName() + ") ===");
            System.out.println("1. تغییر رمز عبور");
            System.out.println("2. ثبت کتاب جدید");
            System.out.println("3. جستجو و ویرایش کتاب");
            System.out.println("4. بررسی درخواست‌های امانت");
            System.out.println("5. مشاهده تاریخچه امانات دانشجو");
            System.out.println("6. فعال/غیرفعال کردن دانشجو");
            System.out.println("7. دریافت کتاب بازگشتی");
            System.out.println("0. خروج");
            System.out.print("انتخاب: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    changePassword();
                    break;
                case 2:
                    addBook();
                    break;
                case 3:
                    searchAndEditBook();
                    break;
                case 4:
                    reviewBorrowRequests();
                    break;
                case 5:
                    viewStudentBorrowHistory();
                    break;
                case 6:
                    toggleStudentStatus();
                    break;
                case 7:
                    returnBook();
                    break;
                case 0:
                    currentUser = null;
                    return;
                default:
                    System.out.println("گزینه نامعتبر!");
            }
        }
    }

    private static void changePassword() {
        System.out.print("رمز عبور جدید: ");
        String newPassword = scanner.nextLine();
        libraryService.changePassword(currentUser.getUsername(), newPassword);
        System.out.println("رمز عبور با موفقیت تغییر یافت.");
    }

    private static void addBook() {
        System.out.print("شناسه کتاب: ");
        String id = scanner.nextLine();
        System.out.print("عنوان: ");
        String title = scanner.nextLine();
        System.out.print("نویسنده: ");
        String author = scanner.nextLine();
        System.out.print("سال نشر: ");
        String year = scanner.nextLine();
        System.out.print("شابک: ");
        String isbn = scanner.nextLine();

        Book book = new Book(id, title, author, year, isbn);
        if (libraryService.addBook(book)) {
            System.out.println("کتاب با موفقیت ثبت شد.");
        } else {
            System.out.println("خطا در ثبت کتاب.");
        }
    }

    private static void showLibrarianMenu() {
        System.out.print("نام کاربری مدیر: ");
        String username = scanner.nextLine();
        System.out.print("رمز عبور: ");
        String password = scanner.nextLine();

        if (!libraryService.authenticateLibrarian(username, password)) {
            System.out.println("احراز هویت ناموفق!");
            return;
        }

        System.out.println("ورود موفقیت‌آمیز.");

        while (true) {
            System.out.println("\n=== منوی مدیر سیستم ===");
            System.out.println("1. تعریف کارمند جدید");
            System.out.println("2. مشاهده عملکرد کارمندان");
            System.out.println("3. مشاهده آمار امانات");
            System.out.println("4. مشاهده آمار دانشجویان");
            System.out.println("0. بازگشت");
            System.out.print("انتخاب: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addEmployee();
                    break;
                case 2:
                    viewEmployeePerformance();
                    break;
                case 3:
                    viewBorrowStatistics();
                    break;
                case 4:
                    viewStudentStatistics();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("گزینه نامعتبر!");
            }
        }
    }

    private static void addEmployee() {
        System.out.print("نام کاربری: ");
        String username = scanner.nextLine();
        System.out.print("رمز عبور: ");
        String password = scanner.nextLine();
        System.out.print("نام کامل: ");
        String fullName = scanner.nextLine();
        System.out.print("شناسه کارمند: ");
        String employeeId = scanner.nextLine();

        Employee employee = new Employee(username, password, fullName, employeeId);
        if (libraryService.addEmployee(employee)) {
            System.out.println("کارمند با موفقیت اضافه شد.");
        } else {
            System.out.println("خطا در اضافه کردن کارمند.");
        }
    }

    // سایر متدها
}