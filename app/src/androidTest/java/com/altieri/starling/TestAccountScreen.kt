package com.altieri.starling

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.testing.TestNavHostController
import com.altieri.starling.account.presentation.AccountsStateUI
import com.altieri.starling.account.presentation.SavingGoalStateUI
import com.altieri.starling.nav.Destinations
import com.altieri.starling.transactions.bl.TransactionsStateUI
import com.altieri.starling.ui.composable.MainScreen
import com.altieri.starling.ui.composable.TestTags
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TestAccountScreen {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    @Test
    fun givenTheAppIsLoadingTheAccountWhenInTheAccountScreenThenShowFullScreenLoadingAnimation() {

        composeTestRule.setContent {
            navController = setTestNavController()
            val accountState = AccountsStateUI.Loading
            val transactionsState = TransactionsStateUI.Idle
            val savingGoalState = SavingGoalStateUI.Idle
            MainScreen(
                navController = navController,
                accountState = accountState,
                transactionsState = transactionsState,
                savingGoalState = savingGoalState,
                onSaveInGoal = {_, _, _ ->},
                onNewTokens = {_, _, _ ->}
            )
            navController.setCurrentDestination(Destinations.Account.route)
        }

        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithTag(TestTags.FullScreenLoadingAnimation).isDisplayed()
        }
    }

    @Test
    fun givenTheLoadingOfTheAccountFailsWhenInTheAccountScreenThenShowError() {
        var error = ""
        composeTestRule.setContent {
            error = stringResource(id = R.string.error)

            navController = setTestNavController()
            val accountState = AccountsStateUI.Error
            val transactionsState = TransactionsStateUI.Idle
            val savingGoalState = SavingGoalStateUI.Idle
            MainScreen(
                navController = navController,
                accountState = accountState,
                transactionsState = transactionsState,
                savingGoalState = savingGoalState,
                onSaveInGoal = {_, _, _ ->},
                onNewTokens = {_, _, _ ->}
            )
            navController.setCurrentDestination(Destinations.Account.route)
        }
        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithText(error).isDisplayed()
        }
    }

}
