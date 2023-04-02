package com.altieri.starling

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.core.util.Consumer
import androidx.navigation.compose.rememberNavController
import com.altieri.starling.account.presentation.AccountsStateUI
import com.altieri.starling.account.presentation.AccountsViewModel
import com.altieri.starling.nav.Destinations
import com.altieri.starling.transactions.presentation.TransactionsViewModel
import com.altieri.starling.ui.composable.MainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val transactionsViewModel: TransactionsViewModel by viewModels()
    private val accountsViewModel: AccountsViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val transactionsState = transactionsViewModel.transactionsState.collectAsState()
            val accountState = accountsViewModel.accountState.collectAsState()
            val savingGoalState = accountsViewModel.savingGoalState.collectAsState()

            LaunchedEffect(Unit) {
                accountsViewModel.updateAccount()
            }
            LaunchedEffect(accountState.value) {
                val state = accountState.value
                if (state is AccountsStateUI.PrimaryAccounts) {

                    navController.navigate(Destinations.SavingGoal.route)
                    val account = state.account
                    transactionsViewModel.onShowRoundUp(
                        accountUid = account.accountUid,
                        category = account.defaultCategory,
                        currency = account.currency.currency,
                        fractionDigits = account.currency.fractionDigits
                    )
                }
            }
            Scaffold(
                modifier = Modifier.semantics {
                    testTagsAsResourceId = true
                }
            ) { padding ->
                MainScreen(
                    modifier = Modifier.padding(padding),
                    navController = navController,
                    accountState = accountState.value,
                    transactionsState = transactionsState.value,
                    savingGoalState = savingGoalState.value,
                    onSaveInGoal = { accountUid, currency, minorUnits ->
                        accountsViewModel.addMoneyToSavingGoal(
                            accountUid = accountUid,
                            currency = currency,
                            minorUnits = minorUnits
                        )
                    },
                    onNewTokens = { accessToken, refreshToken, expiresIn ->
                        accountsViewModel.onNewTokens(
                            accessToken = accessToken,
                            refreshToken = refreshToken,
                            expiresIn = expiresIn
                        )
                    }
                )
            }
        }
    }
}
