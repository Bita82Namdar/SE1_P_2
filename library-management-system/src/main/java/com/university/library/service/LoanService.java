package com.university.library.service;

import com.university.library.model.Book;
import com.university.library.model.Loan;
import com.university.library.model.User;
import com.university.library.repository.BookRepository;
import com.university.library.repository.LoanRepository;
import com.university.library.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class LoanService {
    private LoanRepository loanRepository;
    private BookRepository bookRepository;
    private UserRepository userRepository;

    public LoanService() {
        this.loanRepository = LoanRepository.getInstance();
        this.bookRepository = BookRepository.getInstance();
        this.userRepository = UserRepository.getInstance();
    }

    public Optional<Loan> requestLoan(String bookId, String studentId, LocalDate startDate, LocalDate endDate) {
        Optional<Book> book = bookRepository.findById(bookId);
        Optional<User> student = userRepository.findById(studentId);

        if (book.isEmpty() || student.isEmpty() || !student.get().isActive()) {
            return Optional.empty();
        }

        if (!book.get().isAvailable()) {
            return Optional.empty();
        }

        String loanId = "L" + (loanRepository.getAllLoans().size() + 1);
        Loan loan = new Loan(loanId, studentId, bookId, null, startDate, endDate);
        loanRepository.addLoan(loan);
        
        return Optional.of(loan);
    }

    public boolean approveLoan(String loanId, String employeeId) {
        Optional<Loan> loanOpt = loanRepository.findById(loanId);
        Optional<Book> bookOpt;
        
        if (loanOpt.isPresent()) {
            Loan loan = loanOpt.get();
            bookOpt = bookRepository.findById(loan.getBookId());
            
            if (bookOpt.isPresent() && bookOpt.get().isAvailable()) {
                loan.approve(employeeId);
                loan.borrow();
                bookOpt.get().borrowCopy();
                bookRepository.updateBook(bookOpt.get());
                return loanRepository.updateLoan(loan);
            }
        }
        return false;
    }

    public boolean returnLoan(String loanId) {
        Optional<Loan> loanOpt = loanRepository.findById(loanId);
        Optional<Book> bookOpt;
        
        if (loanOpt.isPresent()) {
            Loan loan = loanOpt.get();
            bookOpt = bookRepository.findById(loan.getBookId());
            
            if (bookOpt.isPresent()) {
                loan.returnBook();
                bookOpt.get().returnCopy();
                bookRepository.updateBook(bookOpt.get());
                return loanRepository.updateLoan(loan);
            }
        }
        return false;
    }

    public boolean rejectLoan(String loanId) {
        Optional<Loan> loanOpt = loanRepository.findById(loanId);
        if (loanOpt.isPresent()) {
            Loan loan = loanOpt.get();
            loan.reject();
            return loanRepository.updateLoan(loan);
        }
        return false;
    }

    public List<Loan> getStudentLoanHistory(String studentId) {
        return loanRepository.findByStudentId(studentId);
    }

    public List<Loan> getPendingLoans() {
        return loanRepository.findPendingLoans();
    }

    public List<Loan> getPendingLoansForApproval() {
        return loanRepository.findLoansForApproval();
    }

    public LoanStatistics getStudentLoanStatistics(String studentId) {
        List<Loan> studentLoans = loanRepository.findByStudentId(studentId);
        return new LoanStatistics(studentLoans);
    }

    public int getTotalLoansCount() {
        return loanRepository.getTotalLoansCount();
    }

    public List<Loan> getActiveLoans() {
        return loanRepository.getActiveLoans();
    }

    public List<Loan> getOverdueLoans() {
        return loanRepository.getOverdueLoans();
    }

    public boolean markLoanAsOverdue(String loanId) {
        Optional<Loan> loanOpt = loanRepository.findById(loanId);
        if (loanOpt.isPresent()) {
            Loan loan = loanOpt.get();
            if (loan.isOverdue()) {
                loan.markOverdue();
                return loanRepository.updateLoan(loan);
            }
        }
        return false;
    }

    public List<Loan> getLoansByBookId(String bookId) {
        return loanRepository.findByBookId(bookId);
    }

    public List<Loan> getLoansByEmployeeId(String employeeId) {
        return loanRepository.findByEmployeeId(employeeId);
    }
}
