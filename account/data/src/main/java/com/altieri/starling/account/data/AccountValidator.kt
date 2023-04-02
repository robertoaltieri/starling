package com.altieri.starling.account.data

class AccountValidator {
    operator fun invoke(account: AccountData): Boolean =
        account.accountUid != null &&
                account.defaultCategory != null &&
                account.currency != null

}
