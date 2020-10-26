package com.adrian.mocanu.atm;

import com.adrian.mocanu.atm.model.ValidatedHashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ValidatedHashMapTest {

	@Autowired
	private Map<String, Integer> pairs = new ValidatedHashMap();

	@BeforeEach
	void clearPairs() {
		pairs.clear();
	}

	@Test
	void validatedHashMapWorksFine() {
		generatePairs(500);
		assertTrue(pairs.containsKey("100"));
		assertTrue(pairs.containsKey("500"));
		assertEquals(pairs.get("100"), 20);
		assertEquals(pairs.get("500"), 500);
	}

	@Test
	void mapThrowsErrorOnNonUniqueKeys() {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			generatePairs(100);
		});
		String expectedMessage = "Denomination 100 is duplicated";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void mapThrowsErrorOnNonNumericKeyValues() {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			pairs.put("nonNumericValue", 20);
		});
		String expectedMessage = "Denomination nonNumericValue is not of type integer";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void mapThrowsErrorOnOutOfBoundariesNegativeKeyValues() {
		var exceededValue = ValidatedHashMap.MIN_DENOMINATION - 13;

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			generatePairs(exceededValue);
		});
		String expectedMessage = "Denomination " + exceededValue
				+ " must be greater than " + ValidatedHashMap.MIN_DENOMINATION;
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void mapThrowsErrorOnOutOfBoundariesPositiveKeyValues() {
		var exceededValue = ValidatedHashMap.MAX_DENOMINATION + 1;
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			generatePairs(exceededValue);
		});
		String expectedMessage = "Denomination " + exceededValue
				+ " must be smaller than " + ValidatedHashMap.MAX_DENOMINATION;
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void mapThrowsErrorOnOutOfBoundariesPositiveNumberOfBillsValues() {
		var exceededValue = ValidatedHashMap.MAX_NUMBER_OF_BILLS + 1;
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			pairs.put("4", exceededValue);
		});
		String expectedMessage = "Number of bills for denomination 4 must be smaller than "
				+ ValidatedHashMap.MAX_NUMBER_OF_BILLS;
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void mapThrowsErrorOnOutOfBoundariesNegativeNumberOfBillsValues() {
		var exceededValue = ValidatedHashMap.MIN_NUMBER_OF_BILLS - 1;
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			pairs.put("4", exceededValue);
		});
		String expectedMessage = "Number of bills for denomination 4 must be greater than "
				+ ValidatedHashMap.MIN_NUMBER_OF_BILLS;
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	void generatePairs(int pair) {
		pairs.put("100", 20);
		pairs.put(String.valueOf(pair), pair);
	}

}
