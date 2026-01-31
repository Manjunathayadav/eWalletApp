package com.hcl.PaymentService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcl.PaymentService.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByTransactionRef(String transactionRef);

}