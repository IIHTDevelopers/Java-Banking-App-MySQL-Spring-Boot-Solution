package com.bankingapplication.repo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bankingapplication.entity.Transaction;
import com.bankingapplication.entity.User;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	Page<Transaction> findAllByUserOrderByTransactionDateAsc(User user, Pageable pageable);

	List<Transaction> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);

	List<Transaction> findByUserIdAndTransactionDateBetween(Long userId, LocalDateTime startDate,
			LocalDateTime endDate);
}
