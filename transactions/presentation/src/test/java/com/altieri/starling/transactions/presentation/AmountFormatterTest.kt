package com.altieri.starling.transactions.presentation

import org.junit.Assert.assertEquals
import org.junit.Test

class AmountFormatterTest {

    // I used only one method because these are very simple checks
    @Test
    fun fixDecimal() {
        assertEquals("0.00", AMOUNT_FORMATTER(0, 2))
        assertEquals("1.00", AMOUNT_FORMATTER(100, 2))
        assertEquals("10.00", AMOUNT_FORMATTER(1000, 2))
        assertEquals("10.10", AMOUNT_FORMATTER(1010, 2))
        assertEquals("123.00", AMOUNT_FORMATTER(12300, 2))
        assertEquals("123.40", AMOUNT_FORMATTER(12340, 2))
        assertEquals("123.45", AMOUNT_FORMATTER(12345, 2))

        assertEquals("0", AMOUNT_FORMATTER(0, 0))
        assertEquals("1", AMOUNT_FORMATTER(1, 0))
        assertEquals("10", AMOUNT_FORMATTER(10, 0))
        assertEquals("123", AMOUNT_FORMATTER(123, 0))

    }
}
