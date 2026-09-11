package com.finbank.account.controller;

import com.finbank.account.dto.AccountResponse;
import com.finbank.account.dto.AmountRequest;
import com.finbank.account.service.AccountService;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{accountNumber}")
    public AccountResponse getByAccountNumber(@PathVariable String accountNumber) {
        return accountService.getByAccountNumber(accountNumber);
    }

    @GetMapping("/{accountNumber}/balance")
    public Map<String, BigDecimal> getBalance(@PathVariable String accountNumber) {
        return Map.of("balance", accountService.getBalance(accountNumber));
    }

    @PutMapping("/{accountNumber}/debit")
    public AccountResponse debit(@PathVariable String accountNumber, @RequestBody AmountRequest request) {
        return accountService.debit(accountNumber, request.amount());
    }

    @PutMapping("/{accountNumber}/credit")
    public AccountResponse credit(@PathVariable String accountNumber, @RequestBody AmountRequest request) {
        return accountService.credit(accountNumber, request.amount());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }
}

