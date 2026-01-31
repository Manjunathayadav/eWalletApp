package com.hcl.PaymentService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.PaymentService.request.PaymentRequest;
import com.hcl.PaymentService.response.PaymentResponse;
import com.hcl.PaymentService.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Payment APIs")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@PostMapping("/initiate")
	@Operation(summary = "Initiate payment")
	public ResponseEntity<PaymentResponse> initiate(@Valid @RequestBody PaymentRequest request) {

		log.info("Initiating payment for customerId: {}", request.getCustomerId());

		return ResponseEntity.ok(paymentService.processPayment(request));
	}
	
	@GetMapping("/{transactionRef}")
	@Operation(summary = "Get payment details by transaction reference")
	public ResponseEntity<PaymentResponse> getPayment(
	        @PathVariable String transactionRef) {

	    log.info("Fetching payment details for transactionRef={}", transactionRef);

	    return ResponseEntity.ok(paymentService.getPaymentByRef(transactionRef));
	}
}
