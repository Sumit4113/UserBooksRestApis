package com.onlinebookstore.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;

@Controller
@RequestMapping("/payment")
public class PaymentController {
  
	   @Value("${razorpay.key.id}")
	    private String keyId;

	    @Value("${razorpay.key.secret}")
	    private String secret;

	    @PostMapping("/create-order")
	    public String createOrder() throws Exception {

	        RazorpayClient client =
	                new RazorpayClient(keyId, secret);

	        JSONObject object = new JSONObject();

	        object.put("amount", 50000);
	        object.put("currency", "INR");
	        object.put("receipt", "txn_123456");

	        Order order = client.orders.create(object);

	        return order.toString();
	
}
}
