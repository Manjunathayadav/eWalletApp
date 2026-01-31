package com.hcl.PaymentService.exception;

public class PaymentProcessingException extends BaseException {
	public PaymentProcessingException(String msg) {
		super("PAYMENT_PROCESSING_ERROR",msg);
	}
}