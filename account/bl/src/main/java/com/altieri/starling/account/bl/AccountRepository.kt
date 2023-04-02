package com.altieri.starling.account.bl

interface AccountRepository {
    suspend fun primaryAccount(): Account?
    suspend fun savingGoals(accountUid: String): List<SavingGoal>
    suspend fun createSavingGoal(
        accountUid: String,
        currency: String,
        name: String
    ): String

    suspend fun addMoneyToGoal(
        accountUid: String,
        currency: String,
        savingsGoalUid: String,
        minorUnits: Int
    )
}
