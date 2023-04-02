package com.altieri.starling.account.bl

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After

import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddMoneyToSavingGoalUseCaseTest {

    @MockK
    private lateinit var repository: AccountRepository

    private lateinit var addMoneyToSavingGoalUseCase: AddMoneyToSavingGoalUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        addMoneyToSavingGoalUseCase = AddMoneyToSavingGoalUseCase(repository)
    }

    @After
    fun tearDown() {
        confirmVerified(repository)
    }

    @Test
    fun `GIVEN there is a primary account WHEN addMoneyToSavingGoalUseCase is called THEN return the primary account`() =
        runTest {
            // given
            coEvery {
                repository.addMoneyToGoal(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    savingsGoalUid = SAVINGS_GOAL_UID,
                    minorUnits = MINOR_UNITS
                )
            } answers {}

            // when
            addMoneyToSavingGoalUseCase(
                accountUid = ACCOUNT_UID,
                currency = CURRENCY,
                savingsGoalUid = SAVINGS_GOAL_UID,
                minorUnits = MINOR_UNITS
            )

            // then
            coVerify {
                repository.addMoneyToGoal(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    savingsGoalUid = SAVINGS_GOAL_UID,
                    minorUnits = MINOR_UNITS
                )
            }
        }

    private companion object {
        const val ACCOUNT_UID = "ACCOUNT_UID"
        const val CURRENCY = "CURRENCY"
        const val SAVINGS_GOAL_UID = "SAVINGS_GOAL_UID"
        const val MINOR_UNITS = 123
    }
}
