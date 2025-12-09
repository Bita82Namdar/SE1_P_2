package com.university.library.model;

public enum LoanStatus {
    REQUESTED,      // درخواست داده شده (PENDING در سناریوها)
    APPROVED,       // تایید شده توسط کارمند
    BORROWED,       // کتاب تحویل داده شده
    RETURNED,       // کتاب برگردانده شده
    REJECTED,       // رد شده
    OVERDUE,        // دیرکرد در برگرداندن
    CANCELLED       // لغو شده
}