package com.altieri.starling.account.data

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SavingGoalValidatorTest {

    private val savingGoalValidator = SavingGoalValidator()

    @Test
    fun `GIVEN the goal is valid WHEN savingGoalValidator is called THEN return true`() {
        // given
        val savingGoal = SAVING_GOAL

        // when
        val ret = savingGoalValidator(savingGoal)

        // then
        assertTrue(ret)
    }

    @Test
    fun `GIVEN the savingsGoalUid is null in the goal WHEN savingGoalValidator is called THEN return false`() {
        // given
        val savingGoal = SAVING_GOAL.copy(savingsGoalUid = null)

        // when
        val ret = savingGoalValidator(savingGoal)

        // then
        assertFalse(ret)
    }


    @Test
    fun `GIVEN the name is null in the goal WHEN savingGoalValidator is called THEN return false`() {
        // given
        val savingGoal = SAVING_GOAL.copy(name = null)

        // when
        val ret = savingGoalValidator(savingGoal)

        // then
        assertFalse(ret)
    }

    private companion object {
        const val NAME = "NAME"
        const val SAVINGS_GOAL_UID = "SAVINGS_GOAL_UID"
        val SAVING_GOAL = SavingGoalData(
            savingsGoalUid = SAVINGS_GOAL_UID,
            name = NAME
        )
    }
}
