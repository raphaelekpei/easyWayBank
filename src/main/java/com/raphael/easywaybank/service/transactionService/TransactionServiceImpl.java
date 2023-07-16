package com.raphael.easywaybank.service.transactionService;

import com.raphael.easywaybank.data.model.Transaction;
import com.raphael.easywaybank.data.repository.TransactionRepository;
import com.raphael.easywaybank.dto.request.TransactionDetails;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void saveTransaction(TransactionDetails transactionDetails) {

        Transaction transaction = Transaction.builder()
                .transactionType(transactionDetails.getTransactionType())
                .accountNumber(transactionDetails.getAccountNumber())
                .amount(transactionDetails.getAmount())
                .status("SUCCESS")
                .build();

        transactionRepository.save(transaction);
    }
}
