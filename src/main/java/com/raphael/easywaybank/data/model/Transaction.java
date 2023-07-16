package com.raphael.easywaybank.data.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // UUID generates a 16-digit character
    private String transactionId;
    private String transactionType;
    private String accountNumber;
    private BigDecimal amount;
    private String status;

}

