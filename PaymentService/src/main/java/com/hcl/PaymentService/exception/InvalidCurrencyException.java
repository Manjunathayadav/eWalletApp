package com.hcl.PaymentService.exception;

public class InvalidCurrencyException extends BaseException {
	public InvalidCurrencyException(String msg) {
		super("INVALID_CURRENCY", msg);
	}
}