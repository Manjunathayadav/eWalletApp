package com.hcl.PaymentService.exception;

public class BaseException extends RuntimeException {

	private String errorCode;

	public BaseException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}
}
