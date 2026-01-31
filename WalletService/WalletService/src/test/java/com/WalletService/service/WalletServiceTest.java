package com.walletService.service;

import com.walletService.entity.Wallet;
import com.walletService.exception.WalletNotFoundException;
import com.walletService.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;


    @Test
    void createWallet_shouldCreateWalletWithZeroBalance() {
        Wallet savedWallet = new Wallet();
        savedWallet.setCustomerId(1L);
        savedWallet.setCurrency("INR");
        savedWallet.setBalance(BigDecimal.ZERO);
        savedWallet.setStatus("ACTIVE");

        when(walletRepository.save(any(Wallet.class)))
                .thenReturn(savedWallet);

        Wallet result = walletService.createWallet(1L, "INR");

        assertEquals(1L, result.getCustomerId());
        assertEquals("INR", result.getCurrency());
        assertEquals(BigDecimal.ZERO, result.getBalance());
        assertEquals("ACTIVE", result.getStatus());

        verify(walletRepository).save(any(Wallet.class));
    }


    @Test
    void getWallet_shouldReturnWallet_whenWalletExists() {
        Wallet wallet = new Wallet();
        wallet.setBalance(new BigDecimal("500"));

        when(walletRepository.findById(1L))
                .thenReturn(Optional.of(wallet));

        Wallet result = walletService.getWallet(1L);

        assertEquals(new BigDecimal("500"), result.getBalance());
        verify(walletRepository).findById(1L);
    }

    @Test
    void getWallet_shouldThrowException_whenWalletNotFound() {
        when(walletRepository.findById(1L))
                .thenReturn(Optional.empty());

        WalletNotFoundException exception = assertThrows(
                WalletNotFoundException.class,
                () -> walletService.getWallet(1L)
        );

        assertEquals("Wallet not found with id: 1", exception.getMessage());
    }


    @Test
    void deposit_shouldIncreaseBalance() {
        Wallet wallet = new Wallet();
        wallet.setBalance(new BigDecimal("100"));

        when(walletRepository.findById(1L))
                .thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Wallet result = walletService.deposit(1L, new BigDecimal("50"));

        assertEquals(new BigDecimal("150"), result.getBalance());
        verify(walletRepository).save(wallet);
    }

    @Test
    void deposit_shouldThrowException_whenAmountIsZeroOrNegative() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> walletService.deposit(1L, BigDecimal.ZERO)
        );

        assertEquals("Deposit amount must be greater than zero", exception.getMessage());
    }


    @Test
    void withdraw_shouldReduceBalance() {
        Wallet wallet = new Wallet();
        wallet.setBalance(new BigDecimal("200"));

        when(walletRepository.findById(1L))
                .thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Wallet result = walletService.withdraw(1L, new BigDecimal("50"));

        assertEquals(new BigDecimal("150"), result.getBalance());
        verify(walletRepository).save(wallet);
    }

    @Test
    void withdraw_shouldThrowException_whenAmountIsZeroOrNegative() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> walletService.withdraw(1L, BigDecimal.ZERO)
        );

        assertEquals("Withdraw amount must be greater than zero", exception.getMessage());
    }

    @Test
    void withdraw_shouldThrowException_whenInsufficientBalance() {
        Wallet wallet = new Wallet();
        wallet.setBalance(new BigDecimal("30"));

        when(walletRepository.findById(1L))
                .thenReturn(Optional.of(wallet));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> walletService.withdraw(1L, new BigDecimal("100"))
        );

        assertEquals("Insufficient wallet balance", exception.getMessage());
    }
}
