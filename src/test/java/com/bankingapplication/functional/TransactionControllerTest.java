package com.bankingapplication.functional;

import static com.bankingapplication.utils.MasterData.getTransactionDTO;
import static com.bankingapplication.utils.MasterData.getTransactionDTOList;
import static com.bankingapplication.utils.TestUtils.businessTestFile;
import static com.bankingapplication.utils.TestUtils.currentTest;
import static com.bankingapplication.utils.TestUtils.testReport;
import static com.bankingapplication.utils.TestUtils.yakshaAssert;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.bankingapplication.controller.TransactionController;
import com.bankingapplication.dto.TransactionDTO;
import com.bankingapplication.entity.Transaction;
import com.bankingapplication.entity.User;
import com.bankingapplication.exception.NotFoundException;
import com.bankingapplication.repo.TransactionRepository;
import com.bankingapplication.repo.UserRepository;
import com.bankingapplication.service.TransactionService;
import com.bankingapplication.utils.MasterData;

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc
public class TransactionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TransactionService transactionService;

	@MockBean
	private TransactionRepository transactionRepository;

	@MockBean
	private UserRepository userRepository;

	@AfterAll
	public static void afterAll() {
		testReport();
	}

	@Test
	public void testAddTransaction() throws Exception {
		TransactionDTO transactionDTO = getTransactionDTO();

		when(this.transactionService.addTransaction(transactionDTO)).thenReturn(transactionDTO);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/transactions")
				.content(MasterData.asJsonString(transactionDTO)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		yakshaAssert(currentTest(), (MasterData.asJsonString(transactionDTO).length() > 0) ? "true" : "false",
				businessTestFile);
	}

	@Test
	public void testGetAllTransactionsByUser() throws Exception {
		List<TransactionDTO> transactionDTOList = getTransactionDTOList();
		Long userId = 1L;

		when(this.transactionService.getAllTransactionsByUserIdInDateOrder(eq(userId), any(Pageable.class)))
				.thenReturn(transactionDTOList);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/transactions/user/" + userId)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		yakshaAssert(currentTest(),
				(result.getResponse().getStatus() == HttpStatus.OK.value() && result.getResponse().getContentAsString()
						.contentEquals(MasterData.asJsonString(transactionDTOList)) ? "true" : "false"),
				businessTestFile);
	}

	@Test
	public void testGetTransactionsByAmountRange() throws Exception {
		List<TransactionDTO> transactionDTOList = getTransactionDTOList();
		BigDecimal minAmount = BigDecimal.valueOf(100);
		BigDecimal maxAmount = BigDecimal.valueOf(500);

		when(this.transactionService.getTransactionsByAmountRange(minAmount, maxAmount)).thenReturn(transactionDTOList);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/transactions/amount-range")
				.param("minAmount", minAmount.toString()).param("maxAmount", maxAmount.toString())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		yakshaAssert(currentTest(),
				(result.getResponse().getContentAsString().contentEquals(MasterData.asJsonString(transactionDTOList))
						? "true"
						: "false"),
				businessTestFile);
	}

	@Test
	public void testGetTransactionsByDateRange() throws Exception {
		List<TransactionDTO> transactionDTOList = getTransactionDTOList();
		Long userId = 1L;
		LocalDate startDate = LocalDate.of(2022, 1, 1);
		LocalDate endDate = LocalDate.of(2022, 12, 31);

		when(this.transactionService.getTransactionsByDateRange(userId, startDate, endDate))
				.thenReturn(transactionDTOList);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/transactions/user/" + userId + "/date-range")
				.param("startDate", startDate.toString()).param("endDate", endDate.toString())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		yakshaAssert(currentTest(),
				(result.getResponse().getContentAsString().contentEquals(MasterData.asJsonString(transactionDTOList))
						? "true"
						: "false"),
				businessTestFile);
	}

	@Test
	public void testAddTransaction_RollbackOnException() throws Exception {
		try {
			TransactionDTO transactionDTO = getTransactionDTO();
			transactionDTO.setTransactionDate(null);
			when(transactionService.addTransaction(any())).thenThrow(NotFoundException.class);
			RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/transactions")
					.content(MasterData.asJsonString(transactionDTO)).contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON);
			mockMvc.perform(requestBuilder);
			Transaction savedTransaction = transactionRepository.findById(transactionDTO.getId()).orElse(null);
			User savedUser = userRepository.findById(transactionDTO.getUserDTO().getId()).orElse(null);
			yakshaAssert(currentTest(), savedTransaction == null && savedUser == null, businessTestFile);
		} catch (Exception ex) {
			ex.printStackTrace();
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

}
