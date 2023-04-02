package com.altieri.starling.account.data

import com.altieri.starling.account.bl.SavingGoal

class SavingGoalDataToSavingGoalMapper {
    // call this only after having checked the validity of the savingGoal
    operator fun invoke(savingGoal: SavingGoalData): SavingGoal =
        SavingGoal(
            savingsGoalUid = savingGoal.savingsGoalUid!!,
            name = savingGoal.name!!,
        )
}
