package com.adrian.mocanu.atm.service;

import com.adrian.mocanu.atm.exception.ExceededAmountException;
import com.adrian.mocanu.atm.exception.OutOfFundsException;
import com.adrian.mocanu.atm.model.Currency;
import com.adrian.mocanu.atm.model.CurrencyDb;
import com.adrian.mocanu.atm.repository.CurrencyQueryBuilder;
import com.adrian.mocanu.atm.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CurrencyService
		implements CurrencyAddManipulator, CurrencyRetrieveManipulator {

	private final CurrencyQueryBuilder currencyQueryBuilder;

	private final CurrencyRepository currencyRepository;

	public CurrencyService(CurrencyQueryBuilder currencyQueryBuilder,
			CurrencyRepository currencyRepository) {
		this.currencyQueryBuilder = currencyQueryBuilder;
		this.currencyRepository = currencyRepository;
	}

	public List<CurrencyDb> addCurrency(Map<String, Integer> pairs) {
		checkTotalNumberOfBills(pairs);

		return pairs.entrySet().stream()
				.map(entry -> new Currency(entry.getKey(), entry.getValue()))
				.map(this::createOrUpdateCurrency).collect(Collectors.toList());
	}

	public Map<String, Integer> getCurrency(Integer amount) {
		var currencyList = currencyRepository.findAll().stream()
				.filter(currencyDb -> currencyDb.denominationIsWithinAmount(amount))
				.collect(Collectors.toList());
		var availableCurrencyValue = 0;
		var availableBills = new ArrayList<Integer>();

		for (CurrencyDb currency : currencyList) {
			availableCurrencyValue = availableCurrencyValue
					+ currency.getAvailableAmountOfCurrency();

			for (int i = 0; i <= currency.getNumberOfBills(); i++) {
				availableBills.add(currency.getBillDenominationAsInt());
			}
		}
		if (availableCurrencyValue == 0) {
			throw new OutOfFundsException("The ATM is out of cash! Please try later");
		}
		if (availableCurrencyValue < amount) {
			throw new OutOfFundsException(
					"We do not have this amount! Try a smaller value");
		}
		var matches = new BillProcessor().findBillsToMatchAmount(availableBills, amount);
		var extractedBills = matches.stream().findFirst().map(sortBills())
				.orElseThrow(() -> new OutOfFundsException(
						"We do not have this amount! Try a smaller value"));
		updateNumberOfExtractedBills(extractedBills);

		return extractedBills;
	}

	private Function<ArrayList<Integer>, HashMap<String, Integer>> sortBills() {
		return match -> {
			var response = new HashMap<String, Integer>();
			for (Integer bill : match) {
				response.merge(bill.toString(), 1, Integer::sum);
			}
			return response;
		};
	}

	private void updateNumberOfExtractedBills(Map<String, Integer> extractedBills) {
		extractedBills.entrySet().forEach(entry -> {
			var currencyDbOpt = currencyRepository.findByBillDenomination(entry.getKey());
			currencyDbOpt.ifPresent(updateNumberOfBillsForGivenDenomination(entry));
		});
	}

	private Consumer<CurrencyDb> updateNumberOfBillsForGivenDenomination(
			Map.Entry<String, Integer> entry) {
		return currencyDb -> {
			currencyDb.updateNumberOfBills(entry.getValue());
			if (currencyDb.getNumberOfBills() == 0) {
				currencyRepository.delete(currencyDb);
			}
			else {
				currencyRepository.save(currencyDb);
			}
		};
	}

	private void checkTotalNumberOfBills(Map<String, Integer> pairs) {
		var availableBills = currencyQueryBuilder.getNumberOfAvailableBills(pairs);
		if (availableBills < 0) {
			throw new ExceededAmountException(
					"The number of bills is exceeded! You are trying to add "
							+ Math.abs(availableBills) + " more bills than expected");
		}
	}

	private CurrencyDb createOrUpdateCurrency(Currency currency) {
		var currencyDbOpt = currencyRepository
				.findByBillDenomination(currency.getBillDenomination());
		return currencyDbOpt.map(updateExistingCurrency(currency))
				.orElseGet(() -> createCurrency(currency));

	}

	private CurrencyDb createCurrency(Currency currency) {
		var currencyDb = new CurrencyDb(currency.getBillDenomination(),
				currency.getNumberOfBills());
		return currencyRepository.save(currencyDb);
	}

	private Function<CurrencyDb, CurrencyDb> updateExistingCurrency(Currency currency) {
		return currencyDb -> {
			currencyDb.increaseNumberOfBills(currency.getNumberOfBills());
			return currencyRepository.save(currencyDb);
		};
	}

}
