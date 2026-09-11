package com.finbank.transaction.repository;

import com.finbank.transaction.model.TransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionRecord, Long> {
}

