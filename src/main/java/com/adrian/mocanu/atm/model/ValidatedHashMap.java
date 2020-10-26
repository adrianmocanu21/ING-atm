package com.adrian.mocanu.atm.model;

import java.util.HashMap;

public class ValidatedHashMap extends HashMap<String, Integer> {

	public static final int MIN_DENOMINATION = 1;

	public static final int MAX_DENOMINATION = 1000;

	public static final int MIN_NUMBER_OF_BILLS = 1;

	public static final int MAX_NUMBER_OF_BILLS = 100000;

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
		if (intKey < MIN_DENOMINATION) {
			throw new IllegalArgumentException(
					"Denomination " + key + " must be greater than " + MIN_DENOMINATION);
		}
		if (intKey > MAX_DENOMINATION) {
			throw new IllegalArgumentException(
					"Denomination " + key + " must be smaller than " + MAX_DENOMINATION);
		}

	}

	private void numberOfBillsIsInBoundaries(String key, Integer value) {
		if (value < MIN_NUMBER_OF_BILLS) {
			throw new IllegalArgumentException("Number of bills for denomination " + key
					+ " must be greater than " + MIN_NUMBER_OF_BILLS);
		}
		if (value > MAX_NUMBER_OF_BILLS) {
			throw new IllegalArgumentException("Number of bills for denomination " + key
					+ " must be smaller than " + MAX_NUMBER_OF_BILLS);
		}
	}

}
