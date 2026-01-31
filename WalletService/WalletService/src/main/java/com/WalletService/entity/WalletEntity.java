package com.hcl.WalletService;

import org.springframework.boot.autoconfigure.SpringBootApplication;

public class WalletService {

	
	ackage com.example.wallet.entity;

	import javax.persistence.*;
	import java.math.BigDecimal;
	import java.time.LocalDateTime;

	@Entity
	@Table(name = "wallet")
	public class Wallet {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(name = "customer_id", nullable = false)
	    private Long customerId;

	    @Column(nullable = false, length = 10)
	    private String currency;

	    @Column(nullable = false, precision = 15, scale = 2)
	    private BigDecimal balance = BigDecimal.ZERO;

	    @Column(length = 20)
	    private String status = "ACTIVE";

	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;

	    @PrePersist
	    public void onCreate() {
	        createdAt = LocalDateTime.now();
	        updatedAt = LocalDateTime.now();
	    }

	    @PreUpdate
	    public void onUpdate() {
	        updatedAt = LocalDateTime.now();
	    }

	    // getters and setters
	}

}
