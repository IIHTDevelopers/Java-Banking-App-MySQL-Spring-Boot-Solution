package com.bankingapplication.exception;

import static com.bankingapplication.utils.TestUtils.currentTest;
import static com.bankingapplication.utils.TestUtils.exceptionTestFile;
import static com.bankingapplication.utils.TestUtils.testReport;
import static com.bankingapplication.utils.TestUtils.yakshaAssert;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.bankingapplication.controller.TransactionController;
import com.bankingapplication.dto.TransactionDTO;
import com.bankingapplication.service.TransactionService;
import com.bankingapplication.utils.MasterData;

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc
public class TransactionExceptionTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TransactionService transactionService;

	@AfterAll
	public static void afterAll() {
		testReport();
	}

	@Test
	public void testAddTransactionInvalidDataException() throws Exception {
		TransactionDTO transactionDTO = new TransactionDTO(); // Create an invalid TransactionDTO

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/transactions")
				.content(MasterData.asJsonString(transactionDTO)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		yakshaAssert(currentTest(),
				(result.getResponse().getStatus() == HttpStatus.BAD_REQUEST.value() ? "true" : "false"),
				exceptionTestFile);
	}

	@Test
	public void testGetAllTransactionsByUserNotFoundException() throws Exception {
		Long userId = 1L;
		ErrorResponse exResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Transactions not found");

		when(this.transactionService.getAllTransactionsByUserId(userId))
				.thenThrow(new NotFoundException("Transactions not found"));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/transactions/user/" + userId)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		yakshaAssert(currentTest(),
				(result.getResponse().getContentAsString().contains(exResponse.getMessage()) ? "true" : "false"),
				exceptionTestFile);
	}
}
