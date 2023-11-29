package com.bankingapplication.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class TransactionDTO {

	private Long id;

	@NotNull(message = "Amount is required")
	private Double amount;

	@NotNull(message = "Transaction date is required")
	private Date transactionDate;

	@NotNull(message = "User is required")
	private UserDTO userDTO;

	public TransactionDTO() {
		super();
	}

	public TransactionDTO(Long id, @NotNull(message = "Amount is required") Double amount,
			@NotNull(message = "Transaction date is required") Date transactionDate,
			@NotNull(message = "User is required") UserDTO userDTO) {
		super();
		this.id = id;
		this.amount = amount;
		this.transactionDate = transactionDate;
		this.userDTO = userDTO;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

	@Override
	public String toString() {
		return "TransactionDTO [id=" + id + ", amount=" + amount + ", transactionDate=" + transactionDate + ", userDTO="
				+ userDTO + "]";
	}
}
