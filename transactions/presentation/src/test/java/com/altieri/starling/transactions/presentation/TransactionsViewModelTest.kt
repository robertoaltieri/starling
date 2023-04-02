package com.altieri.starling.transactions.presentation

import com.altieri.starling.coretesting.AppTestCoroutineRule
import com.altieri.starling.transactions.bl.RoundUpUpdateUseCase
import com.altieri.starling.transactions.bl.TransactionsStateUI
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionsViewModelTest {

    private val scheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(scheduler)

    @get:Rule
    val rule = AppTestCoroutineRule(testDispatcher)

    @MockK
    private lateinit var roundUpUpdateUseCase: RoundUpUpdateUseCase

    @MockK
    private lateinit var amountFormatter: AmountFormatter

    private lateinit var transactionsViewModel: TransactionsViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        transactionsViewModel = TransactionsViewModel(
            _transactionsState = MutableStateFlow(TransactionsStateUI.Idle),
            roundUpUpdateUseCase = roundUpUpdateUseCase,
            amountFormatter = amountFormatter,
            dispatcher = testDispatcher
        )
    }

    @Test
    fun `GIVEN the state is Idle and the call succeeds with roundUp positive WHEN onShowRoundUp THEN switch to Loading and then RoundUp`() =
        runTest {
            // given
            val emitted = mutableListOf<TransactionsStateUI>()
            transactionsViewModel.transactionsState.onEach {
                emitted.add(it)
            }.launchIn(CoroutineScope(testDispatcher))

            coEvery {
                roundUpUpdateUseCase(
                    accountUid = ACCOUNT_ID,
                    category = CATEGORY,
                    fractionDigits = FRACTION_DIGITS
                )
            } answers {
                scheduler.runCurrent()
                RET
            }
            every { amountFormatter(RET, FRACTION_DIGITS) } returns RET_FORMATTED

            // when
            transactionsViewModel.onShowRoundUp(
                accountUid = ACCOUNT_ID,
                category = CATEGORY,
                currency = CURRENCY,
                fractionDigits = FRACTION_DIGITS
            )
            scheduler.runCurrent()

            // then
            assertEquals(
                listOf(
                    TransactionsStateUI.Idle,
                    TransactionsStateUI.Loading,
                    TransactionsStateUI.RoundUp(ACCOUNT_ID, RET_FORMATTED, CURRENCY, RET, true)
                ),
                emitted
            )
        }

    @Test
    fun `GIVEN the state is Idle and the call succeeds with roundUp 0 WHEN onShowRoundUp THEN switch to Loading and then RoundUp`() =
        runTest {
            // given
            val emitted = mutableListOf<TransactionsStateUI>()
            transactionsViewModel.transactionsState.onEach {
                emitted.add(it)
            }.launchIn(CoroutineScope(testDispatcher))

            coEvery {
                roundUpUpdateUseCase(
                    accountUid = ACCOUNT_ID,
                    category = CATEGORY,
                    fractionDigits = FRACTION_DIGITS
                )
            } answers {
                scheduler.runCurrent()
                ZERO_ROUND_UP
            }
            every { amountFormatter(ZERO_ROUND_UP, FRACTION_DIGITS) } returns RET_FORMATTED

            // when
            transactionsViewModel.onShowRoundUp(
                accountUid = ACCOUNT_ID,
                category = CATEGORY,
                currency = CURRENCY,
                fractionDigits = FRACTION_DIGITS
            )
            scheduler.runCurrent()

            // then
            assertEquals(
                listOf(
                    TransactionsStateUI.Idle,
                    TransactionsStateUI.Loading,
                    TransactionsStateUI.RoundUp(ACCOUNT_ID, RET_FORMATTED, CURRENCY, ZERO_ROUND_UP, false)
                ),
                emitted
            )
        }

    @Test
    fun `GIVEN the state is Idle and the call fails WHEN onShowRoundUp THEN switch to Loading and then Error`() =
        runTest {
            // given
            val emitted = mutableListOf<TransactionsStateUI>()
            transactionsViewModel.transactionsState.onEach {
                emitted.add(it)
            }.launchIn(CoroutineScope(testDispatcher))

            coEvery {
                roundUpUpdateUseCase(
                    accountUid = ACCOUNT_ID,
                    category = CATEGORY,
                    fractionDigits = FRACTION_DIGITS
                )
            } answers {
                scheduler.runCurrent()
                throw Exception()
            }
            every { amountFormatter(RET, FRACTION_DIGITS) } returns RET_FORMATTED

            // when
            transactionsViewModel.onShowRoundUp(
                accountUid = ACCOUNT_ID,
                category = CATEGORY,
                currency = CURRENCY,
                fractionDigits = FRACTION_DIGITS
            )
            scheduler.runCurrent()

            // then
            assertEquals(
                listOf(
                    TransactionsStateUI.Idle,
                    TransactionsStateUI.Loading,
                    TransactionsStateUI.Error
                ),
                emitted
            )
        }

    private companion object {
        const val FRACTION_DIGITS = 2
        const val RET = 12345
        const val ZERO_ROUND_UP = 0
        const val RET_FORMATTED = "RET_FORMATTED"
        const val ACCOUNT_ID = "ACCOUNT_ID"
        const val CATEGORY = "CATEGORY"
        const val CURRENCY = "CURRENCY"
    }
}
