package com.university.library.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class BorrowController {
    
    // کلاس‌های داخلی برای جلوگیری از خطا
    public static class BorrowRequest {
        private String id;
        private String studentId;
        private String bookId;
        private Date requestDate;
        private String status; // PENDING, APPROVED, REJECTED, RETURNED
        private Date approvalDate;
        private Date dueDate;
        private Date returnDate;
        
        // Constructors
        public BorrowRequest() {}
        
        public BorrowRequest(String id, String studentId, String bookId) {
            this.id = id;
            this.studentId = studentId;
            this.bookId = bookId;
            this.requestDate = new Date();
            this.status = "PENDING";
        }
        
        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }
        
        public String getBookId() { return bookId; }
        public void setBookId(String bookId) { this.bookId = bookId; }
        
        public Date getRequestDate() { return requestDate; }
        public void setRequestDate(Date requestDate) { this.requestDate = requestDate; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public Date getApprovalDate() { return approvalDate; }
        public void setApprovalDate(Date approvalDate) { this.approvalDate = approvalDate; }
        
        public Date getDueDate() { return dueDate; }
        public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
        
        public Date getReturnDate() { return returnDate; }
        public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }
    }
    
    public static class BorrowRequestDto {
        private String studentId;
        private String bookId;
        
        // Getters and Setters
        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }
        
        public String getBookId() { return bookId; }
        public void setBookId(String bookId) { this.bookId = bookId; }
    }
    
    public static class Book {
        private String id;
        private String title;
        private boolean available;
        
        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public boolean isAvailable() { return available; }
        public void setAvailable(boolean available) { this.available = available; }
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
    private List<BorrowRequest> borrowRequests = new ArrayList<>();
    private List<Book> books = new ArrayList<>();
    private int requestCounter = 1;
    
    public BorrowController() {
        // داده‌های نمونه
        Book book1 = new Book();
        book1.setId("B001");
        book1.setTitle("Effective Java");
        book1.setAvailable(true);
        
        Book book2 = new Book();
        book2.setId("B002");
        book2.setTitle("Clean Code");
        book2.setAvailable(true);
        
        books.add(book1);
        books.add(book2);
        
        // درخواست‌های نمونه
        BorrowRequest request1 = new BorrowRequest("REQ001", "STU001", "B001");
        request1.setStatus("APPROVED");
        
        BorrowRequest request2 = new BorrowRequest("REQ002", "STU002", "B002");
        request2.setStatus("PENDING");
        
        borrowRequests.add(request1);
        borrowRequests.add(request2);
    }
    
    // POST /api/borrow/request
    public ResponseEntity<BorrowRequest> createBorrowRequest(BorrowRequestDto requestDto) {
        try {
            // بررسی وجود کتاب
            Book requestedBook = null;
            for (Book book : books) {
                if (book.getId().equals(requestDto.getBookId()) && book.isAvailable()) {
                    requestedBook = book;
                    break;
                }
            }
            
            if (requestedBook == null) {
                return new ResponseEntity<>(null, 400); // Bad Request
            }
            
            // ایجاد درخواست جدید
            String requestId = "REQ" + String.format("%03d", requestCounter++);
            BorrowRequest newRequest = new BorrowRequest(requestId, requestDto.getStudentId(), requestDto.getBookId());
            
            borrowRequests.add(newRequest);
            
            return new ResponseEntity<>(newRequest, 201); // Created
        } catch (Exception e) {
            return new ResponseEntity<>(null, 500); // Internal Server Error
        }
    }
    
    // GET /api/borrow/requests/pending
    public ResponseEntity<List<BorrowRequest>> getPendingRequests() {
        try {
            List<BorrowRequest> pendingRequests = new ArrayList<>();
            
            for (BorrowRequest request : borrowRequests) {
                if ("PENDING".equals(request.getStatus())) {
                    pendingRequests.add(request);
                }
            }
            
            return new ResponseEntity<>(pendingRequests, 200); // OK
        } catch (Exception e) {
            return new ResponseEntity<>(null, 500); // Internal Server Error
        }
    }
    
    // PUT /api/borrow/requests/{id}/approve
    public ResponseEntity<BorrowRequest> approveRequest(String id) {
        try {
            for (BorrowRequest request : borrowRequests) {
                if (request.getId().equals(id) && "PENDING".equals(request.getStatus())) {
                    // تغییر وضعیت درخواست
                    request.setStatus("APPROVED");
                    request.setApprovalDate(new Date());
                    
                    // تغییر وضعیت کتاب به امانت داده شده
                    for (Book book : books) {
                        if (book.getId().equals(request.getBookId())) {
                            book.setAvailable(false);
                            break;
                        }
                    }
                    
                    return new ResponseEntity<>(request, 200); // OK
                }
            }
            
            return new ResponseEntity<>(null, 404); // Not Found
        } catch (Exception e) {
            return new ResponseEntity<>(null, 500); // Internal Server Error
        }
    }
    
    // PUT /api/borrow/requests/{id}/reject
    public ResponseEntity<BorrowRequest> rejectRequest(String id) {
        try {
            for (BorrowRequest request : borrowRequests) {
                if (request.getId().equals(id) && "PENDING".equals(request.getStatus())) {
                    request.setStatus("REJECTED");
                    return new ResponseEntity<>(request, 200); // OK
                }
            }
            
            return new ResponseEntity<>(null, 404); // Not Found
        } catch (Exception e) {
            return new ResponseEntity<>(null, 500); // Internal Server Error
        }
    }
    
    // PUT /api/borrow/{id}/return
    public ResponseEntity<Book> returnBook(String id) {
        try {
            // یافتن درخواست مربوطه
            BorrowRequest borrowRequest = null;
            for (BorrowRequest request : borrowRequests) {
                if (request.getBookId().equals(id) && "APPROVED".equals(request.getStatus())) {
                    borrowRequest = request;
                    break;
                }
            }
            
            if (borrowRequest == null) {
                return new ResponseEntity<>(null, 404); // Not Found
            }
            
            // تغییر وضعیت درخواست
            borrowRequest.setStatus("RETURNED");
            borrowRequest.setReturnDate(new Date());
            
            // تغییر وضعیت کتاب به موجود
            Book returnedBook = null;
            for (Book book : books) {
                if (book.getId().equals(id)) {
                    book.setAvailable(true);
                    returnedBook = book;
                    break;
                }
            }
            
            if (returnedBook != null) {
                return new ResponseEntity<>(returnedBook, 200); // OK
            } else {
                return new ResponseEntity<>(null, 404); // Not Found
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, 500); // Internal Server Error
        }
    }
    
    // متد تست
    public static void main(String[] args) {
        BorrowController controller = new BorrowController();
        
        // تست دریافت درخواست‌های در انتظار
        ResponseEntity<List<BorrowRequest>> pending = controller.getPendingRequests();
        System.out.println("Pending requests: " + pending.getBody().size());
        
        // تست ایجاد درخواست جدید
        BorrowRequestDto newRequest = new BorrowRequestDto();
        newRequest.setStudentId("STU003");
        newRequest.setBookId("B001");
        
        ResponseEntity<BorrowRequest> created = controller.createBorrowRequest(newRequest);
        System.out.println("Created request ID: " + 
            (created.getBody() != null ? created.getBody().getId() : "null"));
        
        // تست تایید درخواست
        ResponseEntity<BorrowRequest> approved = controller.approveRequest("REQ002");
        System.out.println("Approved request status: " + 
            (approved.getBody() != null ? approved.getBody().getStatus() : "null"));
    }
}