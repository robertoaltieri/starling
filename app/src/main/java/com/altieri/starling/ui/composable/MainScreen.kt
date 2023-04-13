package com.altieri.starling.ui.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.altieri.starling.account.presentation.AccountsStateUI
import com.altieri.starling.account.presentation.SavingGoalStateUI
import com.altieri.starling.nav.Destinations
import com.altieri.starling.nav.composable
import com.altieri.starling.transactions.bl.TransactionsStateUI
import com.altieri.starling.ui.composable.deeplink.DeepLinks
import com.altieri.starling.ui.composable.event.OnNewToken
import com.altieri.starling.ui.composable.event.OnSaveInGoal
import com.altieri.starling.ui.theme.AppTheme

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    accountState: AccountsStateUI,
    transactionsState: TransactionsStateUI,
    savingGoalState: SavingGoalStateUI,
    onSaveInGoal: OnSaveInGoal,
    onNewTokens: OnNewToken
) {
    AppTheme {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(
                navController = navController,
                startDestination = Destinations.Account.route
            ) {
                composable(
                    route = Destinations.Token.route,
                    arguments = DeepLinks.OnNewTokens.ON_NEW_TOKENS_DEEP_LINK_ARGS,
                    deepLinks = listOf(navDeepLink {
                        uriPattern = DeepLinks.OnNewTokens.URI_PATTERN
                    })
                ) { backStackEntry ->
                    TokenUpdatesScreen(backStackEntry, onNewTokens)
                }

                composable(Destinations.Account) {
                    AccountScreen(
                        accountState = accountState
                    )
                }
                composable(Destinations.SavingGoal) {
                    SaveInGoalScreen(
                        transactionsState = transactionsState,
                        savingGoalState = savingGoalState,
                        onSaveInGoal = onSaveInGoal
                    )
                }
            }
        }
    }
}
