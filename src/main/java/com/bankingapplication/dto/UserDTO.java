package com.bankingapplication.dto;

import jakarta.validation.constraints.NotBlank;

public class UserDTO {

	private Long id;

	@NotBlank(message = "Name is required")
	private String name;

	@NotBlank(message = "Account number is required")
	private String accountNumber;

	@NotBlank(message = "Account type is required")
	private String accountType;

	public UserDTO() {
		super();
	}

	public UserDTO(Long id, @NotBlank(message = "Name is required") String name,
			@NotBlank(message = "Account number is required") String accountNumber,
			@NotBlank(message = "Account type is required") String accountType) {
		super();
		this.id = id;
		this.name = name;
		this.accountNumber = accountNumber;
		this.accountType = accountType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", name=" + name + ", accountNumber=" + accountNumber + ", accountType="
				+ accountType + "]";
	}
}
