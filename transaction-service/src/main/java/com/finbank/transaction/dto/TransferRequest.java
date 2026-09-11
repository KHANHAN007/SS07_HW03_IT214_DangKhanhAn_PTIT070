package com.finbank.transaction.dto;

import java.math.BigDecimal;

public record TransferRequest(
        String fromAccountNumber,
        String toAccountNumber,
        BigDecimal amount,
        String description
) {
}

