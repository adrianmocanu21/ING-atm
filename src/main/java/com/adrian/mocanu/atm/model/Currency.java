package com.adrian.mocanu.atm.model;

public class Currency {

	private Integer numberOfBills;

	private final String billDenomination;

	public Currency(String billDenomination, Integer numberOfBills) {
		this.billDenomination = billDenomination;
		this.numberOfBills = numberOfBills;
	}

	public Integer getNumberOfBills() {
		return numberOfBills;
	}

	public String getBillDenomination() {
		return billDenomination;
	}

}
