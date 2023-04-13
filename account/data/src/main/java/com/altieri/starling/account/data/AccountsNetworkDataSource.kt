package com.altieri.starling.account.data

/**
 * Interface of the data source that the [com.altieri.starling.account.bl.AccountRepository]
 * uses to performs the operation calling the backend API
 */
interface AccountsNetworkDataSource {
    suspend fun allAccounts(): List<AccountData>
    suspend fun createSavingGoal(
        accountUid: String,
        currency: String,
        name: String
    ): String
    suspend fun addMoneyToGoal(
        accountUid: String,
        currency: String,
        savingsGoalUid: String,
        transferUid: String,
        minorUnits: Int
    ): String
    suspend fun savingGoals(accountUid: String): List<SavingGoalData>
}
