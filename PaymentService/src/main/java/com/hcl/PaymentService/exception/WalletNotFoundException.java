package com.hcl.PaymentService.exception;

public class WalletNotFoundException extends BaseException {
	public WalletNotFoundException(String msg) {
		super("WALLET_NOT_FOUND", msg);
	}
}