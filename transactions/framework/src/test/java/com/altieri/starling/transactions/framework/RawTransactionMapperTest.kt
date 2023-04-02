package com.altieri.starling.transactions.framework

import com.altieri.starling.transactions.data.AmountData
import com.altieri.starling.transactions.data.TransactionData
import org.junit.Test
import kotlin.test.assertEquals

class RawTransactionMapperTest {

    private val rawTransactionMapper = RawTransactionMapper()

    @Test
    fun `WHEN RawTransactionMapper THEN return the correct transaction data`() {
        val ret = rawTransactionMapper(TRANSACTION_RAW)
        assertEquals(TRANSACTION_DATA, ret)
    }

    private companion object {
        const val CURRENCY = "CURRENCY"
        const val SPENDING_CATEGORY = "SPENDING_CATEGORY"
        const val DIRECTION = "OUT"
        const val FEED_ITEM_UID = "FEED_ITEM_UID"
        const val MINOR_UNITS = 123L
        val AMOUNT_RAW = AmountRaw(
            currency = CURRENCY,
            minorUnits = MINOR_UNITS
        )
        val AMOUNT_DATA = AmountData(
            currency = CURRENCY,
            minorUnits = MINOR_UNITS
        )
        val TRANSACTION_RAW = TransactionRaw(
            feedItemUid = FEED_ITEM_UID,
            amount = AMOUNT_RAW,
            direction = DIRECTION,
            spendingCategory = SPENDING_CATEGORY
        )
        val TRANSACTION_DATA = TransactionData(
            feedItemUid = FEED_ITEM_UID,
            amount = AMOUNT_DATA,
            direction = DIRECTION,
            spendingCategory = SPENDING_CATEGORY
        )
    }
}
