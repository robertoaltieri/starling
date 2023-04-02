package com.altieri.starling.account.framework

import com.altieri.starling.account.data.AccountData

class AccountRawToAccountDataMapper {
    operator fun invoke(accountRaw: AccountRaw) =
        AccountData(
            accountUid = accountRaw.accountUid,
            accountType = accountRaw.accountType,
            defaultCategory = accountRaw.defaultCategory,
            currency = accountRaw.currency,
        )
}
