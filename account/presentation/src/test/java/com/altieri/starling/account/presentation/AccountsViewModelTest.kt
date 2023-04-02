package com.altieri.starling.account.presentation

import com.altieri.starling.account.bl.Account
import com.altieri.starling.account.bl.AddMoneyToSavingGoalUseCase
import com.altieri.starling.account.bl.OnNewTokensUseCase
import com.altieri.starling.account.bl.SetupSavingGoalUseCase
import com.altieri.starling.account.bl.UpdateAccountDetailsUseCase
import com.altieri.starling.common.bl.currency.AppCurrencyInfo
import com.altieri.starling.coretesting.AppTestCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AccountsViewModelTest {


    private val scheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(scheduler)

    @get:Rule
    val rule = AppTestCoroutineRule(testDispatcher)

    @MockK
    private lateinit var updateAccountDetailsUseCase: UpdateAccountDetailsUseCase

    @MockK
    private lateinit var setupSavingGoalUseCase: SetupSavingGoalUseCase

    @MockK
    private lateinit var addMoneyToSavingGoalUseCase: AddMoneyToSavingGoalUseCase

    @MockK
    private lateinit var onNewTokensUseCase: OnNewTokensUseCase

    private val accountState = MutableStateFlow<AccountsStateUI>(AccountsStateUI.Idle)
    private val savingGoalState = MutableStateFlow<SavingGoalStateUI>(SavingGoalStateUI.Idle)

    private lateinit var accountsViewModel: AccountsViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        scheduler.advanceUntilIdle()
        confirmVerified(
            updateAccountDetailsUseCase,
            setupSavingGoalUseCase,
            addMoneyToSavingGoalUseCase
        )
    }

    @Test
    fun `GIVEN the account is returned WHEN updateAccount THEN emit the account`() =
        runTest {
            // given
            accountsViewModel = AccountsViewModel(
                _accountState = accountState,
                _savingGoalState = savingGoalState,
                updateAccountDetailsUseCase = updateAccountDetailsUseCase,
                setupSavingGoalUseCase = setupSavingGoalUseCase,
                addMoneyToSavingGoalUseCase = addMoneyToSavingGoalUseCase,
                onNewTokensUseCase = onNewTokensUseCase,
                dispatcher = testDispatcher
            )
            val emitted = mutableListOf<AccountsStateUI>()
            accountsViewModel.accountState.onEach {
                emitted.add(it)
            }.launchIn(CoroutineScope(testDispatcher))

            coEvery { updateAccountDetailsUseCase() } answers {
                scheduler.runCurrent()
                ACCOUNT
            }

            // when
            accountsViewModel.updateAccount()
            scheduler.runCurrent()

            // then
            assertEquals(
                listOf(
                    AccountsStateUI.Idle,
                    AccountsStateUI.Loading,
                    AccountsStateUI.PrimaryAccounts(
                        ACCOUNT
                    )
                ), emitted
            )
            coVerify { updateAccountDetailsUseCase() }
        }

    class TestException : Exception()

    @Test
    fun `GIVEN the update of the account fails WHEN updateAccount THEN emit the error`() =
        runTest {
            // given
            accountsViewModel = AccountsViewModel(
                _accountState = accountState,
                _savingGoalState = savingGoalState,
                updateAccountDetailsUseCase = updateAccountDetailsUseCase,
                setupSavingGoalUseCase = setupSavingGoalUseCase,
                addMoneyToSavingGoalUseCase = addMoneyToSavingGoalUseCase,
                onNewTokensUseCase = onNewTokensUseCase,
                dispatcher = testDispatcher
            )
            val emitted = mutableListOf<AccountsStateUI>()
            accountsViewModel.accountState.onEach {
                emitted.add(it)
            }.launchIn(CoroutineScope(testDispatcher))

            coEvery { updateAccountDetailsUseCase() } answers {
                scheduler.runCurrent()
                throw TestException()
            }

            // when
            accountsViewModel.updateAccount()
            scheduler.runCurrent()

            // then
            assertEquals(
                listOf(
                    AccountsStateUI.Idle,
                    AccountsStateUI.Loading,
                    AccountsStateUI.Error
                ), emitted
            )
            coVerify { updateAccountDetailsUseCase() }
        }

    @Test
    fun `GIVEN the operation is already running WHEN updateAccount THEN skip it`() =
        runTest {
            // given
            accountsViewModel = AccountsViewModel(
                _accountState = MutableStateFlow(AccountsStateUI.Loading),
                _savingGoalState = savingGoalState,
                updateAccountDetailsUseCase = updateAccountDetailsUseCase,
                setupSavingGoalUseCase = setupSavingGoalUseCase,
                addMoneyToSavingGoalUseCase = addMoneyToSavingGoalUseCase,
                onNewTokensUseCase = onNewTokensUseCase,
                dispatcher = testDispatcher
            )

            // when
            accountsViewModel.updateAccount()
        }

    @Test
    fun `GIVEN the calls succeed WHEN addMoneyToSavingGoal THEN emit the success`() =
        runTest {
            // given

            accountsViewModel = AccountsViewModel(
                _accountState = accountState,
                _savingGoalState = savingGoalState,
                updateAccountDetailsUseCase = updateAccountDetailsUseCase,
                setupSavingGoalUseCase = setupSavingGoalUseCase,
                addMoneyToSavingGoalUseCase = addMoneyToSavingGoalUseCase,
                onNewTokensUseCase = onNewTokensUseCase,
                dispatcher = testDispatcher
            )
            val emitted = mutableListOf<SavingGoalStateUI>()
            accountsViewModel.savingGoalState.onEach {
                emitted.add(it)
            }.launchIn(CoroutineScope(testDispatcher))

            coEvery {
                setupSavingGoalUseCase(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    name = SAVING_GOAL_NAME
                )
            } answers {
                scheduler.runCurrent()
                SAVINGS_GOAL_UID
            }

            coEvery {
                addMoneyToSavingGoalUseCase(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    savingsGoalUid = SAVINGS_GOAL_UID,
                    minorUnits = MINOR_UNITS
                )
            } answers {
                scheduler.runCurrent()
            }

            // when
            accountsViewModel.addMoneyToSavingGoal(
                accountUid = ACCOUNT_UID,
                currency = CURRENCY,
                minorUnits = MINOR_UNITS
            )
            scheduler.runCurrent()

            // then
            assertEquals(
                listOf(
                    SavingGoalStateUI.Idle,
                    SavingGoalStateUI.Running,
                    SavingGoalStateUI.Success
                ), emitted
            )
            coVerify {
                setupSavingGoalUseCase(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    name = SAVING_GOAL_NAME
                )
            }
            coVerify {
                addMoneyToSavingGoalUseCase(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    savingsGoalUid = SAVINGS_GOAL_UID,
                    minorUnits = MINOR_UNITS
                )
            }
        }

    @Test
    fun `GIVEN the call to add money fails WHEN addMoneyToSavingGoal THEN emit the error`() =
        runTest {
            // given
            accountsViewModel = AccountsViewModel(
                _accountState = accountState,
                _savingGoalState = savingGoalState,
                updateAccountDetailsUseCase = updateAccountDetailsUseCase,
                setupSavingGoalUseCase = setupSavingGoalUseCase,
                addMoneyToSavingGoalUseCase = addMoneyToSavingGoalUseCase,
                onNewTokensUseCase = onNewTokensUseCase,
                dispatcher = testDispatcher
            )
            val emitted = mutableListOf<SavingGoalStateUI>()
            accountsViewModel.savingGoalState.onEach {
                emitted.add(it)
            }.launchIn(CoroutineScope(testDispatcher))

            coEvery {
                setupSavingGoalUseCase(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    name = SAVING_GOAL_NAME
                )
            } answers {
                scheduler.runCurrent()
                SAVINGS_GOAL_UID
            }

            coEvery {
                addMoneyToSavingGoalUseCase(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    savingsGoalUid = SAVINGS_GOAL_UID,
                    minorUnits = MINOR_UNITS
                )
            } answers {
                scheduler.runCurrent()
                throw TestException()
            }

            // when
            accountsViewModel.addMoneyToSavingGoal(
                accountUid = ACCOUNT_UID,
                currency = CURRENCY,
                minorUnits = MINOR_UNITS
            )
            scheduler.runCurrent()

            // then
            assertEquals(
                listOf(
                    SavingGoalStateUI.Idle,
                    SavingGoalStateUI.Running,
                    SavingGoalStateUI.Error
                ), emitted
            )
            coVerify {
                setupSavingGoalUseCase(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    name = SAVING_GOAL_NAME
                )
            }
            coVerify {
                addMoneyToSavingGoalUseCase(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    savingsGoalUid = SAVINGS_GOAL_UID,
                    minorUnits = MINOR_UNITS
                )
            }
        }

    @Test
    fun `GIVEN the call to set up the saving goal fails WHEN addMoneyToSavingGoal THEN emit the error`() =
        runTest {
            // given
            accountsViewModel = AccountsViewModel(
                _accountState = accountState,
                _savingGoalState = savingGoalState,
                updateAccountDetailsUseCase = updateAccountDetailsUseCase,
                setupSavingGoalUseCase = setupSavingGoalUseCase,
                addMoneyToSavingGoalUseCase = addMoneyToSavingGoalUseCase,
                onNewTokensUseCase = onNewTokensUseCase,
                dispatcher = testDispatcher
            )
            val emitted = mutableListOf<SavingGoalStateUI>()
            accountsViewModel.savingGoalState.onEach {
                emitted.add(it)
            }.launchIn(CoroutineScope(testDispatcher))

            coEvery {
                setupSavingGoalUseCase(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    name = SAVING_GOAL_NAME
                )
            } answers {
                scheduler.runCurrent()
                throw TestException()
            }

            // when
            accountsViewModel.addMoneyToSavingGoal(
                accountUid = ACCOUNT_UID,
                currency = CURRENCY,
                minorUnits = MINOR_UNITS
            )
            scheduler.runCurrent()

            // then
            assertEquals(
                listOf(
                    SavingGoalStateUI.Idle,
                    SavingGoalStateUI.Running,
                    SavingGoalStateUI.Error
                ), emitted
            )
            coVerify {
                setupSavingGoalUseCase(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    name = SAVING_GOAL_NAME
                )
            }
        }

    @Test
    fun `GIVEN the operation is already running WHEN addMoneyToSavingGoal THEN skip it`() =
        runTest {
            // given
            accountsViewModel = AccountsViewModel(
                _accountState = accountState,
                _savingGoalState = MutableStateFlow(SavingGoalStateUI.Running),
                updateAccountDetailsUseCase = updateAccountDetailsUseCase,
                setupSavingGoalUseCase = setupSavingGoalUseCase,
                addMoneyToSavingGoalUseCase = addMoneyToSavingGoalUseCase,
                onNewTokensUseCase = onNewTokensUseCase,
                dispatcher = testDispatcher
            )

            // when
            accountsViewModel.addMoneyToSavingGoal(
                accountUid = ACCOUNT_UID,
                currency = CURRENCY,
                minorUnits = MINOR_UNITS
            )
        }

    @Test
    fun `WHEN onNewTokens THEN call the use case`() =
        runTest {
            // given
            accountsViewModel = AccountsViewModel(
                _accountState = accountState,
                _savingGoalState = savingGoalState,
                updateAccountDetailsUseCase = updateAccountDetailsUseCase,
                setupSavingGoalUseCase = setupSavingGoalUseCase,
                addMoneyToSavingGoalUseCase = addMoneyToSavingGoalUseCase,
                onNewTokensUseCase = onNewTokensUseCase,
                dispatcher = testDispatcher
            )
            coEvery {
                onNewTokensUseCase(
                    accessToken = ACCESS_TOKEN,
                    refreshToken = REFRESH_TOKEN,
                    expiresIn = EXPIRES_IN
                )
            } answers {}

            // when
            accountsViewModel.onNewTokens(
                accessToken = ACCESS_TOKEN,
                refreshToken = REFRESH_TOKEN,
                expiresIn = EXPIRES_IN
            )

            scheduler.advanceUntilIdle()

            // then
            coVerify {
                onNewTokensUseCase(
                    accessToken = ACCESS_TOKEN,
                    refreshToken = REFRESH_TOKEN,
                    expiresIn = EXPIRES_IN
                )
            }
        }

    private companion object {
        const val ACCOUNT_TYPE_PRIMARY = "PRIMARY"
        const val ACCOUNT_UID = "ACCOUNT_UID"
        const val DEFAULT_CATEGORY = "DEFAULT_CATEGORY"
        const val CURRENCY = "CURRENCY"
        const val SYMBOL = "SYMBOL"
        const val FRACTION_DIGITS = 2
        const val SAVING_GOAL_NAME = "SAVING_GOAL_NAME"
        const val SAVINGS_GOAL_UID = "SAVINGS_GOAL_UID"
        const val MINOR_UNITS = 123
        const val EXPIRES_IN = 123
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"

        val ACCOUNT = Account(
            accountUid = ACCOUNT_UID,
            accountType = ACCOUNT_TYPE_PRIMARY,
            defaultCategory = DEFAULT_CATEGORY,
            currency = AppCurrencyInfo(
                currency = CURRENCY, fractionDigits = FRACTION_DIGITS, symbol = SYMBOL
            )
        )
    }
}
