package com.raphael.easywaybank.service;

import com.raphael.easywaybank.dto.request.CreditDebitRequest;
import com.raphael.easywaybank.dto.request.EnquiryRequest;
import com.raphael.easywaybank.dto.request.TransferRequest;
import com.raphael.easywaybank.dto.request.UserRequest;
import com.raphael.easywaybank.dto.response.BankResponse;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(EnquiryRequest request);
    String nameEnquiry(EnquiryRequest request);
    BankResponse creditAccount(CreditDebitRequest request);
    BankResponse debitAccount(CreditDebitRequest request);

    BankResponse transfer(TransferRequest request);
}
