package com.onlinebookstore.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinebookstore.entity.AppUser;
import com.onlinebookstore.entity.BookAdd;
import com.onlinebookstore.entity.Payment;
import com.onlinebookstore.entity.Purchase;
import com.onlinebookstore.repository.PaymentRepository;
import com.onlinebookstore.repository.PurchaseRepository;
import com.onlinebookstore.repository.UserRepository;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    public Payment createPayment(AppUser user, BookAdd book, String amount, String orderId) {

    	
    	
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setBook(book);
        payment.setAmount(amount);
        payment.setRazorpayOrderId(orderId);
        payment.setPaymentStatus("PENDING");
        payment.setPaymentDate(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    public Payment updatePaymentSuccess(String orderId, String paymentId) {

        Payment payment = paymentRepository.findByRazorpayOrderId(orderId);

        if (payment == null) {
            throw new RuntimeException("Payment not found for orderId" + orderId);
        }

        payment.setRazorpayPaymentId(paymentId);
        payment.setPaymentStatus("SUCCESS");

        Payment saved = paymentRepository.save(payment);

        // 🔥 CREATE PURCHASE (THIS GIVES ACCESS)
        Purchase purchase = new Purchase();
        purchase.setUser(payment.getUser());
        purchase.setBook(payment.getBook());
        purchase.setPaymentId(paymentId);
        purchase.setPurchasedAt(LocalDateTime.now());

        purchaseRepository.save(purchase);

        return saved;
    }
}