package com.hcl.PaymentService.request;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PaymentRequest {

	@NotNull(message = "Customer ID is required")
	@Schema(description = "Customer unique ID", example = "1", required = true)
	private Long customerId;

	@NotNull(message = "Merchant ID is required")
	@Schema(description = "Merchant unique ID", example = "2", required = true)
	private Long merchantId;

	@NotNull(message = "Amount is required")
	@DecimalMin(value = "1.00", message = "Amount must be greater than zero")
	@Digits(integer = 13, fraction = 2, message = "Invalid amount format")
	@Schema(description = "Payment amount", example = "2000.00", required = true)
	private BigDecimal amount;

	@NotBlank(message = "Currency is required")
	@Size(max = 10, message = "Currency length should not exceed 10 characters")
	@Schema(description = "Currency code", example = "INR", required = true)
	private String currency;

	@Size(max = 100, message = "Description cannot exceed 100 characters")
	@Schema(description = "Optional payment description", example = "Payment for electronics purchase")
	private String description;

	@Size(max = 100, message = "Idempotency key length exceeded")
	@Schema(description = "Unique idempotency key to prevent duplicate payment", example = "PAYMENT-REQ-12345")
	private String idempotencyKey;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIdempotencyKey() {
		return idempotencyKey;
	}

	public void setIdempotencyKey(String idempotencyKey) {
		this.idempotencyKey = idempotencyKey;
	}

	@Override
	public String toString() {
		return "PaymentRequest [customerId=" + customerId + ", merchantId=" + merchantId + ", amount=" + amount
				+ ", currency=" + currency + ", description=" + description + ", idempotencyKey=" + idempotencyKey
				+ "]";
	}
	
	

}
