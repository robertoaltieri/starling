package com.altieri.starling.account.data

import com.altieri.starling.account.bl.Account

/**
 * Interface of the data source that the [AccountRepository] uses to store and fetch the accounts from the local DB
 */
interface AccountsLocalDataSource {
    suspend fun save(accounts: List<Account>)
    suspend fun primaryAccount(): AccountData?
}
