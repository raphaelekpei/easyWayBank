package com.raphael.easywaybank.service.transactionService;

import com.raphael.easywaybank.dto.request.TransactionDetails;

public interface TransactionService {
    void saveTransaction(TransactionDetails transaction);

}
