package com.hcl.PaymentService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public class DebitWalletRequest {

	int wallet id;
	String amount;
	int transactionId;

	public int getWalletId() {
    return walletId;
}

public void setWalletId(int walletId) {
    this.walletId = walletId;
}

public String getAmount() {
    return amount;
}

public void setAmount(String amount) {
    this.amount = amount;
}

public int getTransactionId() {
    return transactionId;
}

public void setTransactionId(int transactionId) {
    this.transactionId = transactionId;
}

}
