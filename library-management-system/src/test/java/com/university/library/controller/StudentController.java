package com.university.library.controller;

import java.util.*;

public class StudentController {
    
    // کلاس داخلی Student
    public static class Student {
        private String id;
        private String studentId;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String department;
        private int year;
        private boolean active;
        private Date registrationDate;
        
        public Student(String id, String studentId, String firstName, String lastName, 
                      String email, String department, int year) {
            this.id = id;
            this.studentId = studentId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.department = department;
            this.year = year;
            this.active = true;
            this.registrationDate = new Date();
        }
        
        // Getters
        public String getId() { return id; }
        public String getStudentId() { return studentId; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getFullName() { return firstName + " " + lastName; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public String getDepartment() { return department; }
        public int getYear() { return year; }
        public boolean isActive() { return active; }
        public Date getRegistrationDate() { return registrationDate; }
        
        // Setters
        public void setPhone(String phone) { this.phone = phone; }
        public void setActive(boolean active) { this.active = active; }
        
        @Override
        public String toString() {
            return getFullName() + " (" + studentId + ") - " + department + " - سال " + year;
        }
    }
    
    // کلاس داخلی BorrowRecord
    public static class BorrowRecord {
        private String id;
        private String studentId;
        private String bookId;
        private String bookTitle;
        private Date borrowDate;
        private Date dueDate;
        private Date returnDate;
        private String status; // ACTIVE, RETURNED, OVERDUE
        
        public BorrowRecord(String id, String studentId, String bookId, String bookTitle) {
            this.id = id;
            this.studentId = studentId;
            this.bookId = bookId;
            this.bookTitle = bookTitle;
            this.borrowDate = new Date();
            this.dueDate = addDays(new Date(), 14); // مهلت ۱۴ روز
            this.status = "ACTIVE";
        }
        
        // Getters
        public String getId() { return id; }
        public String getStudentId() { return studentId; }
        public String getBookId() { return bookId; }
        public String getBookTitle() { return bookTitle; }
        public Date getBorrowDate() { return borrowDate; }
        public Date getDueDate() { return dueDate; }
        public Date getReturnDate() { return returnDate; }
        public String getStatus() { return status; }
        
        // Setters
        public void setReturnDate(Date returnDate) { 
            this.returnDate = returnDate; 
            this.status = "RETURNED";
        }
        public void setStatus(String status) { this.status = status; }
        
        // بررسی تأخیر
        public boolean isOverdue() {
            if (returnDate != null) return false;
            return new Date().after(dueDate);
        }
        
        // متد کمکی برای اضافه کردن روز
        private Date addDays(Date date, int days) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, days);
            return calendar.getTime();
        }
        
        @Override
        public String toString() {
            String statusText = status;
            if (status.equals("ACTIVE") && isOverdue()) {
                statusText = "OVERDUE";
            }
            return bookTitle + " - امانت: " + formatDate(borrowDate) + 
                   " - مهلت: " + formatDate(dueDate) + " - وضعیت: " + statusText;
        }
        
        private String formatDate(Date date) {
            if (date == null) return "";
            return String.format("%02d/%02d/%d", 
                date.getDate(), date.getMonth() + 1, date.getYear() + 1900);
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
    
    // ذخیره‌سازی دانشجویان و سوابق امانت
    private Map<String, Student> students = new HashMap<>();
    private Map<String, List<BorrowRecord>> borrowRecords = new HashMap<>();
    private int studentCounter = 1;
    private int recordCounter = 1;
    
    public StudentController() {
        // اضافه کردن دانشجویان و سوابق نمونه
        initializeSampleData();
    }
    
    private void initializeSampleData() {
        // دانشجویان نمونه
        addStudent("40123456", "علی", "محمدی", "ali@university.edu", "مهندسی کامپیوتر", 3);
        addStudent("40123457", "فاطمه", "کریمی", "fatemeh@university.edu", "علوم کامپیوتر", 2);
        addStudent("40123458", "محمد", "رضایی", "mohammad@university.edu", "مهندسی نرم‌افزار", 4);
        addStudent("40123459", "زهرا", "احمدی", "zahra@university.edu", "مهندسی برق", 1);
        
        // سوابق امانت نمونه
        addBorrowRecord("ST001", "B001", "Effective Java");
        addBorrowRecord("ST001", "B002", "Clean Code");
        addBorrowRecord("ST002", "B003", "Design Patterns");
        
        // تنظیم وضعیت‌ها
        BorrowRecord record1 = borrowRecords.get("ST001").get(0);
        record1.setReturnDate(addDays(new Date(), -7));
        
        BorrowRecord record2 = borrowRecords.get("ST001").get(1);
        record2.setStatus("OVERDUE");
    }
    
    private void addStudent(String studentId, String firstName, String lastName, 
                           String email, String department, int year) {
        String id = "ST" + String.format("%03d", studentCounter++);
        Student student = new Student(id, studentId, firstName, lastName, email, department, year);
        student.setPhone("0912" + String.format("%07d", new Random().nextInt(10000000)));
        students.put(id, student);
    }
    
    private void addBorrowRecord(String studentId, String bookId, String bookTitle) {
        String recordId = "BR" + String.format("%03d", recordCounter++);
        BorrowRecord record = new BorrowRecord(recordId, studentId, bookId, bookTitle);
        
        if (!borrowRecords.containsKey(studentId)) {
            borrowRecords.put(studentId, new ArrayList<>());
        }
        borrowRecords.get(studentId).add(record);
    }
    
    private Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }
    
    // ۱. دریافت پروفایل دانشجو - GET /api/students/{id}
    public ApiResponse getStudentProfile(String id) {
        try {
            Student student = students.get(id);
            
            if (student == null) {
                // جستجو با شماره دانشجویی
                for (Student s : students.values()) {
                    if (s.getStudentId().equals(id)) {
                        student = s;
                        break;
                    }
                }
            }
            
            if (student == null) {
                return new ApiResponse(false, "دانشجو با شناسه " + id + " یافت نشد");
            }
            
            Map<String, Object> studentInfo = new HashMap<>();
            studentInfo.put("id", student.getId());
            studentInfo.put("studentId", student.getStudentId());
            studentInfo.put("firstName", student.getFirstName());
            studentInfo.put("lastName", student.getLastName());
            studentInfo.put("fullName", student.getFullName());
            studentInfo.put("email", student.getEmail());
            studentInfo.put("phone", student.getPhone());
            studentInfo.put("department", student.getDepartment());
            studentInfo.put("year", student.getYear());
            studentInfo.put("active", student.isActive());
            studentInfo.put("registrationDate", student.getRegistrationDate());
            
            // آمار دانشجو
            List<BorrowRecord> records = borrowRecords.getOrDefault(student.getId(), new ArrayList<>());
            int totalLoans = records.size();
            int activeLoans = 0;
            int overdueLoans = 0;
            
            for (BorrowRecord record : records) {
                if (record.getStatus().equals("ACTIVE") || record.getStatus().equals("OVERDUE")) {
                    activeLoans++;
                }
                if (record.isOverdue()) {
                    overdueLoans++;
                }
            }
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalLoans", totalLoans);
            stats.put("activeLoans", activeLoans);
            stats.put("overdueLoans", overdueLoans);
            stats.put("returnedLoans", totalLoans - activeLoans);
            
            studentInfo.put("loanStats", stats);
            
            return new ApiResponse(true, "پروفایل دانشجو دریافت شد", studentInfo);
            
        } catch (Exception e) {
            return new ApiResponse(false, "خطا در دریافت پروفایل دانشجو: " + e.getMessage());
        }
    }
    
    // ۲. فعال/غیرفعال کردن دانشجو - PUT /api/students/{id}/status
    public ApiResponse updateStudentStatus(String id, boolean active) {
        try {
            Student student = students.get(id);
            
            if (student == null) {
                return new ApiResponse(false, "دانشجو با شناسه " + id + " یافت نشد");
            }
            
            // بررسی منطقی
            if (student.isActive() == active) {
                String status = active ? "فعال" : "غیرفعال";
                return new ApiResponse(false, "دانشجو هم‌اکنون " + status + " است");
            }
            
            // بررسی امانت‌های فعال
            if (!active) {
                List<BorrowRecord> records = borrowRecords.getOrDefault(student.getId(), new ArrayList<>());
                int activeLoans = 0;
                for (BorrowRecord record : records) {
                    if (record.getStatus().equals("ACTIVE") || record.getStatus().equals("OVERDUE")) {
                        activeLoans++;
                    }
                }
                
                if (activeLoans > 0) {
                    return new ApiResponse(false, 
                        "امکان غیرفعال کردن دانشجو وجود ندارد. " + 
                        activeLoans + " امانت فعال دارد");
                }
            }
            
            // تغییر وضعیت
            student.setActive(active);
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("studentId", student.getId());
            responseData.put("studentName", student.getFullName());
            responseData.put("active", student.isActive());
            responseData.put("updated", true);
            
            String statusMessage = active ? "فعال" : "غیرفعال";
            return new ApiResponse(true, "وضعیت دانشجو به " + statusMessage + " تغییر یافت", responseData);
            
        } catch (Exception e) {
            return new ApiResponse(false, "خطا در تغییر وضعیت دانشجو: " + e.getMessage());
        }
    }
    
    // ۳. تاریخچه امانت‌های دانشجو - GET /api/students/{id}/borrow-history
    public ApiResponse getBorrowHistory(String id) {
        try {
            Student student = students.get(id);
            
            if (student == null) {
                return new ApiResponse(false, "دانشجو با شناسه " + id + " یافت نشد");
            }
            
            List<BorrowRecord> records = borrowRecords.getOrDefault(student.getId(), new ArrayList<>());
            
            // مرتب‌سازی بر اساس تاریخ (جدیدترین اول)
            records.sort((r1, r2) -> r2.getBorrowDate().compareTo(r1.getBorrowDate()));
            
            List<Map<String, Object>> history = new ArrayList<>();
            for (BorrowRecord record : records) {
                Map<String, Object> recordInfo = new HashMap<>();
                recordInfo.put("id", record.getId());
                recordInfo.put("bookId", record.getBookId());
                recordInfo.put("bookTitle", record.getBookTitle());
                recordInfo.put("borrowDate", record.getBorrowDate());
                recordInfo.put("dueDate", record.getDueDate());
                recordInfo.put("returnDate", record.getReturnDate());
                
                String status = record.getStatus();
                if (status.equals("ACTIVE") && record.isOverdue()) {
                    status = "OVERDUE";
                }
                recordInfo.put("status", status);
                recordInfo.put("overdue", record.isOverdue());
                
                history.add(recordInfo);
            }
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("studentId", student.getId());
            responseData.put("studentName", student.getFullName());
            responseData.put("totalRecords", history.size());
            responseData.put("history", history);
            
            return new ApiResponse(true, "تاریخچه امانت‌های دانشجو دریافت شد", responseData);
            
        } catch (Exception e) {
            return new ApiResponse(false, "خطا در دریافت تاریخچه امانت‌ها: " + e.getMessage());
        }
    }
    
    // ۴. دریافت تمام دانشجویان - GET /api/students
    public ApiResponse getAllStudents(String department, Integer year, Boolean active) {
        try {
            List<Map<String, Object>> studentList = new ArrayList<>();
            
            for (Student student : students.values()) {
                boolean match = true;
                
                // فیلتر بر اساس رشته تحصیلی
                if (department != null && !department.isEmpty()) {
                    if (!student.getDepartment().toLowerCase().contains(department.toLowerCase())) {
                        match = false;
                    }
                }
                
                // فیلتر بر اساس سال تحصیلی
                if (year != null) {
                    if (student.getYear() != year) {
                        match = false;
                    }
                }
                
                // فیلتر بر اساس وضعیت فعال/غیرفعال
                if (active != null) {
                    if (student.isActive() != active) {
                        match = false;
                    }
                }
                
                if (match) {
                    Map<String, Object> studentInfo = new HashMap<>();
                    studentInfo.put("id", student.getId());
                    studentInfo.put("studentId", student.getStudentId());
                    studentInfo.put("fullName", student.getFullName());
                    studentInfo.put("email", student.getEmail());
                    studentInfo.put("department", student.getDepartment());
                    studentInfo.put("year", student.getYear());
                    studentInfo.put("active", student.isActive());
                    
                    studentList.add(studentInfo);
                }
            }
            
            // مرتب‌سازی بر اساس نام
            studentList.sort(Comparator.comparing(s -> ((String) s.get("fullName"))));
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("students", studentList);
            responseData.put("count", studentList.size());
            responseData.put("totalStudents", students.size());
            
            return new ApiResponse(true, "لیست دانشجویان دریافت شد", responseData);
            
        } catch (Exception e) {
            return new ApiResponse(false, "خطا در دریافت لیست دانشجویان: " + e.getMessage());
        }
    }
    
    // ۵. جستجوی دانشجو - GET /api/students/search
    public ApiResponse searchStudents(String query) {
        try {
            if (query == null || query.trim().isEmpty()) {
                return getAllStudents(null, null, null);
            }
            
            String searchQuery = query.toLowerCase().trim();
            List<Map<String, Object>> result = new ArrayList<>();
            
            for (Student student : students.values()) {
                boolean match = false;
                
                // جستجو در نام کامل
                if (student.getFullName().toLowerCase().contains(searchQuery)) {
                    match = true;
                }
                
                // جستجو در شماره دانشجویی
                if (!match && student.getStudentId().contains(searchQuery)) {
                    match = true;
                }
                
                // جستجو در ایمیل
                if (!match && student.getEmail().toLowerCase().contains(searchQuery)) {
                    match = true;
                }
                
                // جستجو در رشته تحصیلی
                if (!match && student.getDepartment().toLowerCase().contains(searchQuery)) {
                    match = true;
                }
                
                if (match) {
                    Map<String, Object> studentInfo = new HashMap<>();
                    studentInfo.put("id", student.getId());
                    studentInfo.put("studentId", student.getStudentId());
                    studentInfo.put("fullName", student.getFullName());
                    studentInfo.put("email", student.getEmail());
                    studentInfo.put("department", student.getDepartment());
                    studentInfo.put("year", student.getYear());
                    studentInfo.put("active", student.isActive());
                    
                    result.add(studentInfo);
                }
            }
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("query", query);
            responseData.put("students", result);
            responseData.put("count", result.size());
            
            return new ApiResponse(true, "نتیجه جستجوی دانشجو دریافت شد", responseData);
            
        } catch (Exception e) {
            return new ApiResponse(false, "خطا در جستجوی دانشجو: " + e.getMessage());
        }
    }
    
    // ۶. ثبت امانت جدید برای دانشجو
    public ApiResponse addBorrowRecordForStudent(String studentId, String bookId, String bookTitle) {
        try {
            Student student = students.get(studentId);
            
            if (student == null) {
                return new ApiResponse(false, "دانشجو یافت نشد");
            }
            
            if (!student.isActive()) {
                return new ApiResponse(false, "دانشجو غیرفعال است و نمی‌تواند کتاب امانت بگیرد");
            }
            
            addBorrowRecord(studentId, bookId, bookTitle);
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("studentId", studentId);
            responseData.put("bookId", bookId);
            responseData.put("bookTitle", bookTitle);
            responseData.put("recordId", "BR" + String.format("%03d", recordCounter - 1));
            
            return new ApiResponse(true, "امانت جدید ثبت شد", responseData);
            
        } catch (Exception e) {
            return new ApiResponse(false, "خطا در ثبت امانت: " + e.getMessage());
        }
    }
    
    // تست کنترلر
    public static void main(String[] args) {
        System.out.println("👨‍🎓 تست StudentController");
        System.out.println("=========================");
        
        StudentController controller = new StudentController();
        
        try {
            // تست ۱: دریافت پروفایل دانشجو
            System.out.println("\n1. تست دریافت پروفایل دانشجو:");
            ApiResponse profileResponse = controller.getStudentProfile("ST001");
            System.out.println("   موفق: " + profileResponse.isSuccess());
            System.out.println("   پیام: " + profileResponse.getMessage());
            
            // تست ۲: تغییر وضعیت دانشجو
            System.out.println("\n2. تست تغییر وضعیت دانشجو:");
            ApiResponse statusResponse = controller.updateStudentStatus("ST001", false);
            System.out.println("   موفق: " + statusResponse.isSuccess());
            
            // تست ۳: تاریخچه امانت‌ها
            System.out.println("\n3. تست تاریخچه امانت‌ها:");
            ApiResponse historyResponse = controller.getBorrowHistory("ST001");
            System.out.println("   موفق: " + historyResponse.isSuccess());
            
            // تست ۴: دریافت تمام دانشجویان
            System.out.println("\n4. تست دریافت تمام دانشجویان:");
            ApiResponse allStudentsResponse = controller.getAllStudents(null, null, null);
            System.out.println("   موفق: " + allStudentsResponse.isSuccess());
            
            // تست ۵: جستجوی دانشجو
            System.out.println("\n5. تست جستجوی دانشجو:");
            ApiResponse searchResponse = controller.searchStudents("علی");
            System.out.println("   موفق: " + searchResponse.isSuccess());
            
            // نمایش اطلاعات کلی
            System.out.println("\n📊 اطلاعات سیستم:");
            System.out.println("   تعداد کل دانشجویان: " + controller.students.size());
            System.out.println("   تعداد کل سوابق امانت: " + controller.recordCounter);
            
            System.out.println("\n✅ تمام تست‌ها با موفقیت انجام شد!");
            
        } catch (Exception e) {
            System.out.println("❌ خطا در تست: " + e.getMessage());
            e.printStackTrace();
        }
    }
}