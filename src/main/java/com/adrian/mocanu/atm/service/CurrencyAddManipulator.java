package com.adrian.mocanu.atm.service;

import com.adrian.mocanu.atm.model.CurrencyDb;

import java.util.List;
import java.util.Map;

public interface CurrencyAddManipulator {

	List<CurrencyDb> addCurrency(Map<String, Integer> pairs);

}
