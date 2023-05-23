package com.example.employee;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParameterizedTests {
    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5, -3, 15, Integer.MAX_VALUE})
    void testInteger(int number) {
        assertTrue(number % 2 != 0);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void testStrings(String str) {
        assertTrue(str == null || str.trim().isEmpty());
    }

    @ParameterizedTest
    @EmptySource
    void testStringsEmptySource(String str) {
        assertTrue(str == null || str.trim().isEmpty());
    }

    @ParameterizedTest
    @NullSource
    void testStringsNullSource(String str) {
        assertTrue(str == null || str.trim().isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testStringsNullAndEmptySource(String str) {
        assertTrue(str == null || str.trim().isEmpty());
    }

    @ParameterizedTest
    @EnumSource(Month.class)
    void testEnumSource(Month month) {
        assertTrue(month.getValue() >= 1 && month.getValue() <= 12);
    }

    @ParameterizedTest
    @CsvSource(value = {"test,TEST", "tEst,TEST", "Java,JAVA"}, delimiterString = ",")
    void testCsvSource(String input, String expected) {
        assertEquals(expected, input.toUpperCase());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/param.csv", numLinesToSkip = 1)
    void testCsvFile(String input, String expected) {
        assertEquals(expected, input.toUpperCase());
    }
}
