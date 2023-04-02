package com.altieri.starling.account.presentation

import com.altieri.starling.account.bl.Account

sealed interface AccountsStateUI {
    object Idle: AccountsStateUI
    object Loading: AccountsStateUI
    object Error: AccountsStateUI
    data class PrimaryAccounts(
        val account: Account
    ): AccountsStateUI
}
