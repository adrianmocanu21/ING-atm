package com.adrian.mocanu.atm.repository;

import com.adrian.mocanu.atm.model.NumberOfBills;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CurrencyQueryBuilder {

    private static int TOTAL_NUMBER_OF_ACCEPTED_BILLS = 100000;

    private final MongoTemplate mongoTemplate;

    public CurrencyQueryBuilder(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public int getNumberOfAvailableBills(Map<String, Integer> pairs) {
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

    public static void setTotalNumberOfAcceptedBills(int totalNumberOfAcceptedBills) {
        TOTAL_NUMBER_OF_ACCEPTED_BILLS = totalNumberOfAcceptedBills;
    }
}
