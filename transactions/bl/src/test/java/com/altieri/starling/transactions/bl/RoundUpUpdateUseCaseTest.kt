package com.altieri.starling.transactions.bl

import com.altieri.starling.datetime.bl.AppDateTime
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class RoundUpUpdateUseCaseTest {

    @MockK
    private lateinit var transactionsRepository: TransactionsRepository

    @MockK
    private lateinit var roundUpCalculator: RoundUpCalculator

    @MockK
    private lateinit var dateTime: AppDateTime

    private lateinit var roundUpUpdateUseCase: RoundUpUpdateUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        roundUpUpdateUseCase = RoundUpUpdateUseCase(
            transactionsRepository = transactionsRepository,
            roundUpCalculator = roundUpCalculator,
            dateTime = dateTime
        )
    }

    @Test
    fun `GIVEN the current time and account WHEN roundUp THEN returns the correct value`() =
        runTest {
            coEvery { dateTime.oneWeekAgo() } returns TIME
            coEvery {
                transactionsRepository.transactionsSince(
                    accountUid = ACCOUNT_ID,
                    category = CATEGORY,
                    time = TIME
                )
            } returns all
            coEvery { roundUpCalculator.roundUp(relevant, FRACTION_DIGITS) } returns RET

            val ret = roundUpUpdateUseCase(
                accountUid = ACCOUNT_ID,
                category = CATEGORY,
                fractionDigits = FRACTION_DIGITS
            )

            assertEquals(RET, ret)
        }

    private companion object {
        const val FRACTION_DIGITS = 2
        const val RET = 123
        const val TIME = "TIME"
        const val ACCOUNT_ID = "ACCOUNT_ID"
        const val CATEGORY = "CATEGORY"
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
                minorUnits = 321
            ),
            direction = Transaction.Direction.OUT,
            spendingCategory = Transaction.SpendingCategory.SAVING
        )
        val transaction4 = Transaction(
            feedItemUid = "4",
            amount = Amount(
                currency = "GBP",
                minorUnits = 456
            ),
            direction = Transaction.Direction.IN,
            spendingCategory = Transaction.SpendingCategory.OTHER
        )
        val all = listOf(transaction1, transaction2, transaction3, transaction4)
        val relevant = listOf(transaction1, transaction2)
    }
}
