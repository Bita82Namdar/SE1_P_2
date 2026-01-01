package com.university.library.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class StudentController {
    
    // کلاس‌های داخلی برای جلوگیری از خطا
    public static class Student {
        private String id;
        private String firstName;
        private String lastName;
        private String studentId;
        private String email;
        private String phone;
        private boolean active;
        private Date registrationDate;
        
        // Constructors
        public Student() {
            this.active = true;
            this.registrationDate = new Date();
        }
        
        public Student(String id, String firstName, String lastName, String studentId, String email) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.studentId = studentId;
            this.email = email;
            this.active = true;
            this.registrationDate = new Date();
        }
        
        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        
        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        
        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
        
        public Date getRegistrationDate() { return registrationDate; }
        public void setRegistrationDate(Date registrationDate) { this.registrationDate = registrationDate; }
        
        public String getFullName() {
            return firstName + " " + lastName;
        }
    }
    
    public static class BorrowRecord {
        private String id;
        private String studentId;
        private String bookId;
        private String bookTitle;
        private Date borrowDate;
        private Date dueDate;
        private Date returnDate;
        private String status; // ACTIVE, RETURNED, OVERDUE
        
        // Constructors
        public BorrowRecord() {}
        
        public BorrowRecord(String id, String studentId, String bookId, String bookTitle, 
                           Date borrowDate, Date dueDate) {
            this.id = id;
            this.studentId = studentId;
            this.bookId = bookId;
            this.bookTitle = bookTitle;
            this.borrowDate = borrowDate;
            this.dueDate = dueDate;
            this.status = "ACTIVE";
        }
        
        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }
        
        public String getBookId() { return bookId; }
        public void setBookId(String bookId) { this.bookId = bookId; }
        
        public String getBookTitle() { return bookTitle; }
        public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
        
        public Date getBorrowDate() { return borrowDate; }
        public void setBorrowDate(Date borrowDate) { this.borrowDate = borrowDate; }
        
        public Date getDueDate() { return dueDate; }
        public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
        
        public Date getReturnDate() { return returnDate; }
        public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public boolean isOverdue() {
            if (returnDate != null) return false;
            return dueDate.before(new Date());
        }
    }
    
    public static class ResponseEntity<T> {
        private T body;
        private int status;
        
        public ResponseEntity(T body, int status) {
            this.body = body;
            this.status = status;
        }
        
        public T getBody() { return body; }
        public int getStatus() { return status; }
    }
    
    // داده‌های نمونه
    private List<Student> students = new ArrayList<>();
    private List<BorrowRecord> borrowRecords = new ArrayList<>();
    
    public StudentController() {
        // دانشجویان نمونه
        students.add(new Student("1", "علی", "محمدی", "40123456", "ali@university.edu"));
        students.add(new Student("2", "فاطمه", "کریمی", "40123457", "fatemeh@university.edu"));
        students.add(new Student("3", "محمد", "رضایی", "40123458", "mohammad@university.edu"));
        
        // سوابق امانت نمونه
        Date now = new Date();
        Date twoWeeksAgo = new Date(now.getTime() - (14 * 24 * 60 * 60 * 1000L));
        Date oneWeekAgo = new Date(now.getTime() - (7 * 24 * 60 * 60 * 1000L));
        Date nextWeek = new Date(now.getTime() + (7 * 24 * 60 * 60 * 1000L));
        
        borrowRecords.add(new BorrowRecord("BR001", "1", "B001", "Effective Java", twoWeeksAgo, oneWeekAgo));
        borrowRecords.add(new BorrowRecord("BR002", "1", "B002", "Clean Code", oneWeekAgo, nextWeek));
        borrowRecords.add(new BorrowRecord("BR003", "2", "B003", "Design Patterns", oneWeekAgo, nextWeek));
        
        // تنظیم وضعیت‌ها
        borrowRecords.get(0).setStatus("RETURNED");
        borrowRecords.get(0).setReturnDate(now);
        borrowRecords.get(1).setStatus("ACTIVE");
        borrowRecords.get(2).setStatus("ACTIVE");
    }
    
    // GET /api/students/{id}
    public ResponseEntity<Student> getStudentProfile(String id) {
        try {
            // جستجوی دانشجو با ID
            for (Student student : students) {
                if (student.getId().equals(id) || student.getStudentId().equals(id)) {
                    return new ResponseEntity<>(student, 200); // HTTP 200 OK
                }
            }
            
            // اگر دانشجو یافت نشد
            return new ResponseEntity<>(null, 404); // HTTP 404 Not Found
        } catch (Exception e) {
            return new ResponseEntity<>(null, 500); // HTTP 500 Internal Server Error
        }
    }
    
    // PUT /api/students/{id}/status
    public ResponseEntity<Student> updateStudentStatus(String id, boolean active) {
        try {
            // جستجوی دانشجو
            for (Student student : students) {
                if (student.getId().equals(id) || student.getStudentId().equals(id)) {
                    // تغییر وضعیت
                    student.setActive(active);
                    return new ResponseEntity<>(student, 200); // HTTP 200 OK
                }
            }
            
            // اگر دانشجو یافت نشد
            return new ResponseEntity<>(null, 404); // HTTP 404 Not Found
        } catch (Exception e) {
            return new ResponseEntity<>(null, 500); // HTTP 500 Internal Server Error
        }
    }
    
    // GET /api/students/{id}/borrow-history
    public ResponseEntity<List<BorrowRecord>> getBorrowHistory(String id) {
        try {
            List<BorrowRecord> studentBorrows = new ArrayList<>();
            
            // یافتن تمام سوابق امانت دانشجو
            for (BorrowRecord record : borrowRecords) {
                if (record.getStudentId().equals(id)) {
                    studentBorrows.add(record);
                }
            }
            
            // اگر سابقه‌ای یافت شد
            if (!studentBorrows.isEmpty()) {
                return new ResponseEntity<>(studentBorrows, 200); // HTTP 200 OK
            }
            
            // بررسی اینکه آیا دانشجو وجود دارد
            boolean studentExists = false;
            for (Student student : students) {
                if (student.getId().equals(id) || student.getStudentId().equals(id)) {
                    studentExists = true;
                    break;
                }
            }
            
            if (studentExists) {
                // دانشجو وجود دارد اما سابقه‌ای ندارد
                return new ResponseEntity<>(new ArrayList<>(), 200); // HTTP 200 OK با لیست خالی
            } else {
                // دانشجو وجود ندارد
                return new ResponseEntity<>(null, 404); // HTTP 404 Not Found
            }
            
        } catch (Exception e) {
            return new ResponseEntity<>(null, 500); // HTTP 500 Internal Server Error
        }
    }
    
    // متد کمکی: دریافت تمام دانشجویان
    public ResponseEntity<List<Student>> getAllStudents() {
        try {
            return new ResponseEntity<>(new ArrayList<>(students), 200); // HTTP 200 OK
        } catch (Exception e) {
            return new ResponseEntity<>(null, 500); // HTTP 500 Internal Server Error
        }
    }
    
    // متد کمکی: جستجوی دانشجو با نام
    public ResponseEntity<List<Student>> searchStudents(String name) {
        try {
            List<Student> result = new ArrayList<>();
            
            for (Student student : students) {
                String fullName = student.getFullName().toLowerCase();
                if (name == null || fullName.contains(name.toLowerCase())) {
                    result.add(student);
                }
            }
            
            return new ResponseEntity<>(result, 200); // HTTP 200 OK
        } catch (Exception e) {
            return new ResponseEntity<>(null, 500); // HTTP 500 Internal Server Error
        }
    }
    
    // متد تست
    public static void main(String[] args) {
        StudentController controller = new StudentController();
        
        // تست دریافت پروفایل دانشجو
        System.out.println("=== تست دریافت پروفایل ===");
        ResponseEntity<Student> studentResponse = controller.getStudentProfile("1");
        if (studentResponse.getStatus() == 200 && studentResponse.getBody() != null) {
            Student student = studentResponse.getBody();
            System.out.println("دانشجو یافت شد: " + student.getFullName());
            System.out.println("ایمیل: " + student.getEmail());
            System.out.println("وضعیت فعال: " + student.isActive());
        }
        
        // تست تغییر وضعیت
        System.out.println("\n=== تست تغییر وضعیت ===");
        ResponseEntity<Student> statusResponse = controller.updateStudentStatus("1", false);
        if (statusResponse.getStatus() == 200 && statusResponse.getBody() != null) {
            System.out.println("وضعیت دانشجو تغییر یافت. جدید: " + statusResponse.getBody().isActive());
        }
        
        // تست تاریخچه امانت
        System.out.println("\n=== تست تاریخچه امانت ===");
        ResponseEntity<List<BorrowRecord>> historyResponse = controller.getBorrowHistory("1");
        if (historyResponse.getStatus() == 200 && historyResponse.getBody() != null) {
            System.out.println("تعداد سوابق امانت: " + historyResponse.getBody().size());
            for (BorrowRecord record : historyResponse.getBody()) {
                System.out.println("  - کتاب: " + record.getBookTitle() + "، وضعیت: " + record.getStatus());
            }
        }
        
        // تست دریافت تمام دانشجویان
        System.out.println("\n=== تست دریافت تمام دانشجویان ===");
        ResponseEntity<List<Student>> allStudentsResponse = controller.getAllStudents();
        if (allStudentsResponse.getStatus() == 200 && allStudentsResponse.getBody() != null) {
            System.out.println("تعداد کل دانشجویان: " + allStudentsResponse.getBody().size());
        }
        
        // تست جستجوی دانشجو
        System.out.println("\n=== تست جستجوی دانشجو ===");
        ResponseEntity<List<Student>> searchResponse = controller.searchStudents("علی");
        if (searchResponse.getStatus() == 200 && searchResponse.getBody() != null) {
            System.out.println("تعداد دانشجویان یافت شده: " + searchResponse.getBody().size());
        }
    }
}