package com.finbank.account;

import com.finbank.account.model.Account;
import com.finbank.account.repository.AccountRepository;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner seedAccounts(AccountRepository accountRepository) {
        return args -> {
            if (accountRepository.count() == 0) {
                accountRepository.save(new Account(null, "1001", "Nguyen Van A", new BigDecimal("10000000")));
                accountRepository.save(new Account(null, "1002", "Tran Thi B", new BigDecimal("5000000")));
            }
        };
    }
}

