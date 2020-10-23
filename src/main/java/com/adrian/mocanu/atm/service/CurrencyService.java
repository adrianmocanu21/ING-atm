package com.adrian.mocanu.atm.service;

import com.adrian.mocanu.atm.model.Currency;
import com.adrian.mocanu.atm.model.CurrencyDb;
import com.adrian.mocanu.atm.model.NumberOfBills;
import com.adrian.mocanu.atm.repository.CurrencyRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Consumer;

@Service
public class CurrencyService {

    private static final int TOTAL_NUMBER_OF_ACCEPTED_BILLS = 100000;

    private final CurrencyRepository currencyRepository;

    private final MongoTemplate mongoTemplate;

    public CurrencyService(CurrencyRepository currencyRepository, MongoTemplate mongoTemplate) {
        this.currencyRepository = currencyRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public void addCurrency(Map<String, Integer> pairs) {
        var availableBills = getNumberOfAvailableBills(pairs);
        if (availableBills < 0) {
            throw new RuntimeException("The number of bills is exceeded! You have added " + Math.abs(availableBills) + " more bills than expected" );
        }

        pairs.entrySet().stream().map(entry -> new Currency(entry.getKey(), entry.getValue())).forEach(currency -> {
            var currencyDbOpt = currencyRepository.findByBillDenomination(currency.getBillDenomination());
            currencyDbOpt.ifPresentOrElse(updateExistingDenomination(currency), () ->
                createDenomination(currency));
        });
    }

    private int getNumberOfAvailableBills(Map<String, Integer> pairs) {
        var billsToBeAdded = pairs.values().stream().reduce(0, Integer::sum);
        var numberOfExistingBills =  mongoTemplate.aggregate(getNumberOfBillsAggregation(),"currency", NumberOfBills.class).getUniqueMappedResult();

        if (numberOfExistingBills == null) {
            numberOfExistingBills = new NumberOfBills(0);
        }

        var desiredNumberOfBills = billsToBeAdded + numberOfExistingBills.getTotalNumberOfBills();

        return TOTAL_NUMBER_OF_ACCEPTED_BILLS - desiredNumberOfBills;
    }

    private Aggregation getNumberOfBillsAggregation() {
        return Aggregation.newAggregation(Aggregation.group().sum("numberOfBills").as("totalNumberOfBills"), Aggregation.project().andExclude("_id"));
    }


    private void createDenomination(Currency currency) {
        var currencyDb = new CurrencyDb(currency.getBillDenomination(), currency.getNumberOfBills());
        currencyRepository.save(currencyDb);
    }

    private Consumer<CurrencyDb> updateExistingDenomination(Currency currency) {
        return currencyDb -> {
            currencyDb.increaseNumberOfBills(currency.getNumberOfBills());
            currencyRepository.save(currencyDb);
        };
    }
}
