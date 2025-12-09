package com.university.library.repository;

import com.university.library.model.Loan;
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
    
    public Loan save(Loan loan) {
        loans.removeIf(l -> l.getLoanId().equals(loan.getLoanId()));
        loans.add(loan);
        return loan;
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
    
    public List<Loan> findAll() {
        return new ArrayList<>(loans);
    }
    
    public void delete(String loanId) {
        loans.removeIf(loan -> loan.getLoanId().equals(loanId));
    }
}