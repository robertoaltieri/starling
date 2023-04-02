package com.altieri.starling.account.framework

import com.altieri.starling.account.data.AccountData
import com.altieri.starling.account.data.SavingGoalData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AccountsNetworkDataSourceImplTest {

    @MockK
    private lateinit var apiService: AccountsApiService

    @MockK
    private lateinit var mapAccount: AccountRawToAccountDataMapper

    @MockK
    private lateinit var mapSavingGoal: SavingGoalRawToSavingGoalDataMapper

    private lateinit var accountsNetworkDataSource: AccountsNetworkDataSourceImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        accountsNetworkDataSource = AccountsNetworkDataSourceImpl(
            apiService = apiService,
            mapAccount = mapAccount,
            mapSavingGoal = mapSavingGoal
        )
    }

    @After
    fun tearDown() {
        confirmVerified(
            apiService,
            mapAccount,
            mapSavingGoal
        )
    }

    class TestException : Exception()

    @Test
    fun `GIVEN the service API returns a list of account WHEN allAccounts THEN return the list of the mapped account`() =
        runTest {
            // given
            coEvery { apiService.accounts() } returns ACCOUNTS_RAW
            every { mapAccount(ACCOUNT_RAW) } returns ACCOUNT_DATA

            // when
            val accounts = accountsNetworkDataSource.allAccounts()

            // then
            coVerify { apiService.accounts() }
            verify { mapAccount(ACCOUNT_RAW) }
            assertEquals(listOf(ACCOUNT_DATA), accounts)
        }


    @Test
    fun `GIVEN the service API returns an empty list of account WHEN allAccounts THEN return an empty list`() =
        runTest {
            // given
            coEvery { apiService.accounts() } returns ACCOUNTS_RAW_EMPTY

            // when
            val accounts = accountsNetworkDataSource.allAccounts()

            // then
            coVerify { apiService.accounts() }
            assertTrue(accounts.isEmpty())
        }

    @Test
    fun `GIVEN the service API fails WHEN allAccounts THEN do not catch the exception`() = runTest {
        // given
        coEvery { apiService.accounts() } throws TestException()

        // then
        assertFailsWith<TestException> { accountsNetworkDataSource.allAccounts() }
        coVerify { apiService.accounts() }
    }

    @Test
    fun `GIVEN the service API returns a list of goals WHEN savingGoals THEN return the list of the mapped goals`() =
        runTest {
            // given
            coEvery { apiService.savingGoals(ACCOUNT_UID) } returns SAVING_GOALS_RAW
            every { mapSavingGoal(SAVING_GOAL_RAW) } returns SAVING_GOAL_DATA

            // then
            val list = accountsNetworkDataSource.savingGoals(ACCOUNT_UID)

            coVerify { apiService.savingGoals(ACCOUNT_UID) }
            verify { mapSavingGoal(SAVING_GOAL_RAW) }
            assertEquals(listOf(SAVING_GOAL_DATA), list)
        }

    @Test
    fun `GIVEN the service API returns an empty list of goals WHEN savingGoals THEN return an empty list`() =
        runTest {
            // given
            coEvery { apiService.savingGoals(ACCOUNT_UID) } returns SAVING_GOALS_RAW_EMPTY

            // then
            val list = accountsNetworkDataSource.savingGoals(ACCOUNT_UID)

            coVerify { apiService.savingGoals(ACCOUNT_UID) }
            assertTrue(list.isEmpty())
        }

    @Test
    fun `GIVEN the service API fails WHEN savingGoals THEN do not catch the exception`() = runTest {
        // given
        coEvery { apiService.savingGoals(ACCOUNT_UID) } throws TestException()

        // then
        assertFailsWith<TestException> { accountsNetworkDataSource.savingGoals(ACCOUNT_UID) }
        coVerify { apiService.savingGoals(ACCOUNT_UID) }
    }

    @Test
    fun `GIVEN the service API succeeds WHEN addMoneyToGoal THEN return the uid`() =
        runTest {
            // given
            coEvery {
                apiService.addMoneyToGoal(
                    accountUid = ACCOUNT_UID,
                    savingsGoalUid = SAVINGS_GOAL_UID,
                    transferUid = TRANSFER_UID,
                    addMoneyToGoalRequest = ADD_MONEY_TO_GOAL_REQUEST
                )
            } returns ADD_MONEY_TO_GOAL_RESPONSE

            // when
            val uid = accountsNetworkDataSource.addMoneyToGoal(
                accountUid = ACCOUNT_UID,
                currency = CURRENCY,
                savingsGoalUid = SAVINGS_GOAL_UID,
                transferUid = TRANSFER_UID,
                minorUnits = MINOR_UNITS
            )

            // then
            coVerify {
                apiService.addMoneyToGoal(
                    accountUid = ACCOUNT_UID,
                    savingsGoalUid = SAVINGS_GOAL_UID,
                    transferUid = TRANSFER_UID,
                    addMoneyToGoalRequest = ADD_MONEY_TO_GOAL_REQUEST
                )
            }
            assertEquals(TRANSFER_UID, uid)
        }

    @Test
    fun `GIVEN the service API fails WHEN addMoneyToGoal THEN do not catch the exception`() =
        runTest {
            // given
            coEvery {
                apiService.addMoneyToGoal(
                    accountUid = ACCOUNT_UID,
                    savingsGoalUid = SAVINGS_GOAL_UID,
                    transferUid = TRANSFER_UID,
                    addMoneyToGoalRequest = ADD_MONEY_TO_GOAL_REQUEST
                )
            } throws TestException()

            // then
            assertFailsWith<TestException> {
                accountsNetworkDataSource.addMoneyToGoal(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    savingsGoalUid = SAVINGS_GOAL_UID,
                    transferUid = TRANSFER_UID,
                    minorUnits = MINOR_UNITS
                )
            }
            coVerify {
                apiService.addMoneyToGoal(
                    accountUid = ACCOUNT_UID,
                    savingsGoalUid = SAVINGS_GOAL_UID,
                    transferUid = TRANSFER_UID,
                    addMoneyToGoalRequest = ADD_MONEY_TO_GOAL_REQUEST
                )
            }
        }

    @Test
    fun `GIVEN the service API succeeds WHEN createSavingGoal THEN do not catch the exception`() =
        runTest {
            // given
            coEvery {
                apiService.createSavingGoal(
                    accountUid = ACCOUNT_UID,
                    createSavingGoalRequest = CREATE_SAVING_GOAL_REQUEST
                )
            } returns SAVING_GOAL_RAW

            // when
            val uid = accountsNetworkDataSource.createSavingGoal(
                accountUid = ACCOUNT_UID,
                currency = CURRENCY,
                name = NAME
            )

            // then
            coVerify {
                apiService.createSavingGoal(
                    accountUid = ACCOUNT_UID,
                    createSavingGoalRequest = CREATE_SAVING_GOAL_REQUEST
                )
            }
            assertEquals(SAVINGS_GOAL_UID, uid)
        }

    @Test
    fun `GIVEN the service API returns a null savingsGoalUid WHEN createSavingGoal THEN throw NullPointerException`() =
        runTest {
            // given
            coEvery {
                apiService.createSavingGoal(
                    accountUid = ACCOUNT_UID,
                    createSavingGoalRequest = CREATE_SAVING_GOAL_REQUEST
                )
            } returns SAVING_GOAL_RAW.copy(savingsGoalUid = null)

            // when
            assertFailsWith<NullPointerException> {
                accountsNetworkDataSource.createSavingGoal(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    name = NAME
                )
            }

            // then
            coVerify {
                apiService.createSavingGoal(
                    accountUid = ACCOUNT_UID,
                    createSavingGoalRequest = CREATE_SAVING_GOAL_REQUEST
                )
            }
        }

    @Test
    fun `GIVEN the service API fails WHEN createSavingGoal THEN do not catch the exception`() =
        runTest {
            // given
            coEvery {
                apiService.createSavingGoal(
                    accountUid = ACCOUNT_UID,
                    createSavingGoalRequest = CREATE_SAVING_GOAL_REQUEST
                )
            } throws TestException()

            // then
            assertFailsWith<TestException> {
                accountsNetworkDataSource.createSavingGoal(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    name = NAME
                )
            }
            coVerify {
                apiService.createSavingGoal(
                    accountUid = ACCOUNT_UID,
                    createSavingGoalRequest = CREATE_SAVING_GOAL_REQUEST
                )
            }
        }

    private companion object {
        const val ACCOUNT_TYPE_PRIMARY = "PRIMARY"
        const val ACCOUNT_UID = "ACCOUNT_UID"
        const val DEFAULT_CATEGORY = "DEFAULT_CATEGORY"
        const val CURRENCY = "CURRENCY"
        const val SAVINGS_GOAL_UID = "SAVINGS_GOAL_UID"
        const val TRANSFER_UID = "TRANSFER_UID"
        const val NAME = "NAME"
        const val MINOR_UNITS = 123
        val ACCOUNT_DATA = AccountData(
            accountUid = ACCOUNT_UID,
            accountType = ACCOUNT_TYPE_PRIMARY,
            defaultCategory = DEFAULT_CATEGORY,
            currency = CURRENCY
        )
        val ACCOUNT_RAW = AccountRaw(
            accountUid = ACCOUNT_UID,
            accountType = ACCOUNT_TYPE_PRIMARY,
            defaultCategory = DEFAULT_CATEGORY,
            currency = CURRENCY
        )
        val ACCOUNTS_RAW = AccountsRaw(
            accounts = listOf(ACCOUNT_RAW)
        )
        val ACCOUNTS_RAW_EMPTY = AccountsRaw(
            accounts = emptyList()
        )
        val SAVING_GOAL_DATA = SavingGoalData(
            savingsGoalUid = SAVINGS_GOAL_UID,
            name = NAME
        )
        val SAVING_GOAL_RAW = SavingGoalRaw(
            savingsGoalUid = SAVINGS_GOAL_UID,
            name = NAME
        )
        val SAVING_GOALS_RAW = SavingGoalsRaw(
            savingsGoalList = listOf(SAVING_GOAL_RAW),
        )
        val SAVING_GOALS_RAW_EMPTY = SavingGoalsRaw(
            savingsGoalList = emptyList(),
        )
        val ADD_MONEY_TO_GOAL_REQUEST = AddMoneyToGoalRequest(
            amount = CurrencyAndAmountRaw(
                currency = CURRENCY,
                minorUnits = MINOR_UNITS
            )
        )
        val ADD_MONEY_TO_GOAL_RESPONSE = AddMoneyToGoalResponse(
            transferUid = TRANSFER_UID
        )
        val CREATE_SAVING_GOAL_REQUEST = CreateSavingGoalRequest(
            currency = CURRENCY,
            name = NAME
        )
    }
}
