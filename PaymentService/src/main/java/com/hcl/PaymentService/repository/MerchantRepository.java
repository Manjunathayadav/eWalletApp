package com.hcl.PaymentService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcl.PaymentService.entity.Merchant;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {

}
