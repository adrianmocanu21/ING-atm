package com.adrian.mocanu.atm.validator;

import java.util.HashMap;

public class SingleKeyHashMap extends HashMap<String, Integer> {

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
        var minVal = 1;
        var maxVal = 1000;
        var intKey = Integer.parseInt(key);
        if (intKey < minVal) {
            throw new IllegalArgumentException("Denomination " + key + " must be greater than " + minVal) ;
        }
        if (intKey > maxVal) {
            throw new IllegalArgumentException("Denomination " + key + " must be smaller than " + maxVal) ;
        }

    }
}
