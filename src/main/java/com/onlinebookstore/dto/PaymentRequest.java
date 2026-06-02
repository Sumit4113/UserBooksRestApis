package com.onlinebookstore.dto;

import java.util.UUID;

public class PaymentRequest {

    private UUID userId;
    private UUID bookId;   // ⭐ ADD THIS (VERY IMPORTANT)
    private double amount;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}