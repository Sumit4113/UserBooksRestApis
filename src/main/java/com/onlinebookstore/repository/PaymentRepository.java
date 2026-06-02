package com.onlinebookstore.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onlinebookstore.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

	 Payment findByRazorpayOrderId(String orderId);
}
