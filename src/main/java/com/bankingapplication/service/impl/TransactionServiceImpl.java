package com.bankingapplication.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.bankingapplication.dto.TransactionDTO;
import com.bankingapplication.entity.Transaction;
import com.bankingapplication.entity.User;
import com.bankingapplication.repo.TransactionRepository;
import com.bankingapplication.repo.UserRepository;
import com.bankingapplication.service.TransactionService;

import javassist.NotFoundException;

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
	public List<TransactionDTO> getAllTransactionsByUserId(Long userId) {
		User user = null;
		try {
			user = userRepository.findById(userId)
					.orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		List<Transaction> transactions = transactionRepository.findByUser(user);
		return transactions.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public TransactionDTO addTransaction(TransactionDTO transactionDTO) {
		User user = null;
		try {
			user = userRepository.findById(transactionDTO.getUserDTO().getId()).orElseThrow(
					() -> new NotFoundException("User not found with id: " + transactionDTO.getUserDTO().getId()));
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
}
