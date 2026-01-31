package com.hcl.PaymentService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public class WalletService {

	String status;

	public void Setstaus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}
