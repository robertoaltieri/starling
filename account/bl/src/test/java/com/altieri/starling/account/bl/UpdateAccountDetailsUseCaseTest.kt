package com.altieri.starling.account.bl

import com.altieri.starling.common.bl.currency.AppCurrencyInfo
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
import java.lang.IllegalStateException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateAccountDetailsUseCaseTest {

    @MockK
    private lateinit var repository: AccountRepository

    private lateinit var updateAccountDetailsUseCase: UpdateAccountDetailsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        updateAccountDetailsUseCase = UpdateAccountDetailsUseCase(repository)
    }

    @After
    fun tearDown() {
        confirmVerified(repository)
    }

    @Test
    fun `GIVEN there is a primary account WHEN updateAccountDetailsUseCase is called THEN return the primary account`() =
        runTest {
            // given
            coEvery { repository.primaryAccount() } returns PRIMARY_ACCOUNT

            // when
            val account = updateAccountDetailsUseCase()

            // then
            coVerify { repository.primaryAccount() }
            assertEquals(PRIMARY_ACCOUNT, account)
        }

    @Test
    fun `GIVEN there are no primary account WHEN updateAccountDetailsUseCase is called THEN throw an exception`() =
        runTest {
            // given
            coEvery { repository.primaryAccount() } returns null

            // then
            assertFailsWith<IllegalStateException> { updateAccountDetailsUseCase() }
            coVerify { repository.primaryAccount() }
        }

    private companion object {
        val PRIMARY_ACCOUNT = Account(
            accountUid = "accountUid",
            accountType = "accountType",
            defaultCategory = "defaultCategory",
            currency = AppCurrencyInfo(
                currency = "currency",
                fractionDigits = 2,
                symbol = "£"
            )
        )
    }
}
