package com.bankingapplication.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bankingapplication.dto.TransactionDTO;
import com.bankingapplication.exception.NotFoundException;
import com.bankingapplication.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

	private final TransactionService transactionService;

	@Autowired
	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@PostMapping
	public ResponseEntity<TransactionDTO> addTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
		TransactionDTO savedTransaction = transactionService.addTransaction(transactionDTO);
		return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<TransactionDTO>> getAllTransactionsByUser(@PathVariable Long userId, Pageable pageable) {
		try {
			List<TransactionDTO> transactions = transactionService.getAllTransactionsByUserIdInDateOrder(userId,
					pageable);
			return new ResponseEntity<>(transactions, HttpStatus.OK);
		} catch (NotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

	@GetMapping("/amount-range")
	public ResponseEntity<List<TransactionDTO>> getTransactionsByAmountRange(@RequestParam BigDecimal minAmount,
			@RequestParam BigDecimal maxAmount) {
		List<TransactionDTO> transactions = transactionService.getTransactionsByAmountRange(minAmount, maxAmount);
		return new ResponseEntity<>(transactions, HttpStatus.OK);
	}

	@GetMapping("/user/{userId}/date-range")
	public ResponseEntity<List<TransactionDTO>> getTransactionsByDateRange(@PathVariable Long userId,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
		List<TransactionDTO> transactions = transactionService.getTransactionsByDateRange(userId, startDate, endDate);
		return new ResponseEntity<>(transactions, HttpStatus.OK);
	}
}
