package com.altieri.starling.account.data

import com.altieri.starling.account.bl.Account
import com.altieri.starling.common.bl.currency.CurrencyInfoHelper

class AccountDataToAccountMapper(
    private val currencyInfoHelper: CurrencyInfoHelper
) {
    // call this only after having checked the validity of the account
    operator fun invoke(account: AccountData): Account {
        return Account(
            accountUid = account.accountUid!!,
            accountType = account.accountType!!,
            defaultCategory = account.defaultCategory!!,
            currency = currencyInfoHelper.info(currencyCode = account.currency!!)
        )
    }
}
