package com.bankingapplication.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bankingapplication.dto.TransactionDTO;
import com.bankingapplication.entity.Transaction;
import com.bankingapplication.entity.User;
import com.bankingapplication.exception.NotFoundException;
import com.bankingapplication.repo.TransactionRepository;
import com.bankingapplication.repo.UserRepository;
import com.bankingapplication.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository transactionRepository;
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

	public TransactionServiceImpl(TransactionRepository transactionRepository, UserRepository userRepository,
			ModelMapper modelMapper) {
		this.transactionRepository = transactionRepository;
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<TransactionDTO> getAllTransactionsByUserIdInDateOrder(Long userId, Pageable pageable) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundException("User not found."));

		Page<Transaction> transactions = transactionRepository.findAllByUserOrderByTransactionDateAsc(user, pageable);
		return transactions.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public TransactionDTO addTransaction(TransactionDTO transactionDTO) {
		User user = null;
		try {
			user = userRepository.findById(transactionDTO.getUserDTO().getId())
					.orElseThrow(() -> new NotFoundException("User not found."));
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		Transaction transaction = convertToEntity(transactionDTO);
		transaction.setUser(user);
		Transaction savedTransaction = transactionRepository.save(transaction);
		return convertToDTO(savedTransaction);
	}

	private TransactionDTO convertToDTO(Transaction transaction) {
		return modelMapper.map(transaction, TransactionDTO.class);
	}

	private Transaction convertToEntity(TransactionDTO transactionDTO) {
		return modelMapper.map(transactionDTO, Transaction.class);
	}

	@Override
	public List<TransactionDTO> getTransactionsByAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
		List<Transaction> transactions = transactionRepository.findByAmountBetween(minAmount, maxAmount);
		return transactions.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public List<TransactionDTO> getTransactionsByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
		List<Transaction> transactions = transactionRepository.findByUserIdAndTransactionDateBetween(userId,
				startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
		return transactions.stream().map(this::convertToDTO).collect(Collectors.toList());
	}
}
