package com.hcl.PaymentService.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.PaymentService.entity.AuditLog;
import com.hcl.PaymentService.entity.Merchant;
import com.hcl.PaymentService.entity.Transaction;
import com.hcl.PaymentService.entity.Wallet;
import com.hcl.PaymentService.exception.InsufficientBalanceException;
import com.hcl.PaymentService.exception.InvalidCurrencyException;
import com.hcl.PaymentService.exception.MerchantNotFoundException;
import com.hcl.PaymentService.exception.PaymentNotFoundException;
import com.hcl.PaymentService.exception.PaymentProcessingException;
import com.hcl.PaymentService.exception.WalletNotFoundException;
import com.hcl.PaymentService.repository.AuditLogRepository;
import com.hcl.PaymentService.repository.MerchantRepository;
import com.hcl.PaymentService.repository.TransactionRepository;
import com.hcl.PaymentService.repository.WalletRepository;
import com.hcl.PaymentService.request.PaymentRequest;
import com.hcl.PaymentService.response.PaymentResponse;
import com.hcl.PaymentService.responseEnum.PaymentStatus;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Builder
public class PaymentService {

	@Autowired
	private WalletRepository walletRepository;
	@Autowired
	private MerchantRepository merchantRepository;
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private AuditLogRepository auditLogRepository;

	@Transactional
	public PaymentResponse processPayment(PaymentRequest request) {

		log.info("Payment initiated for customerId={}, merchantId={}, amount={}", request.getCustomerId(),
				request.getMerchantId(), request.getAmount());

		// 🔹 Idempotency Check
		if (request.getIdempotencyKey() != null) {
			transactionRepository.findByTransactionRef(request.getIdempotencyKey()).ifPresent(existingTxn -> {
				log.warn("Duplicate payment request detected for key={}", request.getIdempotencyKey());
				throw new PaymentProcessingException("Duplicate payment request");
			});
		}

		// 🔹 Fetch Wallet
		Wallet wallet = walletRepository.findByCustomerId(request.getCustomerId())
				.orElseThrow(() -> new WalletNotFoundException("Wallet not found"));

		// 🔹 Validate Currency
		if (!wallet.getCurrency().equalsIgnoreCase(request.getCurrency())) {
			log.error("Invalid currency for customerId={}", request.getCustomerId());
			throw new InvalidCurrencyException("Currency mismatch");
		}

		// 🔹 Validate Balance
		if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
			log.error("Insufficient balance for customerId={}", request.getCustomerId());
			throw new InsufficientBalanceException("Insufficient balance");
		}

		// 🔹 Fetch Merchant
		Merchant merchant = merchantRepository.findById(request.getMerchantId())
				.orElseThrow(() -> new MerchantNotFoundException("Merchant not found"));

		// 🔹 Calculate Fee (2%)
		BigDecimal fee = request.getAmount().multiply(new BigDecimal("0.02"));

		BigDecimal merchantCredit = request.getAmount().subtract(fee);

		// 🔹 Update Wallet & Merchant
		wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
		merchant.setBalance(merchant.getBalance().add(merchantCredit));

		walletRepository.save(wallet);
		merchantRepository.save(merchant);

		// 🔹 Create Transaction
		String txnRef = UUID.randomUUID().toString();

		Transaction transaction = Transaction.builder().transactionRef(txnRef).customer(wallet.getCustomer())
				.merchant(merchant).amount(request.getAmount()).walletFee(fee).currency(request.getCurrency())
				.status(PaymentStatus.SUCCESS.name()).createdAt(LocalDateTime.now()).build();

		transactionRepository.save(transaction);

		// 🔹 Audit Log
		AuditLog audit = AuditLog.builder().transactionRef(txnRef).action("PAYMENT_SUCCESS").status("SUCCESS")
				.message("Payment processed successfully").createdAt(LocalDateTime.now()).build();

		auditLogRepository.save(audit);

		log.info("Payment successful. transactionRef={}", txnRef);

		// 🔹 Build Response
		return PaymentResponse.builder().transactionRef(txnRef).customerId(request.getCustomerId())
				.merchantId(request.getMerchantId()).amount(request.getAmount()).walletFee(fee)
				.currency(request.getCurrency()).status(PaymentStatus.SUCCESS).message("Payment processed successfully")
				.timestamp(LocalDateTime.now()).build();
	}

	public PaymentResponse getPaymentByRef(String transactionRef) {

		Transaction transaction = transactionRepository.findByTransactionRef(transactionRef)
				.orElseThrow(() -> new PaymentNotFoundException("Transaction not found"));
		

		log.info("Transaction found for ref={}", transactionRef);

		return mapToResponse(transaction);
	}

	private PaymentResponse mapToResponse(Transaction txn) {
		return PaymentResponse.builder().transactionRef(txn.getTransactionRef()).customerId(txn.getCustomer().getId())
				.merchantId(txn.getMerchant().getId()).amount(txn.getAmount()).walletFee(txn.getWalletFee())
				.currency(txn.getCurrency()).status(PaymentStatus.valueOf(txn.getStatus()))
				.message("Transaction fetched successfully").timestamp(txn.getCreatedAt()).build();
	}
}
