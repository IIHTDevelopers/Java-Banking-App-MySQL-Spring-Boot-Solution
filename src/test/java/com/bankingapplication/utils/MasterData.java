package com.bankingapplication.utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.bankingapplication.dto.TransactionDTO;
import com.bankingapplication.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class MasterData {

	public static UserDTO getUserDTO() {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(1L);
		userDTO.setName("John Doe");
		userDTO.setAccountNumber("1234567890");
		userDTO.setAccountType("Savings");
		return userDTO;
	}

	public static List<UserDTO> getUserDTOList() {
		List<UserDTO> userDTOList = new ArrayList<>();
		userDTOList.add(getUserDTO());
		return userDTOList;
	}

	public static TransactionDTO getTransactionDTO() {
		TransactionDTO transactionDTO = new TransactionDTO();
		transactionDTO.setId(1L);
		transactionDTO.setAmount(500.0);
		transactionDTO.setTransactionDate(LocalDateTime.of(2023, 1, 14, 15, 30, 0));
		transactionDTO.setUserDTO(getUserDTO());
		return transactionDTO;
	}

	public static List<TransactionDTO> getTransactionDTOList() {
		List<TransactionDTO> transactionDTOList = new ArrayList<>();
		transactionDTOList.add(getTransactionDTO());
		return transactionDTOList;
	}

	public static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			final String jsonContent = mapper.writeValueAsString(obj);

			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String randomStringWithSize(int size) {
		String s = "";
		for (int i = 0; i < size; i++) {
			s.concat("A");
		}
		return s;
	}
}