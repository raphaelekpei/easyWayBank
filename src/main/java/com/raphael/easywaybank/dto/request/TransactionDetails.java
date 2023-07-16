package com.raphael.easywaybank.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDetails {
    private String transactionType;
    private String accountNumber;
    private BigDecimal amount;
    private String status;
}
