package com.altieri.starling.account.framework

import com.altieri.starling.account.data.AccountData
import com.altieri.starling.account.data.AccountsNetworkDataSource
import com.altieri.starling.account.data.SavingGoalData

class AccountsNetworkDataSourceImpl(
    private val apiService: AccountsApiService,
    private val mapAccount: AccountRawToAccountDataMapper,
    private val mapSavingGoal: SavingGoalRawToSavingGoalDataMapper
) : AccountsNetworkDataSource {
    override suspend fun allAccounts(): List<AccountData> =
        apiService.accounts().accounts.map {
            mapAccount(it)
        }

    override suspend fun savingGoals(accountUid: String): List<SavingGoalData> =
        apiService.savingGoals(accountUid).savingsGoalList.map {
            mapSavingGoal(it)
        }

    override suspend fun addMoneyToGoal(
        accountUid: String,
        currency: String,
        savingsGoalUid: String,
        transferUid: String,
        minorUnits: Int
    ): String = apiService.addMoneyToGoal(
        accountUid = accountUid,
        savingsGoalUid = savingsGoalUid,
        transferUid = transferUid,
        addMoneyToGoalRequest = AddMoneyToGoalRequest(
            CurrencyAndAmountRaw(
                currency = currency,
                minorUnits = minorUnits
            )
        )
    ).transferUid

    override suspend fun createSavingGoal(
        accountUid: String,
        currency: String,
        name: String
    ): String =
        apiService.createSavingGoal(
            accountUid = accountUid,
            CreateSavingGoalRequest(
                currency = currency,
                name = name
            )
        ).savingsGoalUid!! // this will throw an exception in case savingsGoalUid is null
}

