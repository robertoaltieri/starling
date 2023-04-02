package com.altieri.starling.transactions.data

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TransactionValidatorTest {

    private lateinit var transactionValidator: TransactionValidator

    @Before
    fun setUp() {
        transactionValidator = TransactionValidator()
    }

    @Test
    fun `GIVEN a valid transaction WHEN transactionValidator is called THEN return true`() {
        // given
        val transaction = VALID

        // when
        val ret = transactionValidator(transaction)

        // then
        assertTrue(ret)
    }

    @Test
    fun `GIVEN the transaction has a null feedItemUid WHEN transactionValidator is called THEN return false`() {
        // given
        val transaction = VALID.copy(feedItemUid = null)

        // when
        val ret = transactionValidator(transaction)

        // then
        assertFalse(ret)
    }

    @Test
    fun `GIVEN the transaction has a null currency WHEN transactionValidator is called THEN return false`() {
        // given
        val transaction = VALID.copy(amount = VALID.amount!!.copy(
            currency = null
        ))

        // when
        val ret = transactionValidator(transaction)

        // then
        assertFalse(ret)
    }

    @Test
    fun `GIVEN the transaction has a null minorUnits WHEN transactionValidator is called THEN return false`() {
        // given
        val transaction = VALID.copy(amount = VALID.amount!!.copy(
            minorUnits = null
        ))

        // when
        val ret = transactionValidator(transaction)

        // then
        assertFalse(ret)
    }

    @Test
    fun `GIVEN the transaction has a null direction WHEN transactionValidator is called THEN return false`() {
        // given
        val transaction = VALID.copy(direction = null)

        // when
        val ret = transactionValidator(transaction)

        // then
        assertFalse(ret)
    }

    @Test
    fun `GIVEN the transaction has a null spendingCategory WHEN transactionValidator is called THEN return false`() {
        // given
        val transaction = VALID.copy(spendingCategory = null)

        // when
        val ret = transactionValidator(transaction)

        // then
        assertFalse(ret)
    }

    private companion object {
        const val CURRENCY = "CURRENCY"
        const val AMOUNT = 123L
        val VALID = TransactionData(
            feedItemUid = "feedItemUid",
            amount = AmountData(
                currency = CURRENCY,
                minorUnits = AMOUNT
            ),
            direction = "direction",
            spendingCategory = "spendingCategory"
        )
    }
}
