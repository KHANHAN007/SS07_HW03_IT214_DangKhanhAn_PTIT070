package com.finbank.transaction.client;

import com.finbank.transaction.dto.AccountResponse;
import com.finbank.transaction.dto.AmountRequest;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class AccountClient {

    private static final String ACCOUNT_SERVICE = "http://account-service/api/accounts";

    private final RestTemplate restTemplate;

    public AccountClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AccountResponse getAccount(String accountNumber) {
        try {
            return restTemplate.getForObject(ACCOUNT_SERVICE + "/{accountNumber}", AccountResponse.class, accountNumber);
        } catch (RestClientException ex) {
            throw new IllegalArgumentException("Khong tim thay tai khoan " + accountNumber);
        }
    }

    public BigDecimal getBalance(String accountNumber) {
        try {
            Map<?, ?> response = restTemplate.getForObject(
                    ACCOUNT_SERVICE + "/{accountNumber}/balance",
                    Map.class,
                    accountNumber);
            return new BigDecimal(response.get("balance").toString());
        } catch (RestClientException ex) {
            throw new IllegalArgumentException("Khong lay duoc so du tai khoan " + accountNumber);
        }
    }

    public AccountResponse debit(String accountNumber, BigDecimal amount) {
        try {
            restTemplate.put(ACCOUNT_SERVICE + "/{accountNumber}/debit", new AmountRequest(amount), accountNumber);
            return getAccount(accountNumber);
        } catch (RestClientException ex) {
            throw new IllegalArgumentException("Tru tien that bai: " + accountNumber);
        }
    }

    public AccountResponse credit(String accountNumber, BigDecimal amount) {
        try {
            restTemplate.put(ACCOUNT_SERVICE + "/{accountNumber}/credit", new AmountRequest(amount), accountNumber);
            return getAccount(accountNumber);
        } catch (RestClientException ex) {
            throw new IllegalArgumentException("Cong tien that bai: " + accountNumber);
        }
    }
}

