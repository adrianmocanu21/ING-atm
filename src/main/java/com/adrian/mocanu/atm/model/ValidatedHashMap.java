package com.adrian.mocanu.atm.model;

import java.util.HashMap;

public class ValidatedHashMap extends HashMap<String, Integer> {

	public static final int MIN_DENOMINATION_VALUE = 1;

	public static final int MAX_DENOMINATION_VALUE = 1000;

	public static final int MIN_NUMBER_OF_BILLS_VALUE = 1;

	public static final int MAX_NUMBER_OF_BILLS_VALUE = 100000;

	@Override
	public Integer put(String key, Integer value) {
		isUnique(key);
		isNumeric(key);
		denominationIsInBoundaries(key);
		numberOfBillsIsInBoundaries(key, value);

		return super.put(key, value);
	}

	private void isUnique(String key) {
		if (containsKey(key)) {
			throw new IllegalArgumentException("Denomination " + key + " is duplicated");
		}
	}

	private void isNumeric(String key) {
		try {
			Integer.parseInt(key);
		}
		catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Denomination " + key + " is not of type integer");
		}
	}

	private void denominationIsInBoundaries(String key) {
		var intKey = Integer.parseInt(key);
		if (intKey < MIN_DENOMINATION_VALUE) {
			throw new IllegalArgumentException("Denomination " + key
					+ " must be greater than " + MIN_DENOMINATION_VALUE);
		}
		if (intKey > MAX_DENOMINATION_VALUE) {
			throw new IllegalArgumentException("Denomination " + key
					+ " must be smaller than " + MAX_DENOMINATION_VALUE);
		}

	}

	private void numberOfBillsIsInBoundaries(String key, Integer value) {
		if (value < MIN_NUMBER_OF_BILLS_VALUE) {
			throw new IllegalArgumentException("Number of bills for denomination " + key
					+ " must be greater than " + MIN_NUMBER_OF_BILLS_VALUE);
		}
		if (value > MAX_NUMBER_OF_BILLS_VALUE) {
			throw new IllegalArgumentException("Number of bills for denomination " + key
					+ " must be smaller than " + MAX_NUMBER_OF_BILLS_VALUE);
		}
	}

}
