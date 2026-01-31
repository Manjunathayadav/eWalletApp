package com.hcl.PaymentService.exception;

public class PaymentNotFoundException extends BaseException {
	public PaymentNotFoundException(String msg) {
		super("PAYMENT_NOT_FOUND",msg);
	}
}
