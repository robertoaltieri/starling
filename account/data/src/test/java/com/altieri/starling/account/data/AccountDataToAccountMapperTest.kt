package com.altieri.starling.account.data

import com.altieri.starling.account.bl.Account
import com.altieri.starling.common.bl.currency.AppCurrencyInfo
import com.altieri.starling.common.bl.currency.CurrencyInfoHelper
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AccountDataToAccountMapperTest {

    @MockK
    private lateinit var currencyInfoHelper: CurrencyInfoHelper

    private lateinit var accountDataToAccountMapper: AccountDataToAccountMapper

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        accountDataToAccountMapper = AccountDataToAccountMapper(currencyInfoHelper = currencyInfoHelper)
    }

    @After
    fun tearDown() {
        confirmVerified(currencyInfoHelper)
    }

    @Test
    fun `GIVEN an AccountData WHEN accountDataToAccountMapper is called THEN map to the correct Account`() {
        // given
        val accountData = ACCOUNT_DATA
        every { currencyInfoHelper.info(CURRENCY) } returns APP_CURRENCY

        // when
        val account = accountDataToAccountMapper(accountData)

        // then
        verify { currencyInfoHelper.info(CURRENCY) }
        assertEquals(ACCOUNT_CORRECT, account)
    }

    private companion object {
        const val SYMBOL = "SYMBOL"
        const val FRACTION_DIGITS = 123
        const val CURRENCY = "CURRENCY"
        val ACCOUNT_DATA = AccountData(
            accountUid = "accountUid",
            accountType = "accountType",
            defaultCategory = "defaultCategory",
            currency = CURRENCY
        )
        val APP_CURRENCY = AppCurrencyInfo(
            currency = CURRENCY,
            fractionDigits = FRACTION_DIGITS,
            symbol = SYMBOL
        )
        val ACCOUNT_CORRECT = Account(
            accountUid = "accountUid",
            accountType = "accountType",
            defaultCategory = "defaultCategory",
            currency = APP_CURRENCY
        )
    }
}
