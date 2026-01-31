package com.walletService.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 15)
    private String mobile;

    @Column(length = 20)
    private String status = "ACTIVE";
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Wallet> wallets;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
