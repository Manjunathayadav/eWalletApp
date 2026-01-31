package com.walletService.service;

import com.walletService.entity.Wallet;
import com.walletService.exception.WalletNotFoundException;
import com.walletService.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Transactional
    public Wallet createWallet(Long customerId, String currency) {

        Wallet wallet = new Wallet();
        wallet.setCustomerId(customerId);
        wallet.setCurrency(currency);
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setStatus("ACTIVE");

        return walletRepository.save(wallet);
    }

    public Wallet getWallet(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() ->
                        new WalletNotFoundException("Wallet not found with id: " + walletId));
    }

    @Transactional
    public Wallet deposit(Long walletId, BigDecimal amount) {

        Wallet wallet = getWallet(walletId);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }

        wallet.setBalance(wallet.getBalance().add(amount));
        return walletRepository.save(wallet);
    }


    @Transactional
    public Wallet withdraw(Long walletId, BigDecimal amount) {

        Wallet wallet = getWallet(walletId);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdraw amount must be greater than zero");
        }

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient wallet balance");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        return walletRepository.save(wallet);
    }
}
