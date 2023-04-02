package com.altieri.starling

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.testing.TestNavHostController
import com.altieri.starling.account.bl.Account
import com.altieri.starling.account.presentation.AccountsStateUI
import com.altieri.starling.account.presentation.SavingGoalStateUI
import com.altieri.starling.common.bl.currency.AppCurrencyInfo
import com.altieri.starling.nav.Destinations
import com.altieri.starling.transactions.bl.TransactionsStateUI
import com.altieri.starling.ui.composable.MainScreen
import com.altieri.starling.ui.composable.TestTags
import org.junit.Rule
import org.junit.Test


class TestSaveInGoalScreen {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    @Test
    fun givenTheAppIsLoadingTheRoundUpWhenInTheSavingGoalScreenThenShowFullScreenLoadingAnimation() {

        composeTestRule.setContent {
            navController = setTestNavController()
            val accountState = AccountsStateUI.PrimaryAccounts(ACCOUNT)
            val transactionsState = TransactionsStateUI.Loading
            val savingGoalState = SavingGoalStateUI.Idle
            MainScreen(
                navController = navController,
                accountState = accountState,
                transactionsState = transactionsState,
                savingGoalState = savingGoalState,
                onSaveInGoal = { _, _, _ -> },
                onNewTokens = { _, _, _ -> }
            )
            navController.setCurrentDestination(Destinations.SavingGoal.route)
        }

        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithTag(TestTags.FullScreenLoadingAnimation).isDisplayed()
        }
    }

    @Test
    fun givenTheLoadingOfTheTransactionsFailsWhenInTheSavingGoalScreenThenShowError() {
        var error = ""
        composeTestRule.setContent {
            error = stringResource(id = R.string.error)

            navController = setTestNavController()
            val accountState = AccountsStateUI.Idle
            val transactionsState = TransactionsStateUI.Error
            val savingGoalState = SavingGoalStateUI.Idle
            MainScreen(
                navController = navController,
                accountState = accountState,
                transactionsState = transactionsState,
                savingGoalState = savingGoalState,
                onSaveInGoal = { _, _, _ -> },
                onNewTokens = { _, _, _ -> }
            )
            navController.setCurrentDestination(Destinations.SavingGoal.route)
        }
        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithText(error).isDisplayed()
        }
    }

    @Test
    fun givenTheCalculationOfTheRoundUpSucceedWhenInTheSavingGoalScreenThenShowTheRoundUpAndTheButton() {
        var saveInGoal = ""
        var roundUp = ""
        composeTestRule.setContent {
            saveInGoal = stringResource(id = R.string.save_in_goal)
            roundUp = stringResource(id = R.string.roundup, ROUND_UP, CURRENCY)

            navController = setTestNavController()
            val accountState = AccountsStateUI.Idle
            val transactionsState = TransactionsStateUI.RoundUp(
                accountUid = ACCOUNT_UID,
                roundUp = ROUND_UP,
                currency = CURRENCY,
                roundUpMinorUnits = 12345,
                saveInGoalButtonEnabled = true
            )
            val savingGoalState = SavingGoalStateUI.Idle
            MainScreen(
                navController = navController,
                accountState = accountState,
                transactionsState = transactionsState,
                savingGoalState = savingGoalState,
                onSaveInGoal = { _, _, _ -> },
                onNewTokens = { _, _, _ -> }
            )
            navController.setCurrentDestination(Destinations.SavingGoal.route)
        }
        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithText(roundUp).isDisplayed()
        }
        composeTestRule.onNodeWithText(saveInGoal).assertIsDisplayed()
    }

    @Test
    fun givenTheCalculationOfTheRoundUpSucceededWhenInTheSavingGoalScreenThenShowTheRoundUpAndTheButton() {
        var saveInGoal = ""
        var roundUp = ""
        composeTestRule.setContent {
            saveInGoal = stringResource(id = R.string.save_in_goal)
            roundUp = stringResource(id = R.string.roundup, ROUND_UP, CURRENCY)

            navController = setTestNavController()
            val accountState = AccountsStateUI.Idle
            val transactionsState = TransactionsStateUI.RoundUp(
                accountUid = ACCOUNT_UID,
                roundUp = ROUND_UP,
                currency = CURRENCY,
                roundUpMinorUnits = 12345,
                saveInGoalButtonEnabled = true
            )
            val savingGoalState = SavingGoalStateUI.Idle
            MainScreen(
                navController = navController,
                accountState = accountState,
                transactionsState = transactionsState,
                savingGoalState = savingGoalState,
                onSaveInGoal = { _, _, _ -> },
                onNewTokens = { _, _, _ -> }
            )
            navController.setCurrentDestination(Destinations.SavingGoal.route)
        }
        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithText(roundUp).isDisplayed()
        }
        composeTestRule.onNodeWithText(saveInGoal).assertIsDisplayed()
    }

    @Test
    fun givenTheAppIsAddingTheMoneyInTheGoalWhenInTheSavingGoalScreenThenShowTheRoundUpAndTheDisabledButton() {
        var saveInGoal = ""
        var roundUp = ""
        composeTestRule.setContent {
            saveInGoal = stringResource(id = R.string.save_in_goal)
            roundUp = stringResource(id = R.string.roundup, ROUND_UP, CURRENCY)

            navController = setTestNavController()
            val accountState = AccountsStateUI.Idle
            val transactionsState = TransactionsStateUI.RoundUp(
                accountUid = ACCOUNT_UID,
                roundUp = ROUND_UP,
                currency = CURRENCY,
                roundUpMinorUnits = 12345,
                saveInGoalButtonEnabled = true
            )
            val savingGoalState = SavingGoalStateUI.Running
            MainScreen(
                navController = navController,
                accountState = accountState,
                transactionsState = transactionsState,
                savingGoalState = savingGoalState,
                onSaveInGoal = { _, _, _ -> },
                onNewTokens = { _, _, _ -> }
            )
            navController.setCurrentDestination(Destinations.SavingGoal.route)
        }
        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithText(roundUp).isDisplayed()
        }
        composeTestRule.onNodeWithText(saveInGoal).assertIsDisplayed().assertIsNotEnabled()
    }

    @Test
    fun givenTheAppHasAddedTheMoneyInTheGoalWhenInTheSavingGoalScreenThenShowTheRoundUpAndHideTheButton() {
        var saveInGoal = ""
        var roundUpSaved = ""
        composeTestRule.setContent {
            saveInGoal = stringResource(id = R.string.save_in_goal)
            roundUpSaved = stringResource(id = R.string.roundup_saved, ROUND_UP, CURRENCY)

            navController = setTestNavController()
            val accountState = AccountsStateUI.Idle
            val transactionsState = TransactionsStateUI.RoundUp(
                accountUid = ACCOUNT_UID,
                roundUp = ROUND_UP,
                currency = CURRENCY,
                roundUpMinorUnits = 12345,
                saveInGoalButtonEnabled = true
            )
            val savingGoalState = SavingGoalStateUI.Success
            MainScreen(
                navController = navController,
                accountState = accountState,
                transactionsState = transactionsState,
                savingGoalState = savingGoalState,
                onSaveInGoal = { _, _, _ -> },
                onNewTokens = { _, _, _ -> }
            )
            navController.setCurrentDestination(Destinations.SavingGoal.route)
        }
        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithText(roundUpSaved).isDisplayed()
        }
        composeTestRule.onNodeWithText(saveInGoal).assertDoesNotExist()
    }

    @Test
    fun givenAddingTheMoneyInTheGoalFailedWhenInTheSavingGoalScreenThenShowTheRoundUpAndTheButtonAndTheError() {
        var saveInGoal = ""
        var roundUpSaved = ""
        var error = ""
        composeTestRule.setContent {
            saveInGoal = stringResource(id = R.string.save_in_goal)
            roundUpSaved = stringResource(id = R.string.roundup, ROUND_UP, CURRENCY)
            error = stringResource(id = R.string.error)

            navController = setTestNavController()
            val accountState = AccountsStateUI.Idle
            val transactionsState = TransactionsStateUI.RoundUp(
                accountUid = ACCOUNT_UID,
                roundUp = ROUND_UP,
                currency = CURRENCY,
                roundUpMinorUnits = 12345,
                saveInGoalButtonEnabled = true
            )
            val savingGoalState = SavingGoalStateUI.Error
            MainScreen(
                navController = navController,
                accountState = accountState,
                transactionsState = transactionsState,
                savingGoalState = savingGoalState,
                onSaveInGoal = { _, _, _ -> },
                onNewTokens = { _, _, _ -> }
            )
            navController.setCurrentDestination(Destinations.SavingGoal.route)
        }
        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithText(roundUpSaved).isDisplayed() &&
                    composeTestRule.onAllNodesWithText(error).isDisplayed() &&
                    composeTestRule.onAllNodesWithText(saveInGoal).isDisplayed()
        }
        composeTestRule.onNodeWithText(saveInGoal).assertIsEnabled()

    }

    private companion object {
        const val ACCOUNT_TYPE_PRIMARY = "PRIMARY"
        const val ACCOUNT_UID = "ACCOUNT_UID"
        const val DEFAULT_CATEGORY = "DEFAULT_CATEGORY"
        const val SYMBOL = "SYMBOL"
        const val FRACTION_DIGITS = 2
        const val CURRENCY = "CURRENCY"
        const val ROUND_UP = "123.45"
        val APP_CURRENCY = AppCurrencyInfo(
            currency = CURRENCY,
            fractionDigits = FRACTION_DIGITS,
            symbol = SYMBOL
        )
        val ACCOUNT = Account(
            accountUid = ACCOUNT_UID,
            accountType = ACCOUNT_TYPE_PRIMARY,
            defaultCategory = DEFAULT_CATEGORY,
            currency = APP_CURRENCY
        )
    }

}
