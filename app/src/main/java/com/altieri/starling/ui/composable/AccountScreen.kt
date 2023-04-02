package com.altieri.starling.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.altieri.starling.R
import com.altieri.starling.account.presentation.AccountsStateUI

@Composable
fun AccountScreen(accountState: AccountsStateUI) {
    when (accountState) {
        is AccountsStateUI.PrimaryAccounts -> {
        }

        is AccountsStateUI.Loading -> {
            FullScreenLoadingAnimation(true)
        }

        AccountsStateUI.Idle -> {}
        AccountsStateUI.Error -> {
            AppText(text = stringResource(id = R.string.error))
        }
    }
}
