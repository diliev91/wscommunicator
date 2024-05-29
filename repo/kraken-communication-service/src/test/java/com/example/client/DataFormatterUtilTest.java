package com.example.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DataFormatterUtilTest {

    private DataFormatterUtil dataFormatterUtil;

    @BeforeEach
    public void setUp() {
        dataFormatterUtil = new DataFormatterUtil();
    }

    @Test
    public void testFormat_RemovesDecimalAndLeadingZeros() {
        String input = "0.05000";
        String expectedOutput = "5000";
        String actualOutput = dataFormatterUtil.format(input);
        assert expectedOutput.equals(actualOutput);
    }

    @Test
    public void testFormat_RemovesOnlyDecimalIfNoLeadingZeros() {
        String input = "1.12345";
        String expectedOutput = "112345";
        String actualOutput = dataFormatterUtil.format(input);
        assert expectedOutput.equals(actualOutput);
    }

    @Test
    public void testFormat_HandlesAlreadyFormattedValue() {
        String input = "5000";
        String expectedOutput = "5000";
        String actualOutput = dataFormatterUtil.format(input);
        assert expectedOutput.equals(actualOutput);
    }

    @Test
    public void testFormat_HandlesOnlyZeros() {
        String input = "0.00000";
        String expectedOutput = "0";
        String actualOutput = dataFormatterUtil.format(input);
        assert expectedOutput.equals(actualOutput);
    }

    @Test
    public void testFormat_HandlesZeroWithNoDecimal() {
        String input = "0";
        String expectedOutput = "0";
        String actualOutput = dataFormatterUtil.format(input);
        assert expectedOutput.equals(actualOutput);
    }

    @Test
    public void testFormat_HandlesEmptyString() {
        String input = "";
        String expectedOutput = "0";
        String actualOutput = dataFormatterUtil.format(input);
        assert expectedOutput.equals(actualOutput);
    }

    @Test
    public void testFormat_HandlesNullInput() {
        String input = null;
        String expectedOutput = "0";
        String actualOutput = dataFormatterUtil.format(input);
        assert expectedOutput.equals(actualOutput);
    }
}
