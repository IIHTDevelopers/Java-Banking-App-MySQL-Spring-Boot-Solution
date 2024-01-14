package com.bankingapplication.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.bankingapplication.dto.TransactionDTO;
import com.bankingapplication.exception.NotFoundException;

public interface TransactionService {
	TransactionDTO addTransaction(TransactionDTO transactionDTO);

	List<TransactionDTO> getAllTransactionsByUserIdInDateOrder(Long userId, Pageable pageable) throws NotFoundException;

	List<TransactionDTO> getTransactionsByAmountRange(BigDecimal minAmount, BigDecimal maxAmount);

	List<TransactionDTO> getTransactionsByDateRange(Long userId, LocalDate startDate, LocalDate endDate);
}
