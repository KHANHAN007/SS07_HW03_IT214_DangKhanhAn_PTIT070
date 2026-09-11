package com.finbank.transaction.controller;

import com.finbank.transaction.dto.TransferRequest;
import com.finbank.transaction.dto.TransferResponse;
import com.finbank.transaction.service.TransferService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransferService transferService;

    public TransactionController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/transfer")
    public TransferResponse transfer(@RequestBody TransferRequest request) {
        return transferService.transfer(request);
    }
}

