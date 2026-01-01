package com.university.library.controller;

import java.util.ArrayList;
import java.util.List;

public class AdminController {
    
    // کلاس داخلی Employee
    public static class Employee {
        private String id;
        private String firstName;
        private String lastName;
        private String username;
        private String email;
        private String role;
        private boolean active;
        
        // Constructor
        public Employee(String id, String firstName, String lastName, 
                       String username, String email, String role) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.username = username;
            this.email = email;
            this.role = role;
            this.active = true;
        }
        
        // Getters
        public String getId() { return id; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
        public boolean isActive() { return active; }
        
        // Setters
        public void setActive(boolean active) { this.active = active; }
    }
    
    // کلاس داخلی ResponseEntity
    public static class ResponseEntity<T> {
        private T data;
        private int statusCode;
        private String message;
        
        public ResponseEntity(T data, int statusCode) {
            this.data = data;
            this.statusCode = statusCode;
        }
        
        public ResponseEntity(T data, int statusCode, String message) {
            this.data = data;
            this.statusCode = statusCode;
            this.message = message;
        }
        
        public T getBody() { return data; }
        public int getStatusCode() { return statusCode; }
        public String getMessage() { return message; }
    }
    
    // داده‌های نمونه
    private List<Employee> employees = new ArrayList<>();
    private int employeeCounter = 1;
    
    public AdminController() {
        // اضافه کردن کارمندان نمونه
        employees.add(new Employee("EMP001", "احمد", "رضایی", "ahmad", "ahmad@library.edu", "ADMIN"));
        employees.add(new Employee("EMP002", "مریم", "محمودی", "maryam", "maryam@library.edu", "LIBRARIAN"));
        employees.add(new Employee("EMP003", "محمد", "کریمی", "mohammad", "mohammad@library.edu", "LIBRARIAN"));
        employeeCounter = 4;
    }
    
    // POST /api/admin/employees
    public ResponseEntity<Employee> createEmployee(String firstName, String lastName, 
                                                   String username, String email, String role) {
        try {
            // بررسی تکراری نبودن username
            for (Employee emp : employees) {
                if (emp.getUsername().equals(username)) {
                    return new ResponseEntity<>(null, 400, "نام کاربری تکراری است");
                }
            }
            
            // ایجاد کارمند جدید
            String employeeId = "EMP" + String.format("%03d", employeeCounter++);
            Employee newEmployee = new Employee(employeeId, firstName, lastName, username, email, role);
            
            employees.add(newEmployee);
            
            return new ResponseEntity<>(newEmployee, 201, "کارمند با موفقیت ایجاد شد");
            
        } catch (Exception e) {
            return new ResponseEntity<>(null, 500, "خطا در ایجاد کارمند: " + e.getMessage());
        }
    }
    
    // GET /api/admin/employees
    public ResponseEntity<List<Employee>> getAllEmployees() {
        try {
            return new ResponseEntity<>(new ArrayList<>(employees), 200, "لیست کارمندان دریافت شد");
        } catch (Exception e) {
            return new ResponseEntity<>(null, 500, "خطا در دریافت لیست کارمندان");
        }
    }
    
    // GET /api/admin/employees/{id}
    public ResponseEntity<Employee> getEmployeeById(String id) {
        try {
            for (Employee emp : employees) {
                if (emp.getId().equals(id)) {
                    return new ResponseEntity<>(emp, 200, "کارمند یافت شد");
                }
            }
            return new ResponseEntity<>(null, 404, "کارمند با شناسه " + id + " یافت نشد");
        } catch (Exception e) {
            return new ResponseEntity<>(null, 500, "خطا در دریافت اطلاعات کارمند");
        }
    }
    
    // PUT /api/admin/employees/{id}/status
    public ResponseEntity<Employee> updateEmployeeStatus(String id, boolean active) {
        try {
            for (Employee emp : employees) {
                if (emp.getId().equals(id)) {
                    emp.setActive(active);
                    return new ResponseEntity<>(emp, 200, "وضعیت کارمند به روز شد");
                }
            }
            return new ResponseEntity<>(null, 404, "کارمند با شناسه " + id + " یافت نشد");
        } catch (Exception e) {
            return new ResponseEntity<>(null, 500, "خطا در به‌روزرسانی وضعیت کارمند");
        }
    }
    
    // DELETE /api/admin/employees/{id}
    public ResponseEntity<String> deleteEmployee(String id) {
        try {
            for (int i = 0; i < employees.size(); i++) {
                if (employees.get(i).getId().equals(id)) {
                    employees.remove(i);
                    return new ResponseEntity<>("کارمند حذف شد", 200, "کارمند با موفقیت حذف شد");
                }
            }
            return new ResponseEntity<>(null, 404, "کارمند با شناسه " + id + " یافت نشد");
        } catch (Exception e) {
            return new ResponseEntity<>(null, 500, "خطا در حذف کارمند");
        }
    }
    
    // تعداد کارمندان فعال
    public int getActiveEmployeesCount() {
        int count = 0;
        for (Employee emp : employees) {
            if (emp.isActive()) {
                count++;
            }
        }
        return count;
    }
    
    // دریافت کارمندان بر اساس نقش
    public ResponseEntity<List<Employee>> getEmployeesByRole(String role) {
        try {
            List<Employee> result = new ArrayList<>();
            for (Employee emp : employees) {
                if (emp.getRole().equals(role)) {
                    result.add(emp);
                }
            }
            return new ResponseEntity<>(result, 200, "کارمندان بر اساس نقش دریافت شدند");
        } catch (Exception e) {
            return new ResponseEntity<>(null, 500, "خطا در فیلتر کردن کارمندان");
        }
    }
    
    // متد تست
    public static void main(String[] args) {
        AdminController controller = new AdminController();
        
        System.out.println("🧪 تست AdminController");
        System.out.println("=======================");
        
        // تست دریافت تمام کارمندان
        ResponseEntity<List<Employee>> allEmployees = controller.getAllEmployees();
        System.out.println("1. دریافت تمام کارمندان:");
        System.out.println("   وضعیت: " + allEmployees.getStatusCode());
        System.out.println("   تعداد: " + (allEmployees.getBody() != null ? allEmployees.getBody().size() : 0));
        
        // تست ایجاد کارمند جدید
        System.out.println("\n2. ایجاد کارمند جدید:");
        ResponseEntity<Employee> newEmp = controller.createEmployee(
            "زهرا", "احمدی", "zahra", "zahra@library.edu", "LIBRARIAN"
        );
        System.out.println("   وضعیت: " + newEmp.getStatusCode());
        if (newEmp.getBody() != null) {
            System.out.println("   کارمند جدید: " + newEmp.getBody().getFirstName() + " " + newEmp.getBody().getLastName());
        }
        
        // تست دریافت کارمند با ID
        System.out.println("\n3. دریافت کارمند با ID:");
        ResponseEntity<Employee> empById = controller.getEmployeeById("EMP001");
        System.out.println("   وضعیت: " + empById.getStatusCode());
        if (empById.getBody() != null) {
            System.out.println("   نام کارمند: " + empById.getBody().getFirstName());
        }
        
        // تست تغییر وضعیت کارمند
        System.out.println("\n4. تغییر وضعیت کارمند:");
        ResponseEntity<Employee> statusUpdate = controller.updateEmployeeStatus("EMP002", false);
        System.out.println("   وضعیت: " + statusUpdate.getStatusCode());
        
        // تست تعداد کارمندان فعال
        System.out.println("\n5. آمار کارمندان:");
        System.out.println("   تعداد کل کارمندان: " + controller.employees.size());
        System.out.println("   تعداد کارمندان فعال: " + controller.getActiveEmployeesCount());
        
        System.out.println("\n✅ تمام تست‌ها با موفقیت انجام شد!");
    }
}