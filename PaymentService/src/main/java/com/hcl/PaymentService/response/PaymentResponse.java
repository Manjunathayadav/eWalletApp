package com.hcl.PaymentService.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.hcl.PaymentService.responseEnum.PaymentStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

	@Schema(description = "Unique transaction reference", example = "TXN-123456789")
	private String transactionRef;

	@Schema(description = "Customer ID", example = "1")
	private Long customerId;

	@Schema(description = "Merchant ID", example = "2")
	private Long merchantId;

	@Schema(description = "Transaction amount", example = "2000.00")
	private BigDecimal amount;

	@Schema(description = "Wallet fee deducted", example = "40.00")
	private BigDecimal walletFee;

	@Schema(description = "Currency code", example = "INR")
	private String currency;

	@Schema(description = "Transaction status", example = "SUCCESS")
	private PaymentStatus status;

	@Schema(description = "Response message", example = "Payment processed successfully")
	private String message;

	@Schema(description = "Transaction timestamp")
	private LocalDateTime timestamp;

}
