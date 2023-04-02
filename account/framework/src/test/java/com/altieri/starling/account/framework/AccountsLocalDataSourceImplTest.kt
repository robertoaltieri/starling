package com.altieri.starling.account.framework

import com.altieri.starling.account.bl.Account
import com.altieri.starling.account.data.AccountData
import com.altieri.starling.common.bl.currency.AppCurrencyInfo
import com.altieri.starling.database.framework.AccountDBEntity
import com.altieri.starling.database.framework.AccountDao
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class AccountsLocalDataSourceImplTest {

    @MockK
    private lateinit var accountDao: AccountDao

    @MockK
    private lateinit var toAccountDataMapper: AccountEntityToAccountDataMapper

    @MockK
    private lateinit var toAccountDBEntityMapper: AccountToAccountEntityMapper

    private lateinit var accountsLocalDataSource: AccountsLocalDataSourceImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        accountsLocalDataSource = AccountsLocalDataSourceImpl(
            accountDao = accountDao,
            toAccountDataMapper = toAccountDataMapper,
            toAccountDBEntityMapper = toAccountDBEntityMapper
        )
    }

    @After
    fun tearDown() {
        confirmVerified(
            accountDao, toAccountDataMapper, toAccountDBEntityMapper
        )
    }

    private class TestException : Exception()

    @Test
    fun `GIVEN the database contains account a primary account WHEN primaryAccount is called THEN return the mapped account`() =
        runTest {
            // given
            coEvery { accountDao.primaryAccount() } returns listOf(ACCOUNT_DB)
            coEvery { toAccountDataMapper(ACCOUNT_DB) } returns ACCOUNT_DATA

            // when
            val account = accountsLocalDataSource.primaryAccount()

            // then
            coVerify { accountDao.primaryAccount() }
            coVerify { toAccountDataMapper(ACCOUNT_DB) }
            assertEquals(ACCOUNT_DATA, account)
        }

    @Test
    fun `GIVEN the database is empty WHEN primaryAccount is called THEN return null`() =
        runTest {
            // given
            coEvery { accountDao.primaryAccount() } returns emptyList()

            // when
            val account = accountsLocalDataSource.primaryAccount()

            // then
            coVerify { accountDao.primaryAccount() }
            assertNull(account)
        }

    @Test
    fun `GIVEN the database fails WHEN primaryAccount is called THEN do not catch the exception`() =
        runTest {
            // given
            coEvery { accountDao.primaryAccount() } throws TestException()

            // then
            assertFailsWith<TestException> { accountsLocalDataSource.primaryAccount() }
            coVerify { accountDao.primaryAccount() }
        }

    @Test
    fun `GIVEN a list of account WHEN save is called THEN save the mapped account`() =
        runTest {
            // given
            coEvery { accountDao.save(listOf(ACCOUNT_DB)) } answers {}
            coEvery { toAccountDBEntityMapper(ACCOUNT) } returns ACCOUNT_DB

            // when
            accountsLocalDataSource.save(listOf(ACCOUNT))

            // then
            coVerify { accountDao.save(listOf(ACCOUNT_DB)) }
            coVerify { toAccountDBEntityMapper(ACCOUNT) }
        }

    @Test
    fun `GIVEN the database fails WHEN save is called THEN do not catch the exception`() =
        runTest {
            // given
            coEvery { accountDao.save(listOf(ACCOUNT_DB)) } throws TestException()

            coEvery { toAccountDBEntityMapper(ACCOUNT) } returns ACCOUNT_DB

            // when
            assertFailsWith<TestException> {
                accountsLocalDataSource.save(listOf(ACCOUNT))
            }
            // then
            coVerify { accountDao.save(listOf(ACCOUNT_DB)) }
            coVerify { toAccountDBEntityMapper(ACCOUNT) }
        }

    private companion object {
        const val ACCOUNT_TYPE_PRIMARY = "PRIMARY"
        const val ACCOUNT_UID = "ACCOUNT_UID"
        const val DEFAULT_CATEGORY = "DEFAULT_CATEGORY"
        const val CURRENCY = "CURRENCY"
        const val SYMBOL = "SYMBOL"
        const val FRACTION_DIGITS = 2
        val ACCOUNT = Account(
            accountUid = ACCOUNT_UID,
            accountType = ACCOUNT_TYPE_PRIMARY,
            defaultCategory = DEFAULT_CATEGORY,
            currency = AppCurrencyInfo(
                currency = CURRENCY, fractionDigits = FRACTION_DIGITS, symbol = SYMBOL
            )
        )
        val ACCOUNT_DATA = AccountData(
            accountUid = ACCOUNT_UID,
            accountType = ACCOUNT_TYPE_PRIMARY,
            defaultCategory = DEFAULT_CATEGORY,
            currency = CURRENCY
        )
        val ACCOUNT_DB = AccountDBEntity(
            accountUid = ACCOUNT_UID,
            accountType = ACCOUNT_TYPE_PRIMARY,
            defaultCategory = DEFAULT_CATEGORY,
            currency = CURRENCY
        )
    }
}
