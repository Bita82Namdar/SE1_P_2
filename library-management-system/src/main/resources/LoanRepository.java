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
    }

    public static synchronized LoanRepository getInstance() {
        if (instance == null) {
            instance = new LoanRepository();
        }
        return instance;
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
                .filter(loan -> loan.getEmployeeId().equals(employeeId))
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
                        (loan.getStartDate().equals(today) || loan.getStartDate().equals(today.minusDays(1))))
                .collect(Collectors.toList());
    }

    public List<Loan> getActiveLoans() {
        return loans.stream()
                .filter(loan -> loan.getStatus() == LoanStatus.BORROWED)
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
}
