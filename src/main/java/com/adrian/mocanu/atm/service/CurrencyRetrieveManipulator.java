package com.adrian.mocanu.atm.service;

import java.util.Map;

public interface CurrencyRetrieveManipulator {

	Map<String, Integer> getCurrency(Integer amount);

}
