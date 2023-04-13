package com.altieri.starling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.altieri.starling.account.presentation.AccountsStateUI
import com.altieri.starling.account.presentation.AccountsViewModel
import com.altieri.starling.account.presentation.SavingGoalStateUI
import com.altieri.starling.nav.Destinations
import com.altieri.starling.transactions.bl.TransactionsStateUI
import com.altieri.starling.transactions.presentation.TransactionsViewModel
import com.altieri.starling.ui.composable.MainScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val accountsViewModel: AccountsViewModel by viewModels()
    private val transactionsViewModel: TransactionsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            MainContent(
                navController = navController,
                accountsViewModel = accountsViewModel,
                transactionsViewModel = transactionsViewModel
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
fun MainContent(
    navController: NavHostController,
    accountsViewModel: AccountsViewModel,
    transactionsViewModel: TransactionsViewModel
) {
    val transactionsState = transactionsViewModel.transactionsState.collectAsStateWithLifecycle(TransactionsStateUI.Idle)
    val accountState = accountsViewModel.accountState.collectAsStateWithLifecycle(AccountsStateUI.Idle)
    val savingGoalState = accountsViewModel.savingGoalState.collectAsStateWithLifecycle(SavingGoalStateUI.Idle)

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
        } else if (state is AccountsStateUI.Idle) {
            accountsViewModel.updateAccount()
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
