package com.raphael.easywaybank.service.impl;


import com.raphael.easywaybank.data.model.User;
import com.raphael.easywaybank.data.repository.UserRepository;
import com.raphael.easywaybank.dto.request.*;
import com.raphael.easywaybank.dto.response.AccountInfo;
import com.raphael.easywaybank.dto.response.BankResponse;
import com.raphael.easywaybank.service.transactionService.TransactionService;
import com.raphael.easywaybank.service.UserService;
import com.raphael.easywaybank.service.mailSerivce.EmailService;
import com.raphael.easywaybank.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final EmailService emailService;

    private final TransactionService transactionService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, TransactionService transactionService, EmailService emailService){
        this.userRepository = userRepository;
        this.transactionService = transactionService;
        this.emailService = emailService;
    }

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        /**
         * Creating an account - saving a new user into the db
         */

        // TODO: check if user already has an account -> If not
        // TODO: generate account number
        // TODO: save to db

        if (userRepository.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null) // This means that the user who wants to create an account does not have an AccountInfo
                    .build();
        }
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO) // set a default balance of 0 at the point of account creation
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE") // once the account is created, it should be active
                .build();

        User savedUser = userRepository.save(newUser);
        //Send email Alert
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulations! Your Account Has been Successfully Created.\nYour Account Details: \n" +
                        "Account Name: " + savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName() + "\nAccount Number: " + savedUser.getAccountNumber())
                .build();
        emailService.sendEmail(emailDetails);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
                        .build())
                .build();

    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
        // TODO: check if the provided account number exists in the db

        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(request.getAccountNumber())
                        .accountName(foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        //TODO: check if the provided account number exists in the db

        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist){
            return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
        }
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        // TODO: check if the account to be credited exists
        // TODO: If no, return a custom bankResponse
        // TODO: If yes, Get the account from the UserRepository
        // TODO: Credit the account
        // TODO: Update the account balance in the db
        // TODO: Build & Save the transaction details
        // TODO:
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        userRepository.save(userToCredit);

        TransactionDetails transactionDetails = TransactionDetails
                .builder()
                .transactionType("CREDIT")
                .accountNumber(userToCredit.getAccountNumber())
                .amount(request.getAmount())
                .build();

        transactionService.saveTransaction(transactionDetails);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName() + " " + userToCredit.getOtherName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(request.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        // TODO: check if the account exists
        // TODO: check if the amount you intend to withdraw is not more than the current account balance
        // TODO: check if the debit amount is not more than the current balance

        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());
        // here we converted the current balance of the account to Big Integer: this is not advisable as it will lead to loss of precision
        BigInteger availableBalance =userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = request.getAmount().toBigInteger();
        if ( availableBalance.intValue() < debitAmount.intValue()){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
            userRepository.save(userToDebit);

            TransactionDetails transactionDetails = TransactionDetails
                    .builder()
                    .transactionType("DEBIT")
                    .accountNumber(userToDebit.getAccountNumber())
                    .amount(request.getAmount())
                    .build();

            transactionService.saveTransaction(transactionDetails);

            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountNumber(request.getAccountNumber())
                            .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName() + " " + userToDebit.getOtherName())
                            .accountBalance(userToDebit.getAccountBalance())
                            .build())
                    .build();
        }

    }

    public BankResponse transfer(TransferRequest request){
        // TODO: The source account is the currently logged in account. Therefore, we don't need to check if it exist
        // TODO: get the source account from the db
        // TODO: check if the destination account exist
        // TODO: check if the transfer amount is not more than the source account balance
        // TODO: debit the source account
        // TODO: Update the source account in the db
        // TODO: Build & Send a debit alert to the source account
        // TODO: Get the destination account from the db
        // TODO: Credit the destination account
        // TODO: Update the destination account in the db
        // TODO: Build & Send a credit alert to the destination account
        // TODO: Send a transfer response to the source account


        boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
        if (!isDestinationAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User sourceAccount = userRepository.findByAccountNumber(request.getSourceAccountNumber());
        // TODO: We are comparing 2 Big Decimal numbers here
        // When comparing big decimal numbers, you either get a 1 or -1 it is big decimal you are
        if (request.getTransferAmount().compareTo(sourceAccount.getAccountBalance()) > 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        sourceAccount.setAccountBalance(sourceAccount.getAccountBalance().subtract(request.getTransferAmount()));
        userRepository.save(sourceAccount);

        EmailDetails debitAlert = EmailDetails.builder()
                .recipient(sourceAccount.getEmail())
                .subject("DEBIT ALERT")
                .messageBody("The sum of " + request.getTransferAmount() + " has been deducted from your account! Your current balance is " + sourceAccount.getAccountBalance())
                .build();
        emailService.sendEmail(debitAlert);

        User destinationAccount = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        destinationAccount.setAccountBalance(destinationAccount.getAccountBalance().add(request.getTransferAmount()));
        userRepository.save(destinationAccount);

        EmailDetails creditAlert = EmailDetails.builder()
                .recipient(destinationAccount.getEmail())
                .subject("CREDIT ALERT")
                .messageBody("The sum of " + request.getTransferAmount() +
                        " has been sent to your account from "
                                + sourceAccount.getFirstName() + " " + sourceAccount.getLastName() + " " + sourceAccount.getOtherName() +
                        " Your current balance is " + destinationAccount.getAccountBalance())
                .build();
        emailService.sendEmail(creditAlert);

        TransactionDetails transactionDetails = TransactionDetails
                .builder()
                .transactionType("Transfer")
                .accountNumber(destinationAccount.getAccountNumber())
                .amount(request.getTransferAmount())
                .build();

        transactionService.saveTransaction(transactionDetails);


        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                .accountInfo(null)
                .build();
    }

}
