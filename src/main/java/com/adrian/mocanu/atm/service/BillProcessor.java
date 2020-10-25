package com.adrian.mocanu.atm.service;

import java.util.ArrayList;
import java.util.List;

public class BillProcessor {

    static List<ArrayList<Integer>> findBillsToMatchAmount(ArrayList<Integer> bills, int amount) {
        var matches = new ArrayList<ArrayList<Integer>>();
        findBillsToMatchAmountRecursively(bills, amount, new ArrayList<>(), matches);

        return matches;
    }

    private static void findBillsToMatchAmountRecursively(ArrayList<Integer> bills,
                                                          int amount,
                                                          ArrayList<Integer> partial,
                                                          ArrayList<ArrayList<Integer>> matches) {
        int s = partial.stream().reduce(0, Integer::sum);

        if (s == amount) {
            matches.add(partial);
        }

        if (s >= amount) {
            return;
        }

        for (int i=0; i < bills.size(); i++) {
            ArrayList<Integer> remaining = new ArrayList<>(bills.subList(i+1, bills.size()));
            ArrayList<Integer> suitableBills = new ArrayList<>(partial);
            suitableBills.add(bills.get(i));
            findBillsToMatchAmountRecursively(remaining, amount, suitableBills, matches);
        }

    }
}
