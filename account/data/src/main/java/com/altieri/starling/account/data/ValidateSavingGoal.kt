package com.altieri.starling.account.data

class SavingGoalValidator {
    operator fun invoke(savingGoalData: SavingGoalData): Boolean =
        savingGoalData.savingsGoalUid != null &&
                savingGoalData.name != null

}
