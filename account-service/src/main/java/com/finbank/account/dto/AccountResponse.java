package com.finbank.account.dto;

import java.math.BigDecimal;

public record AccountResponse(
        String accountNumber,
        String ownerName,
        BigDecimal balance
) {
}

