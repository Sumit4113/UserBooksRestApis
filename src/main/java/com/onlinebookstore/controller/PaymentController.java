package com.onlinebookstore.controller;

import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinebookstore.dto.PaymentRequest;
import com.onlinebookstore.dto.PaymentVerifyRequest;
import com.onlinebookstore.entity.AppUser;
import com.onlinebookstore.entity.BookAdd;
import com.onlinebookstore.repository.BookRepository;
import com.onlinebookstore.repository.PurchaseRepository;
import com.onlinebookstore.repository.UserRepository;
import com.onlinebookstore.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;


@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String secret;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private PurchaseRepository purchaseRepo;
    
    // STEP 1: Create Order
    @PostMapping("/create-order")
    public ResponseEntity<String> createOrder(@RequestBody PaymentRequest request) throws Exception {

        RazorpayClient client = new RazorpayClient(keyId, secret);

        JSONObject object = new JSONObject();
        object.put("amount", request.getAmount() * 100);
        object.put("currency", "INR");
        object.put("receipt", "txn_" + System.currentTimeMillis());

        Order order = client.orders.create(object);

        AppUser user = userRepository.findById(request.getUserId()).orElseThrow();
        BookAdd book = bookRepo.findById(request.getBookId()).orElseThrow();

        paymentService.createPayment(user, book, String.valueOf(request.getAmount()), order.get("id"));

        return ResponseEntity.ok(order.toString());
    }

    // STEP 2: VERIFY PAYMENT
    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(@RequestBody PaymentVerifyRequest request) {

        paymentService.updatePaymentSuccess(
                request.getRazorpayOrderId(),
                request.getRazorpayPaymentId()
        );

        return ResponseEntity.ok("Payment Verified");
    }
    
    @GetMapping("/purchase/check/{bookId}")
    public ResponseEntity<Boolean> checkPurchase(
            @PathVariable UUID bookId,
            Authentication auth) {

        AppUser user = userRepository.findByUserEmail(auth.getName());
        BookAdd book = bookRepo.findById(bookId).orElseThrow();

        boolean isPurchased = purchaseRepo.existsByUserAndBook(user, book);

        return ResponseEntity.ok(isPurchased);
    }
}