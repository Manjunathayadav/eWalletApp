package com.hcl.PaymentService.exception;

public class InsufficientBalanceException extends BaseException {
	public InsufficientBalanceException(String msg) {
		super("INSUFFICIENT_BALANCE", msg);
	}
}
