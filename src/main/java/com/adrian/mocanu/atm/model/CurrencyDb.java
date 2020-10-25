package com.adrian.mocanu.atm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("currency")
public class CurrencyDb {

    @Id
    @JsonProperty
    private String id;

    @JsonProperty
    private final String billDenomination;

    @JsonProperty
    private Integer numberOfBills;

    public CurrencyDb(String billDenomination, Integer numberOfBills) {
        this.billDenomination = billDenomination;
        this.numberOfBills = numberOfBills;
    }

    public void increaseNumberOfBills(Integer numberOfBills) {
        this.numberOfBills = this.numberOfBills + numberOfBills;
    }

    public int getBillDenominationAsInt() {
        return Integer.parseInt(billDenomination);
    }

    public Integer getNumberOfBills() {
        return numberOfBills;
    }

    public Integer getAvailableAmountOfCurrency() {
        return numberOfBills * getBillDenominationAsInt();
    }

    public void updateNumberOfBills(Integer extractedBills) {
        this.numberOfBills = this.numberOfBills - extractedBills;
    }
}
