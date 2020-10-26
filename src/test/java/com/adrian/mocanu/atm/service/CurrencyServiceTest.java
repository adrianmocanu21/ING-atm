package com.adrian.mocanu.atm.service;

import com.adrian.mocanu.atm.TestUtils;
import com.adrian.mocanu.atm.exception.OutOfFundsException;
import com.adrian.mocanu.atm.model.CurrencyDb;
import com.adrian.mocanu.atm.repository.CurrencyQueryBuilder;
import com.adrian.mocanu.atm.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CurrencyServiceTest {

	@Autowired
	private CurrencyService currencyService;

	@Autowired
	private CurrencyRepository currencyRepository;

	private Map<String, Integer> pairs = new HashMap<>();

	@BeforeEach
	void init() {
		pairs.clear();
		currencyRepository.deleteAll();
	}

	@Test
	void addCurrencyCreatesNewDenomination() {
		TestUtils.generatePairs(pairs, 4);
		var currencyList = currencyService.addCurrency(pairs);

		assertThat(currencyList.size()).isEqualTo(4);
		assertThat(
				currencyRepository.findByBillDenomination("3").get().getNumberOfBills())
						.isEqualTo(3);
	}

	@Test
	void addCurrencyUpdatesExistingDenomination() {
		currencyRepository.save(new CurrencyDb("2", 2));
		TestUtils.generatePairs(pairs, 4);
		var currencyList = currencyService.addCurrency(pairs);

		assertThat(currencyList.size()).isEqualTo(4);
		assertThat(
				currencyRepository.findByBillDenomination("2").get().getNumberOfBills())
						.isEqualTo(4);
	}

	@Test
	void addCurrencyThrowsErrorWhenNumberOfBillsIsExceeded() {
		CurrencyQueryBuilder.setTotalNumberOfAcceptedBills(20);
		TestUtils.generatePairs(pairs, 30);

		Exception exception = assertThrows(RuntimeException.class,
				() -> currencyService.addCurrency(pairs));
		String expectedMessage = "The number of bills is exceeded! You are trying to add 445 more bills than expected";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void getCurrencyReturnsAmount() {
		int amount = 70;
		var currencyDbs = TestUtils.generateCurrencyDbs(10);
		currencyRepository.saveAll(currencyDbs);

		var currencyList = currencyService.getCurrency(amount);
		var retrievedAmount = currencyList.entrySet().stream()
				.mapToLong(e -> Integer.parseInt(e.getKey()) * e.getValue()).sum();

		assertThat(retrievedAmount).isEqualTo(70);
	}

	@Test
	void getCurrencyReturnsAmountAndCleansDb() {
		int amount = 5;
		var currencyDbs = TestUtils.generateCurrencyDbs(2);
		currencyRepository.saveAll(currencyDbs);

		var currencyList = currencyService.getCurrency(amount);
		var retrievedAmount = currencyList.entrySet().stream()
				.mapToLong(e -> Integer.parseInt(e.getKey()) * e.getValue()).sum();

		assertThat(retrievedAmount).isEqualTo(5);
		assertThat(currencyRepository.findByBillDenomination("1"))
				.isEqualTo(Optional.empty());
		assertThat(currencyRepository.findByBillDenomination("2"))
				.isEqualTo(Optional.empty());
	}

	@Test
	void addCurrencyThrowsErrorWhenTheAmountIsExceeded() {
		var desiredAmount = 7;
		var currencyDbs = TestUtils.generateCurrencyDbs(2);
		currencyRepository.saveAll(currencyDbs);

		Exception exception = assertThrows(OutOfFundsException.class,
				() -> currencyService.getCurrency(desiredAmount));
		String expectedMessage = "We do not have this amount! Try a smaller value";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void addCurrencyThrowsErrorWhenTheAtmIsEmpty() {
		var desiredAmount = 7;

		Exception exception = assertThrows(OutOfFundsException.class,
				() -> currencyService.getCurrency(desiredAmount));
		String expectedMessage = "The ATM is out of cash! Please try later";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void addCurrencyThrowsExceptionWhenAtmHasTheDesiredAmountButNoSuitableBills() {
		var desiredAmount = 7;
		var currency = new CurrencyDb("5", 2);
		currencyRepository.save(currency);

		Exception exception = assertThrows(OutOfFundsException.class,
				() -> currencyService.getCurrency(desiredAmount));
		String expectedMessage = "We do not have this amount! Try a smaller value";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

}
