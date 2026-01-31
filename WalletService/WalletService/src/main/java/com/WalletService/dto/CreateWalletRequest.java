package com.hcl.PaymentService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public class CreateWalletRequest {

	private String currency;
	private String status;
	private Long balance;
	private String walletId;
	private String customerId;

	public String getCurrency() {
    return currency;
}

public void setCurrency(String currency) {
    this.currency = currency;
}

public String getStatus() {
    return status;
}

public void setStatus(String status) {
    this.status = status;
}

public Long getBalance() {
    return balance;
}

public void setBalance(Long balance) {
    this.balance = balance;
}

public String getWalletId() {
    return walletId;
}

public void setWalletId(String walletId) {
    this.walletId = walletId;
}

public String getCustomerId() {
    return customerId;
}

public void setCustomerId(String customerId) {
    this.customerId = customerId;
}

	

}
