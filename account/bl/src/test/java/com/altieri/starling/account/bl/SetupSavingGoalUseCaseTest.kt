package com.altieri.starling.account.bl

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals

import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SetupSavingGoalUseCaseTest {

    @MockK
    private lateinit var repository: AccountRepository

    @MockK
    private lateinit var filter: SavingGoalFilter

    private lateinit var setupSavingGoalUseCase: SetupSavingGoalUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        setupSavingGoalUseCase = SetupSavingGoalUseCase(repository, filter)
    }

    @After
    fun tearDown() {
        confirmVerified(repository, filter)
    }

    @Test
    fun `GIVEN there is a saving goal with the correct name WHEN setupSavingGoalUseCase is called THEN return the saving uid of that goal`() =
        runTest {
            // given
            coEvery {
                repository.savingGoals(
                    accountUid = ACCOUNT_UID
                )
            } returns listOf(SAVING_GOAL1, SAVING_GOAL2)
            every {
                filter.goalWith(list = listOf(SAVING_GOAL1, SAVING_GOAL2), name = NAME)
            } returns SAVING_GOAL1

            // when
            val uid = setupSavingGoalUseCase(
                accountUid = ACCOUNT_UID,
                currency = CURRENCY,
                name = NAME
            )

            // then
            coVerify {
                repository.savingGoals(
                    accountUid = ACCOUNT_UID
                )
            }
            verify {
                filter.goalWith(list = listOf(SAVING_GOAL1, SAVING_GOAL2), name = NAME)
            }
            assertEquals(SAVINGS_GOAL_UID, uid)
        }

    @Test
    fun `GIVEN there are no saving goals with the correct name for the account WHEN setupSavingGoalUseCase is called THEN create it and return its saving uid`() =
        runTest {
            // given
            coEvery {
                repository.savingGoals(
                    accountUid = ACCOUNT_UID
                )
            } returns listOf(SAVING_GOAL1, SAVING_GOAL2)

            coEvery {
                repository.createSavingGoal(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    name = NAME
                )
            } returns SAVINGS_GOAL_UID
            every {
                filter.goalWith(list = listOf(SAVING_GOAL1, SAVING_GOAL2), name = NAME)
            } returns null

            // when
            val uid = setupSavingGoalUseCase(
                accountUid = ACCOUNT_UID,
                currency = CURRENCY,
                name = NAME
            )

            // then
            coVerify {
                repository.savingGoals(
                    accountUid = ACCOUNT_UID
                )
            }
            coVerify {
                repository.createSavingGoal(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    name = NAME
                )
            }
            verify {
                filter.goalWith(list = listOf(SAVING_GOAL1, SAVING_GOAL2), name = NAME)
            }
            assertEquals(SAVINGS_GOAL_UID, uid)
        }

    private companion object {
        const val ACCOUNT_UID = "ACCOUNT_UID"
        const val CURRENCY = "CURRENCY"
        const val NAME = "NAME"
        const val NAME2 = "NAME2"
        const val SAVINGS_GOAL_UID = "SAVINGS_GOAL_UID"
        const val SAVINGS_GOAL_UID2 = "SAVINGS_GOAL_UID2"
        val SAVING_GOAL1 = SavingGoal(
            savingsGoalUid = SAVINGS_GOAL_UID,
            name = NAME
        )
        val SAVING_GOAL2 = SavingGoal(
            savingsGoalUid = SAVINGS_GOAL_UID2,
            name = NAME2
        )
    }
}
