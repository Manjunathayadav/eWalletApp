package com.hcl.PaymentService.exception;

public class MerchantNotFoundException extends BaseException {
	public MerchantNotFoundException(String msg) {
		super("MERCHANT_NOT_FOUND",msg);
	}
}
