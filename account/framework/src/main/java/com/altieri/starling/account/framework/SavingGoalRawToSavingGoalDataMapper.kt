package com.altieri.starling.account.framework

import com.altieri.starling.account.data.SavingGoalData

class SavingGoalRawToSavingGoalDataMapper {
    operator fun invoke(savingGoal: SavingGoalRaw) =
        SavingGoalData(
            savingsGoalUid = savingGoal.savingsGoalUid,
            name = savingGoal.name
        )
}
