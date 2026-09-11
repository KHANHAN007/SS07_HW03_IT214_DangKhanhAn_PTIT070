package com.finbank.transaction.service;

import com.finbank.transaction.client.AccountClient;
import com.finbank.transaction.dto.TransferRequest;
import com.finbank.transaction.dto.TransferResponse;
import com.finbank.transaction.model.TransactionRecord;
import com.finbank.transaction.repository.TransactionRepository;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    private final AccountClient accountClient;
    private final TransactionRepository transactionRepository;

    public TransferService(AccountClient accountClient, TransactionRepository transactionRepository) {
        this.accountClient = accountClient;
        this.transactionRepository = transactionRepository;
    }

    public TransferResponse transfer(TransferRequest request) {
        try {
            validateRequest(request);
            accountClient.getAccount(request.fromAccountNumber());
            accountClient.getAccount(request.toAccountNumber());

            BigDecimal balance = accountClient.getBalance(request.fromAccountNumber());
            if (balance.compareTo(request.amount()) < 0) {
                return saveFailed(request, "So du khong du");
            }

            accountClient.debit(request.fromAccountNumber(), request.amount());
            accountClient.credit(request.toAccountNumber(), request.amount());

            TransactionRecord saved = transactionRepository.save(new TransactionRecord(
                    request.fromAccountNumber(),
                    request.toAccountNumber(),
                    request.amount(),
                    request.description(),
                    "SUCCESS",
                    "Chuyen tien thanh cong"));

            return toResponse(saved);
        } catch (IllegalArgumentException ex) {
            return saveFailed(request, ex.getMessage());
        }
    }

    private void validateRequest(TransferRequest request) {
        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("So tien phai lon hon 0");
        }
        if (request.fromAccountNumber() == null || request.toAccountNumber() == null) {
            throw new IllegalArgumentException("Tai khoan nguon va dich khong duoc de trong");
        }
    }

    private TransferResponse saveFailed(TransferRequest request, String message) {
        TransactionRecord saved = transactionRepository.save(new TransactionRecord(
                request.fromAccountNumber(),
                request.toAccountNumber(),
                request.amount(),
                request.description(),
                "FAILED",
                message));
        return toResponse(saved);
    }

    private TransferResponse toResponse(TransactionRecord record) {
        return new TransferResponse(
                record.getId(),
                record.getStatus(),
                record.getMessage(),
                record.getFromAccountNumber(),
                record.getToAccountNumber(),
                record.getAmount());
    }
}

