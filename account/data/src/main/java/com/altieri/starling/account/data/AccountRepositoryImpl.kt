package com.altieri.starling.account.data

import com.altieri.starling.account.bl.Account
import com.altieri.starling.account.bl.AccountRepository
import com.altieri.starling.account.bl.SavingGoal
import com.altieri.starling.common.bl.AppUUIDGenerator

/**
 * Repository for the account.
 *
 * It allows retrieving the primary account. It caches the result and returns the same instance if called multiple times
 * It saves the accounts in a local DB and returns the primary account from there if present.
 * It fetches the accounts from the backend when the primary account is missing in the local DB.
 *
 * For the saving goals it allows to:
 * - create a new saving goals
 * - list all the saving goals
 * - add money to a saving goals
 *
 */
class AccountRepositoryImpl(
    private val accountsNetworkDataSource: AccountsNetworkDataSource,
    private val accountsLocalDataSource: AccountsLocalDataSource,
    private val validateAccount: AccountValidator,
    private val validateSavingGoal: SavingGoalValidator,
    private val mapAccount: AccountDataToAccountMapper,
    private val mapSavingGoal: SavingGoalDataToSavingGoalMapper,
    private val uuidGenerator: AppUUIDGenerator,
    private var cachedAccount: Account?
) : AccountRepository {

    override suspend fun primaryAccount(): Account? =
        cachedAccount ?: accountsLocalDataSource.primaryAccount()?.toAccount() ?: primary()

    override suspend fun savingGoals(accountUid: String): List<SavingGoal> =
        accountsNetworkDataSource.savingGoals(accountUid).filter {
            validateSavingGoal(it)
        }.map {
            mapSavingGoal(it)
        }

    override suspend fun createSavingGoal(
        accountUid: String,
        currency: String,
        name: String
    ): String =
        accountsNetworkDataSource.createSavingGoal(
            accountUid = accountUid,
            currency = currency,
            name = name
        )

    override suspend fun addMoneyToGoal(
        accountUid: String,
        currency: String,
        savingsGoalUid: String,
        minorUnits: Int
    ) {
        // I'm considering this as a details that the upper layers don't need to know
        // that's why I'm creating the UUID here
        val transferUid: String = uuidGenerator.randomUUID()

        val ret = accountsNetworkDataSource.addMoneyToGoal(
            accountUid = accountUid,
            currency = currency,
            transferUid = transferUid,
            savingsGoalUid = savingsGoalUid,
            minorUnits = minorUnits
        )
        if (transferUid != ret) throw IllegalStateException("expected transferUid=$transferUid actual=$ret")
    }

    private fun AccountData.toAccount() = mapAccount(this)

    private suspend fun primary(): Account? =
        allAccounts().firstOrNull { it.accountType == AccountType.PRIMARY }.also {
            cachedAccount = it
        }

    private suspend fun allAccounts(): List<Account> =
        accountsNetworkDataSource.allAccounts().filter {
            validateAccount(it)
        }.map {
            it.toAccount()
        }.also {
            if (it.isNotEmpty()) {
                accountsLocalDataSource.save(it)
            }
        }
}
