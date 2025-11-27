package com.university.library.repository;
import com.university.library.model.Loan;
import com.university.library.model.LoanStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LoanRepository {
    private List<Loan> loans;
    private static LoanRepository instance;

    private LoanRepository() {
        this.loans = new ArrayList<>();
        initializeSampleData();
    }

    public static synchronized LoanRepository getInstance() {
        if (instance == null) {
            instance = new LoanRepository();
        }
        return instance;
    }

    private void initializeSampleData() {
        // اضافه کردن برخی داده‌های نمونه برای تست
        LocalDate today = LocalDate.now();
        Loan loan1 = new Loan("L1", "S1", "B1", "E1", 
                today.minusDays(5), today.plusDays(10));
        loan1.borrow();
        
        Loan loan2 = new Loan("L2", "S2", "B2", null, 
                today.plusDays(1), today.plusDays(15));
        
        loans.add(loan1);
        loans.add(loan2);
    }

    public void addLoan(Loan loan) {
        loans.add(loan);
    }

    public Optional<Loan> findById(String loanId) {
        return loans.stream()
                .filter(loan -> loan.getLoanId().equals(loanId))
                .findFirst();
    }

    public List<Loan> findByStudentId(String studentId) {
        return loans.stream()
                .filter(loan -> loan.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    public List<Loan> findByBookId(String bookId) {
        return loans.stream()
                .filter(loan -> loan.getBookId().equals(bookId))
                .collect(Collectors.toList());
    }

    public List<Loan> findByEmployeeId(String employeeId) {
        return loans.stream()
                .filter(loan -> employeeId.equals(loan.getEmployeeId()))
                .collect(Collectors.toList());
    }

    public List<Loan> findPendingLoans() {
        return loans.stream()
                .filter(loan -> loan.getStatus() == LoanStatus.REQUESTED)
                .collect(Collectors.toList());
    }

    public List<Loan> findLoansForApproval() {
        LocalDate today = LocalDate.now();
        return loans.stream()
                .filter(loan -> loan.getStatus() == LoanStatus.REQUESTED &&
                        (loan.getStartDate().isEqual(today) || 
                         loan.getStartDate().isEqual(today.minusDays(1))))
                .collect(Collectors.toList());
    }

    public List<Loan> getActiveLoans() {
        return loans.stream()
                .filter(loan -> loan.getStatus() == LoanStatus.BORROWED || 
                               loan.getStatus() == LoanStatus.OVERDUE)
                .collect(Collectors.toList());
    }

    public List<Loan> getOverdueLoans() {
        return loans.stream()
                .filter(Loan::isOverdue)
                .collect(Collectors.toList());
    }

    public boolean updateLoan(Loan updatedLoan) {
        for (int i = 0; i < loans.size(); i++) {
            if (loans.get(i).getLoanId().equals(updatedLoan.getLoanId())) {
                loans.set(i, updatedLoan);
                return true;
            }
        }
        return false;
    }

    public int getTotalLoansCount() {
        return loans.size();
    }

    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }

    // متدهای جدید برای آمار و گزارش‌گیری
    public long getBorrowedBooksCount() {
        return loans.stream()
                .filter(loan -> loan.getStatus() == LoanStatus.BORROWED)
                .count();
    }

    public long getReturnedBooksCount() {
        return loans.stream()
                .filter(loan -> loan.getStatus() == LoanStatus.RETURNED)
                .count();
    }

    public long getOverdueLoansCount() {
        return loans.stream()
                .filter(Loan::isOverdue)
                .count();
    }

    public List<Loan> findLoansByStatus(LoanStatus status) {
        return loans.stream()
                .filter(loan -> loan.getStatus() == status)
                .collect(Collectors.toList());
    }
}