package com.hcl.PaymentService.response;

import java.time.LocalDateTime;

public class ErrorResponse {

	private String errorCode;
	private String message;
	private String path;
	private LocalDateTime timestamp;

	public ErrorResponse() {
	}

	public ErrorResponse(String errorCode, String message, String path) {
		this.errorCode = errorCode;
		this.message = message;
		this.path = path;
		this.timestamp = LocalDateTime.now();
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getMessage() {
		return message;
	}

	public String getPath() {
		return path;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}
}
