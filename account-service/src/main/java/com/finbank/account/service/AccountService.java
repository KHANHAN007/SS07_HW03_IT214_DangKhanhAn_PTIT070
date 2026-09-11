package com.finbank.account.service;

import com.finbank.account.dto.AccountResponse;
import com.finbank.account.model.Account;
import com.finbank.account.repository.AccountRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountResponse getByAccountNumber(String accountNumber) {
        Account account = findAccount(accountNumber);
        return toResponse(account);
    }

    public BigDecimal getBalance(String accountNumber) {
        return findAccount(accountNumber).getBalance();
    }

    @Transactional
    public AccountResponse debit(String accountNumber, BigDecimal amount) {
        Account account = findAccount(accountNumber);
        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("So du khong du");
        }
        account.setBalance(account.getBalance().subtract(amount));
        return toResponse(accountRepository.save(account));
    }

    @Transactional
    public AccountResponse credit(String accountNumber, BigDecimal amount) {
        Account account = findAccount(accountNumber);
        account.setBalance(account.getBalance().add(amount));
        return toResponse(accountRepository.save(account));
    }

    private Account findAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay tai khoan " + accountNumber));
    }

    private AccountResponse toResponse(Account account) {
        return new AccountResponse(account.getAccountNumber(), account.getOwnerName(), account.getBalance());
    }
}

