package com.adrian.mocanu.atm.model;

import java.util.HashMap;

public class ValidatedHashMap extends HashMap<String, Integer> {

    public static final int MIN_DENOMINATION_VALUE = 1;

    public static final int MAX_DENOMINATION_VALUE = 1000;

    @Override
    public Integer put(String key, Integer value) {
        isUnique(key);
        isNumeric(key);
        isInBoundaries(key);

        return super.put(key, value);
    }

    private void isUnique(String key) {
        if (containsKey(key)) {
            throw new IllegalArgumentException("Denomination " + key + " is duplicated");
        }
    }

    private void isNumeric(String key) {
        try {
            Integer.parseInt(key);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Denomination " + key + " is not of type integer") ;
        }
    }

    private void isInBoundaries(String key) {
        var intKey = Integer.parseInt(key);
        if (intKey < MIN_DENOMINATION_VALUE) {
            throw new IllegalArgumentException("Denomination " + key + " must be greater than " + MIN_DENOMINATION_VALUE) ;
        }
        if (intKey > MAX_DENOMINATION_VALUE) {
            throw new IllegalArgumentException("Denomination " + key + " must be smaller than " + MAX_DENOMINATION_VALUE) ;
        }

    }
}
