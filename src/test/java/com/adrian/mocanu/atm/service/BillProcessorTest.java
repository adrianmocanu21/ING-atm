package com.adrian.mocanu.atm.service;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class BillProcessorTest {

	private BillProcessor billProcessor = new BillProcessor();

	@Test
	void billProcessorFindsSuitableCombination() {
		var values = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 7, 20, 30, 20, 50));
		var matches = billProcessor.findBillsToMatchAmount(values, 38);
		assertThat(matches.get(0)).isNotNull();
		assertThat(matches.get(0).stream().reduce(0, Integer::sum)).isEqualTo(38);
	}

	@Test
	void billProcessorDoesNotFindCombinationWhenEnoughAmountExists() {
		var values = new ArrayList<>(Arrays.asList(27, 5));
		var matches = billProcessor.findBillsToMatchAmount(values, 31);
		assertThat(matches).isEmpty();
	}

}
