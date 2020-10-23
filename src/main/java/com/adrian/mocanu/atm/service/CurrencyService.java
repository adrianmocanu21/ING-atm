package com.adrian.mocanu.atm.service;

import com.adrian.mocanu.atm.model.Currency;
import com.adrian.mocanu.atm.model.CurrencyDb;
import com.adrian.mocanu.atm.repository.CurrencyQueryBuilder;
import com.adrian.mocanu.atm.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CurrencyService {

    private final CurrencyQueryBuilder currencyQueryBuilder;

    private final CurrencyRepository currencyRepository;

    public CurrencyService( CurrencyQueryBuilder currencyQueryBuilder, CurrencyRepository currencyRepository) {
        this.currencyQueryBuilder = currencyQueryBuilder;
        this.currencyRepository = currencyRepository;
    }

    public List<CurrencyDb> addCurrency(Map<String, Integer> pairs) {
        var availableBills = currencyQueryBuilder.getNumberOfAvailableBills(pairs);
        if (availableBills < 0) {
            //TODO can create custom exception
            throw new RuntimeException("The number of bills is exceeded! You have added " + Math.abs(availableBills) + " more bills than expected" );
        }

        return pairs.entrySet().stream().map(entry -> new Currency(entry.getKey(), entry.getValue())).map(this::createOrUpdateCurrency).collect(Collectors.toList());
    }

    private CurrencyDb createOrUpdateCurrency(Currency currency) {
        var currencyDbOpt = currencyRepository.findByBillDenomination(currency.getBillDenomination());
        return currencyDbOpt.map(updateExistingCurrency(currency)).orElseGet(() -> createCurrency(currency));

    }

    private CurrencyDb createCurrency(Currency currency) {
        var currencyDb = new CurrencyDb(currency.getBillDenomination(), currency.getNumberOfBills());
        return currencyRepository.save(currencyDb);
    }

    private Function<CurrencyDb, CurrencyDb> updateExistingCurrency(Currency currency) {
            return currencyDb -> {
                currencyDb.increaseNumberOfBills(currency.getNumberOfBills());
                return currencyRepository.save(currencyDb);
        };
    }
}
