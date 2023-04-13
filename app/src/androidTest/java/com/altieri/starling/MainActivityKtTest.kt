package com.altieri.starling

import android.content.Context
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import com.altieri.starling.ConnectedAndroidTestJsonFiles.ACCOUNT_SUCCEEDS
import com.altieri.starling.ConnectedAndroidTestJsonFiles.KEYS
import com.altieri.starling.ConnectedAndroidTestJsonFiles.TRANSACTIONS
import com.altieri.starling.account.presentation.AccountsViewModel
import com.altieri.starling.networking.framework.BuildConfig
import com.altieri.starling.networking.framework.enqueueFailure
import com.altieri.starling.networking.framework.enqueueSuccesses
import com.altieri.starling.transactions.presentation.TransactionsViewModel
import com.altieri.starling.ui.composable.TestTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainActivityKtTest {

    @get:Rule(order = 0)
    val hiltTestRule by lazy { HiltAndroidRule(this) }

    @get:Rule(order = 1)
    val composeTestRule by lazy { createAndroidComposeRule<MainActivityTest>() }

    private lateinit var navController: NavHostController

    private lateinit var context: Context

    private lateinit var appMockWebServer: MockWebServer

    @Before
    fun setUp() {
        hiltTestRule.inject()
        appMockWebServer = MockWebServer()
        appMockWebServer.start(BuildConfig.PORT)
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @After
    fun tearDown() {
        appMockWebServer.shutdown()
    }

    @Test
    fun given_the_call_to_fetch_the_account_succeeds_WHEN_showing_the_account_screen_THEN_download_transactions() {
        appMockWebServer.enqueueSuccesses(
            context = context,
            "/api/v2/accounts" to ACCOUNT_SUCCEEDS,
            "/api/v2/feed/account/$ACCOUNT_ID/category/$CATEGORY?changesSince=2023-04-01T10%3A10%3A10.123Z" to TRANSACTIONS,
            "/oauth/access-token" to KEYS
        )
        composeTestRule.setContent {
            val accountsViewModel: AccountsViewModel = hiltViewModel()
            val transactionsViewModel: TransactionsViewModel = hiltViewModel()
            navController = rememberNavController()
            MainContent(
                navController = navController,
                accountsViewModel = accountsViewModel,
                transactionsViewModel = transactionsViewModel
            )
        }
        composeTestRule.waitWithTimeout {
            composeTestRule.onAllNodesWithTag(TestTags.FullScreenLoadingAnimation).isDisplayed()
        }
        composeTestRule.waitWithTimeout {
            composeTestRule.onAllNodesWithText(
                text = "0",
                substring = true
            ).isDisplayed()
        }
    }

    @Test
    fun given_the_call_to_fetch_the_account_fails_WHEN_showing_the_account_screen_THEN_show_the_error() {
        appMockWebServer.enqueueFailure()
        var error = ""

        composeTestRule.setContent {
            val accountsViewModel: AccountsViewModel = hiltViewModel()
            val transactionsViewModel: TransactionsViewModel = hiltViewModel()
            error = stringResource(id = R.string.error)
            navController = rememberNavController()
            MainContent(
                navController = navController,
                accountsViewModel = accountsViewModel,
                transactionsViewModel = transactionsViewModel
            )
        }
        composeTestRule.waitWithTimeout {
            composeTestRule.onAllNodesWithTag(TestTags.FullScreenLoadingAnimation).isDisplayed()
        }
        composeTestRule.waitWithTimeout {
            composeTestRule.onAllNodesWithText(error).isDisplayed()
        }
    }

    private companion object {
        const val ACCOUNT_ID = "b4e32c69-214d-4d8d-a514-ad22932ace49"
        const val CATEGORY = "2ed05844-0181-45bd-963c-87c91e997746"
    }
}
