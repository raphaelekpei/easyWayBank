package com.raphael.easywaybank.dto;

import java.math.BigDecimal;

public class TransferRequest {
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private BigDecimal amount;

    // sourceAccountNumber = account to be debited
    // destinationAccountNumber = account to be credited
}
