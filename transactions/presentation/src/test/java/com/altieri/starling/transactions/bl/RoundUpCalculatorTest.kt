package com.altieri.starling.transactions.bl

import org.junit.Assert.assertEquals
import org.junit.Test

class RoundUpCalculatorTest {

    private val roundUpCalculator = RoundUpCalculator()

    @Test
    fun `GIVEN a non empty list WHEN roundUp THEN return the correct value`() {
        assertEquals(158, roundUpCalculator.roundUp(all, FRACTION_DIGITS))
    }

    @Test
    fun `GIVEN a non empty list of integers WHEN roundUp THEN return 0`() {
        assertEquals(0, roundUpCalculator.roundUp(allInt, FRACTION_DIGITS))
    }

    @Test
    fun `GIVEN an empty list WHEN roundUp THEN return 0`() {
        assertEquals(0, roundUpCalculator.roundUp(emptyList(), FRACTION_DIGITS))
    }

    private companion object {
        const val FRACTION_DIGITS = 2
        val transaction1 = Transaction(
            feedItemUid = "1",
            amount = Amount(
                currency = "GBP",
                minorUnits = 435
            ),
            direction = Transaction.Direction.OUT,
            spendingCategory = Transaction.SpendingCategory.OTHER
        )
        val transaction2 = Transaction(
            feedItemUid = "2",
            amount = Amount(
                currency = "GBP",
                minorUnits = 520
            ),
            direction = Transaction.Direction.OUT,
            spendingCategory = Transaction.SpendingCategory.OTHER
        )
        val transaction3 = Transaction(
            feedItemUid = "3",
            amount = Amount(
                currency = "GBP",
                minorUnits = 87
            ),
            direction = Transaction.Direction.OUT,
            spendingCategory = Transaction.SpendingCategory.OTHER
        )
        val transactionInt1 = Transaction(
            feedItemUid = "1",
            amount = Amount(
                currency = "GBP",
                minorUnits = 400
            ),
            direction = Transaction.Direction.OUT,
            spendingCategory = Transaction.SpendingCategory.OTHER
        )
        val transactionInt2 = Transaction(
            feedItemUid = "2",
            amount = Amount(
                currency = "GBP",
                minorUnits = 500
            ),
            direction = Transaction.Direction.OUT,
            spendingCategory = Transaction.SpendingCategory.OTHER
        )
        val transactionInt3 = Transaction(
            feedItemUid = "3",
            amount = Amount(
                currency = "GBP",
                minorUnits = 800
            ),
            direction = Transaction.Direction.OUT,
            spendingCategory = Transaction.SpendingCategory.OTHER
        )
        val all = listOf(transaction1, transaction2, transaction3)
        val allInt = listOf(transactionInt1, transactionInt2, transactionInt3)
    }
}
