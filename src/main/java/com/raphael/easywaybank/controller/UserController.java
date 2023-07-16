package com.raphael.easywaybank.controller;

import com.raphael.easywaybank.dto.request.CreditDebitRequest;
import com.raphael.easywaybank.dto.request.EnquiryRequest;
import com.raphael.easywaybank.dto.request.TransferRequest;
import com.raphael.easywaybank.dto.request.UserRequest;
import com.raphael.easywaybank.dto.response.BankResponse;
import com.raphael.easywaybank.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
        return userService.createAccount(userRequest);
    }

    // TODO: This end point allows us to check account balance given a particular account number
    @GetMapping("balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request){
        return userService.balanceEnquiry(request);
    }

    // TODO: This end point allows us to check account name given a particular account number
    @GetMapping("nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest request){
        return userService.nameEnquiry(request);
    }

    // TODO: Credit is a one transaction
    @PostMapping("credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest request){
        return userService.creditAccount(request);
    }

    // TODO: Debit is a one transaction
    @PostMapping("debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest request){
        return userService.debitAccount(request);
    }


    // TODO: Transfer is a two-way transaction i.e. debit one account and credit another account simultaneously
    @PostMapping("transfer")
    public BankResponse transfer(@RequestBody TransferRequest request){
        return userService.transfer(request);
    }

}
