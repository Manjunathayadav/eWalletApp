package com.walletService.controller;

import com.walletService.dto.AmountRequest;
import com.walletService.dto.CreateWalletRequest;
import com.walletService.entity.Wallet;
import com.walletService.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<Wallet> createWallet(
            @Valid @RequestBody CreateWalletRequest request) {

        Wallet wallet = walletService.createWallet(
                request.getCustomerId(),
                request.getCurrency()
        );

        return new ResponseEntity<>(wallet, HttpStatus.CREATED);
    }


    @GetMapping("/{walletId}")
    public ResponseEntity<Wallet> getWallet(
            @PathVariable Long walletId) {

        Wallet wallet = walletService.getWallet(walletId);
        return ResponseEntity.ok(wallet);
    }

    @PostMapping("/{walletId}/deposit")
    public ResponseEntity<Wallet> deposit(
            @PathVariable Long walletId,
            @Valid @RequestBody AmountRequest request) {

        Wallet wallet = walletService.deposit(
                walletId,
                request.getAmount()
        );

        return ResponseEntity.ok(wallet);
    }


    @PostMapping("/{walletId}/withdraw")
    public ResponseEntity<Wallet> withdraw(
            @PathVariable Long walletId,
            @Valid @RequestBody AmountRequest request) {

        Wallet wallet = walletService.withdraw(
                walletId,
                request.getAmount()
        );

        return ResponseEntity.ok(wallet);
    }
}
