package com.hcl.PaymentService.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcl.PaymentService.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	Optional<Transaction> findByTransactionRef(String transactionRef);

	List<Transaction> findByCustomerId(Long customerId);

}
