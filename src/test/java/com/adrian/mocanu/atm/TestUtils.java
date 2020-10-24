package com.adrian.mocanu.atm;

import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TestUtils {

    public static String convertResourceToString(Resource resource) {

        try {
            Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8);
            return FileCopyUtils.copyToString(reader);
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void generatePairs(Map<String, Integer> pairs, int numberOfPairs) {
        for (int i = 1; i <= numberOfPairs; i++ ) {
            pairs.put(String.valueOf(i), i);
        }
    }
}
