package com.altieri.starling.account.data

import com.altieri.starling.account.bl.Account
import com.altieri.starling.account.bl.SavingGoal
import com.altieri.starling.common.bl.AppUUIDGenerator
import com.altieri.starling.common.bl.currency.AppCurrencyInfo
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
import java.lang.IllegalStateException
import kotlin.Exception
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class AccountRepositoryImplTest {

    @MockK
    private lateinit var accountsNetworkDataSource: AccountsNetworkDataSource

    @MockK
    private lateinit var accountsLocalDataSource: AccountsLocalDataSource

    @MockK
    private lateinit var validateAccount: AccountValidator

    @MockK
    private lateinit var validateSavingGoal: SavingGoalValidator

    @MockK
    private lateinit var mapAccount: AccountDataToAccountMapper

    @MockK
    private lateinit var mapSavingGoal: SavingGoalDataToSavingGoalMapper

    @MockK
    private lateinit var uuidGenerator: AppUUIDGenerator

    private lateinit var accountRepository: AccountRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

    }

    @After
    fun tearDown() {
        confirmVerified(
            accountsNetworkDataSource,
            accountsLocalDataSource,
            validateAccount,
            validateSavingGoal,
            mapAccount,
            mapSavingGoal,
            uuidGenerator
        )
    }

    @Test
    fun `GIVEN the repository has a cached value WHEN primaryAccount is called THEN return the cached account`() =
        runTest {
            // given
            accountRepository = AccountRepositoryImpl(
                accountsNetworkDataSource = accountsNetworkDataSource,
                accountsLocalDataSource = accountsLocalDataSource,
                validateAccount = validateAccount,
                validateSavingGoal = validateSavingGoal,
                mapAccount = mapAccount,
                mapSavingGoal = mapSavingGoal,
                uuidGenerator = uuidGenerator,
                cachedAccount = ACCOUNT
            )

            // when
            val account = accountRepository.primaryAccount()

            // then
            assertEquals(ACCOUNT, account)
        }


    @Test
    fun `GIVEN the repository does not have a cached value and the account is in the DB WHEN primaryAccount is called THEN return the mapped account from the DB`() =
        runTest {
            // given
            accountRepository = AccountRepositoryImpl(
                accountsNetworkDataSource = accountsNetworkDataSource,
                accountsLocalDataSource = accountsLocalDataSource,
                validateAccount = validateAccount,
                validateSavingGoal = validateSavingGoal,
                mapAccount = mapAccount,
                mapSavingGoal = mapSavingGoal,
                uuidGenerator = uuidGenerator,
                cachedAccount = null
            )

            coEvery { accountsLocalDataSource.primaryAccount() } returns ACCOUNT_DATA
            coEvery { mapAccount(ACCOUNT_DATA) } returns ACCOUNT

            // when
            val account = accountRepository.primaryAccount()

            // then
            coVerify { accountsLocalDataSource.primaryAccount() }
            coVerify { mapAccount(ACCOUNT_DATA) }

            assertEquals(ACCOUNT, account)

        }

    @Test
    fun `GIVEN no account in the cache or DB and the network returns a valid account WHEN primaryAccount is called THEN save and return the mapped account from the network`() =
        runTest {
            // given
            accountRepository = AccountRepositoryImpl(
                accountsNetworkDataSource = accountsNetworkDataSource,
                accountsLocalDataSource = accountsLocalDataSource,
                validateAccount = validateAccount,
                validateSavingGoal = validateSavingGoal,
                mapAccount = mapAccount,
                mapSavingGoal = mapSavingGoal,
                uuidGenerator = uuidGenerator,
                cachedAccount = null
            )

            coEvery { accountsLocalDataSource.primaryAccount() } returns null
            coEvery { accountsLocalDataSource.save(listOf(ACCOUNT)) } answers {}
            coEvery { accountsNetworkDataSource.allAccounts() } returns listOf(ACCOUNT_DATA)
            coEvery { mapAccount(ACCOUNT_DATA) } returns ACCOUNT
            coEvery { validateAccount(ACCOUNT_DATA) } returns true

            // when
            val account = accountRepository.primaryAccount()

            // then
            coVerify { accountsLocalDataSource.primaryAccount() }
            coVerify { mapAccount(ACCOUNT_DATA) }
            coVerify { accountsLocalDataSource.save(listOf(ACCOUNT)) }
            coVerify { accountsNetworkDataSource.allAccounts() }
            coVerify { validateAccount(ACCOUNT_DATA) }

            assertEquals(ACCOUNT, account)

        }

    @Test
    fun `GIVEN no account in the cache or DB and the network returns a valid non primary account WHEN primaryAccount is called THEN save the account and return null`() =
        runTest {
            // given
            accountRepository = AccountRepositoryImpl(
                accountsNetworkDataSource = accountsNetworkDataSource,
                accountsLocalDataSource = accountsLocalDataSource,
                validateAccount = validateAccount,
                validateSavingGoal = validateSavingGoal,
                mapAccount = mapAccount,
                mapSavingGoal = mapSavingGoal,
                uuidGenerator = uuidGenerator,
                cachedAccount = null
            )

            coEvery { accountsLocalDataSource.primaryAccount() } returns null
            coEvery { accountsLocalDataSource.save(listOf(NON_PRIMARY_ACCOUNT)) } answers {}
            coEvery { accountsNetworkDataSource.allAccounts() } returns listOf(ACCOUNT_DATA)
            coEvery { mapAccount(ACCOUNT_DATA) } returns NON_PRIMARY_ACCOUNT
            coEvery { validateAccount(ACCOUNT_DATA) } returns true

            // when
            val account = accountRepository.primaryAccount()

            // then
            coVerify { accountsLocalDataSource.primaryAccount() }
            coVerify { mapAccount(ACCOUNT_DATA) }
            coVerify { accountsLocalDataSource.save(listOf(NON_PRIMARY_ACCOUNT)) }
            coVerify { accountsNetworkDataSource.allAccounts() }
            coVerify { validateAccount(ACCOUNT_DATA) }

            assertNull(account)

        }

    @Test
    fun `GIVEN no account in the cache or DB and the network returns a non valid account WHEN primaryAccount is called THEN return null`() =
        runTest {
            // given
            accountRepository = AccountRepositoryImpl(
                accountsNetworkDataSource = accountsNetworkDataSource,
                accountsLocalDataSource = accountsLocalDataSource,
                validateAccount = validateAccount,
                validateSavingGoal = validateSavingGoal,
                mapAccount = mapAccount,
                mapSavingGoal = mapSavingGoal,
                uuidGenerator = uuidGenerator,
                cachedAccount = null
            )

            coEvery { accountsLocalDataSource.primaryAccount() } returns null
            coEvery { accountsNetworkDataSource.allAccounts() } returns listOf(ACCOUNT_DATA)
            coEvery { validateAccount(ACCOUNT_DATA) } returns false

            // when
            val account = accountRepository.primaryAccount()

            // then
            coVerify { accountsLocalDataSource.primaryAccount() }
            coVerify { accountsNetworkDataSource.allAccounts() }
            coVerify { validateAccount(ACCOUNT_DATA) }

            assertNull(account)

        }

    private class TestException : Exception()

    @Test
    fun `GIVEN no account in the cache or DB and the network call fails WHEN primaryAccount is called THEN do not catch the Exception`() =
        runTest {
            // given
            accountRepository = AccountRepositoryImpl(
                accountsNetworkDataSource = accountsNetworkDataSource,
                accountsLocalDataSource = accountsLocalDataSource,
                validateAccount = validateAccount,
                validateSavingGoal = validateSavingGoal,
                mapAccount = mapAccount,
                mapSavingGoal = mapSavingGoal,
                uuidGenerator = uuidGenerator,
                cachedAccount = null
            )

            coEvery { accountsLocalDataSource.primaryAccount() } returns null
            coEvery { accountsNetworkDataSource.allAccounts() } throws TestException()

            // then
            assertFailsWith<TestException> { accountRepository.primaryAccount() }
            coVerify { accountsLocalDataSource.primaryAccount() }
            coVerify { accountsNetworkDataSource.allAccounts() }

        }

    @Test
    fun `GIVEN the API returns a list of goals WHEN savingGoals THEN map and return the list of the valid goals`() =
        runTest {
            // given
            accountRepository = AccountRepositoryImpl(
                accountsNetworkDataSource = accountsNetworkDataSource,
                accountsLocalDataSource = accountsLocalDataSource,
                validateAccount = validateAccount,
                validateSavingGoal = validateSavingGoal,
                mapAccount = mapAccount,
                mapSavingGoal = mapSavingGoal,
                uuidGenerator = uuidGenerator,
                cachedAccount = null
            )

            coEvery { accountsNetworkDataSource.savingGoals(ACCOUNT_UID) } returns listOf(
                SAVING_GOAL_DATA, INVALID_SAVING_GOAL_DATA
            )
            coEvery { validateSavingGoal(SAVING_GOAL_DATA) } returns true
            coEvery { validateSavingGoal(INVALID_SAVING_GOAL_DATA) } returns false
            coEvery { mapSavingGoal(SAVING_GOAL_DATA) } returns SAVING_GOAL

            // when
            val savingGoals = accountRepository.savingGoals(ACCOUNT_UID)

            // then
            coVerify { accountsNetworkDataSource.savingGoals(ACCOUNT_UID) }
            coVerify { validateSavingGoal(SAVING_GOAL_DATA) }
            coVerify { validateSavingGoal(INVALID_SAVING_GOAL_DATA) }
            coVerify { mapSavingGoal(SAVING_GOAL_DATA) }

            assertEquals(listOf(SAVING_GOAL), savingGoals)

        }

    @Test
    fun `GIVEN the API call fails WHEN savingGoals THEN do not catch the Exception`() =
        runTest {
            // given
            accountRepository = AccountRepositoryImpl(
                accountsNetworkDataSource = accountsNetworkDataSource,
                accountsLocalDataSource = accountsLocalDataSource,
                validateAccount = validateAccount,
                validateSavingGoal = validateSavingGoal,
                mapAccount = mapAccount,
                mapSavingGoal = mapSavingGoal,
                uuidGenerator = uuidGenerator,
                cachedAccount = null
            )

            coEvery { accountsNetworkDataSource.savingGoals(ACCOUNT_UID) } throws TestException()

            // then
            assertFailsWith<TestException> { accountRepository.savingGoals(ACCOUNT_UID) }
            coVerify { accountsNetworkDataSource.savingGoals(ACCOUNT_UID) }
        }

    @Test
    fun `GIVEN the call to create the goal succeeds WHEN createSavingGoal THEN return the uid`() =
        runTest {
            // given
            accountRepository = AccountRepositoryImpl(
                accountsNetworkDataSource = accountsNetworkDataSource,
                accountsLocalDataSource = accountsLocalDataSource,
                validateAccount = validateAccount,
                validateSavingGoal = validateSavingGoal,
                mapAccount = mapAccount,
                mapSavingGoal = mapSavingGoal,
                uuidGenerator = uuidGenerator,
                cachedAccount = null
            )
            coEvery {
                accountsNetworkDataSource.createSavingGoal(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    name = NAME
                )
            } returns RANDOM_UUID

            // when
            val uid = accountRepository.createSavingGoal(
                accountUid = ACCOUNT_UID,
                currency = CURRENCY,
                name = NAME
            )

            // then
            coVerify {
                accountsNetworkDataSource.createSavingGoal(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    name = NAME
                )
            }
            assertEquals(RANDOM_UUID, uid)

        }

    @Test
    fun `GIVEN the call to create a goal fails WHEN createSavingGoal THEN do not catch the exception`() =
        runTest {
            // given
            accountRepository = AccountRepositoryImpl(
                accountsNetworkDataSource = accountsNetworkDataSource,
                accountsLocalDataSource = accountsLocalDataSource,
                validateAccount = validateAccount,
                validateSavingGoal = validateSavingGoal,
                mapAccount = mapAccount,
                mapSavingGoal = mapSavingGoal,
                uuidGenerator = uuidGenerator,
                cachedAccount = null
            )
            coEvery {
                accountsNetworkDataSource.createSavingGoal(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    name = NAME
                )
            } throws TestException()

            // then
            assertFailsWith<TestException> {
                accountRepository.createSavingGoal(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    name = NAME
                )
            }
            coVerify {
                accountsNetworkDataSource.createSavingGoal(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    name = NAME
                )
            }
        }

    @Test
    fun `GIVEN the call to add money succeeds WHEN addMoneyToGoal THEN do nothing`() =
        runTest {
            // given
            accountRepository = AccountRepositoryImpl(
                accountsNetworkDataSource = accountsNetworkDataSource,
                accountsLocalDataSource = accountsLocalDataSource,
                validateAccount = validateAccount,
                validateSavingGoal = validateSavingGoal,
                mapAccount = mapAccount,
                mapSavingGoal = mapSavingGoal,
                uuidGenerator = uuidGenerator,
                cachedAccount = null
            )
            every { uuidGenerator.randomUUID() } returns RANDOM_UUID
            coEvery {
                accountsNetworkDataSource.addMoneyToGoal(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    savingsGoalUid = SAVINGS_GOAL_UID,
                    transferUid = RANDOM_UUID,
                    minorUnits = MINOR_UNIT
                )
            } returns RANDOM_UUID

            // when
            accountRepository.addMoneyToGoal(
                accountUid = ACCOUNT_UID,
                currency = CURRENCY,
                savingsGoalUid = SAVINGS_GOAL_UID,
                minorUnits = MINOR_UNIT
            )

            // then
            verify { uuidGenerator.randomUUID() }
            coVerify {
                accountsNetworkDataSource.addMoneyToGoal(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    savingsGoalUid = SAVINGS_GOAL_UID,
                    transferUid = RANDOM_UUID,
                    minorUnits = MINOR_UNIT
                )
            }

        }

    @Test
    fun `GIVEN the call returns a wrong UID WHEN addMoneyToGoal THEN throw an exception`() =
        runTest {
            // given
            accountRepository = AccountRepositoryImpl(
                accountsNetworkDataSource = accountsNetworkDataSource,
                accountsLocalDataSource = accountsLocalDataSource,
                validateAccount = validateAccount,
                validateSavingGoal = validateSavingGoal,
                mapAccount = mapAccount,
                mapSavingGoal = mapSavingGoal,
                uuidGenerator = uuidGenerator,
                cachedAccount = null
            )
            every { uuidGenerator.randomUUID() } returns RANDOM_UUID
            coEvery {
                accountsNetworkDataSource.addMoneyToGoal(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    savingsGoalUid = SAVINGS_GOAL_UID,
                    transferUid = RANDOM_UUID,
                    minorUnits = MINOR_UNIT
                )
            } returns WRONG_UUID

            // then
            assertFailsWith<IllegalStateException> {
                accountRepository.addMoneyToGoal(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    savingsGoalUid = SAVINGS_GOAL_UID,
                    minorUnits = MINOR_UNIT
                )
            }
            verify { uuidGenerator.randomUUID() }
            coVerify {
                accountsNetworkDataSource.addMoneyToGoal(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    savingsGoalUid = SAVINGS_GOAL_UID,
                    transferUid = RANDOM_UUID,
                    minorUnits = MINOR_UNIT
                )
            }
        }

    @Test
    fun `GIVEN the call fails WHEN addMoneyToGoal THEN do not catch the exception`() =
        runTest {
            // given
            accountRepository = AccountRepositoryImpl(
                accountsNetworkDataSource = accountsNetworkDataSource,
                accountsLocalDataSource = accountsLocalDataSource,
                validateAccount = validateAccount,
                validateSavingGoal = validateSavingGoal,
                mapAccount = mapAccount,
                mapSavingGoal = mapSavingGoal,
                uuidGenerator = uuidGenerator,
                cachedAccount = null
            )
            every { uuidGenerator.randomUUID() } returns RANDOM_UUID
            coEvery {
                accountsNetworkDataSource.addMoneyToGoal(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    savingsGoalUid = SAVINGS_GOAL_UID,
                    transferUid = RANDOM_UUID,
                    minorUnits = MINOR_UNIT
                )
            } throws TestException()

            // then
            assertFailsWith<TestException> {
                accountRepository.addMoneyToGoal(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    savingsGoalUid = SAVINGS_GOAL_UID,
                    minorUnits = MINOR_UNIT
                )
            }

            verify { uuidGenerator.randomUUID() }
            coVerify {
                accountsNetworkDataSource.addMoneyToGoal(
                    accountUid = ACCOUNT_UID,
                    currency = CURRENCY,
                    savingsGoalUid = SAVINGS_GOAL_UID,
                    transferUid = RANDOM_UUID,
                    minorUnits = MINOR_UNIT
                )
            }
        }

    private companion object {
        const val RANDOM_UUID = "RANDOM_UUID"
        const val WRONG_UUID = "WRONG_UUID"
        const val ACCOUNT_TYPE_PRIMARY = "PRIMARY"
        const val ACCOUNT_UID = "ACCOUNT_UID"
        const val DEFAULT_CATEGORY = "DEFAULT_CATEGORY"
        const val SYMBOL = "SYMBOL"
        const val FRACTION_DIGITS = 123
        const val CURRENCY = "CURRENCY"
        const val SAVINGS_GOAL_UID = "SAVINGS_GOAL_UID"
        const val NAME = "NAME"
        const val MINOR_UNIT = 456
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
        val NON_PRIMARY_ACCOUNT = Account(
            accountUid = ACCOUNT_UID,
            accountType = "ACCOUNT_TYPE",
            defaultCategory = DEFAULT_CATEGORY,
            currency = APP_CURRENCY
        )
        val ACCOUNT_DATA = AccountData(
            accountUid = ACCOUNT_UID,
            accountType = ACCOUNT_TYPE_PRIMARY,
            defaultCategory = DEFAULT_CATEGORY,
            currency = CURRENCY
        )
        val SAVING_GOAL_DATA = SavingGoalData(
            savingsGoalUid = SAVINGS_GOAL_UID,
            name = NAME
        )
        val INVALID_SAVING_GOAL_DATA = SavingGoalData(
            savingsGoalUid = null,
            name = NAME
        )
        val SAVING_GOAL = SavingGoal(
            savingsGoalUid = SAVINGS_GOAL_UID,
            name = NAME
        )
    }
}
