package com.adrian.mocanu.atm;

import com.adrian.mocanu.atm.model.CurrencyDb;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TestUtils {

	public static String convertResourceToString(Resource resource) {

		try {
			Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8);
			return FileCopyUtils.copyToString(reader);
		}
		catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public static void generatePairs(Map<String, Integer> pairs, int numberOfPairs) {
		for (int i = 1; i <= numberOfPairs; i++) {
			pairs.put(String.valueOf(i), i);
		}
	}

	public static List<CurrencyDb> generateCurrencyDbs(int numberOfCurrencies) {
		var currencyDbs = new ArrayList<CurrencyDb>();
		for (int i = 1; i <= numberOfCurrencies; i++) {
			currencyDbs.add(new CurrencyDb(String.valueOf(i), i));
		}
		return currencyDbs;
	}

}
