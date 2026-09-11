package com.finbank.transaction.dto;

import java.math.BigDecimal;

public record TransferResponse(
        Long transactionId,
        String status,
        String message,
        String fromAccountNumber,
        String toAccountNumber,
        BigDecimal amount
) {
}

