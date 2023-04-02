package com.altieri.starling.account.framework

import com.altieri.starling.account.bl.Account
import com.altieri.starling.account.data.AccountData
import com.altieri.starling.account.data.AccountsLocalDataSource
import com.altieri.starling.database.framework.AccountDBEntity
import com.altieri.starling.database.framework.AccountDao

class AccountsLocalDataSourceImpl(
    private val accountDao: AccountDao,
    private val toAccountDataMapper: AccountEntityToAccountDataMapper,
    private val toAccountDBEntityMapper: AccountToAccountEntityMapper
) : AccountsLocalDataSource {
    override suspend fun save(accounts: List<Account>) =
        accountDao.save(accounts.map {
            toAccountDBEntityMapper((it))
        })

    override suspend fun primaryAccount(): AccountData? =
        accountDao.primaryAccount().firstOrNull()?.let {
            toAccountDataMapper(it)
        }
}

class AccountToAccountEntityMapper {
    operator fun invoke(account: Account): AccountDBEntity =
        AccountDBEntity(
            accountUid = account.accountUid,
            accountType = account.accountType,
            defaultCategory = account.defaultCategory,
            currency = account.currency.currency,
        )
}

class AccountEntityToAccountDataMapper {
    operator fun invoke(account: AccountDBEntity): AccountData =
        AccountData(
            accountUid = account.accountUid,
            accountType = account.accountType,
            defaultCategory = account.defaultCategory,
            currency = account.currency
        )
}
