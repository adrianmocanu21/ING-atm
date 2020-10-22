package com.adrian.mocanu.atm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("currency")
public class CurrencyDb {

    @Id
    private String id;

    private final String billDenomination;

    private Integer numberOfBills;

    public CurrencyDb(String billDenomination, Integer numberOfBills) {
        this.billDenomination = billDenomination;
        this.numberOfBills = numberOfBills;
    }

    public void increaseNumberOfBills(Integer numberOfBills) {
        this.numberOfBills = this.numberOfBills + numberOfBills;
    }
}
