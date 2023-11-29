package com.bankingapplication.service;

import java.util.List;

import com.bankingapplication.dto.TransactionDTO;
import com.bankingapplication.exception.NotFoundException;

public interface TransactionService {
	TransactionDTO addTransaction(TransactionDTO transactionDTO);

	List<TransactionDTO> getAllTransactionsByUserId(Long userId) throws NotFoundException;
}
