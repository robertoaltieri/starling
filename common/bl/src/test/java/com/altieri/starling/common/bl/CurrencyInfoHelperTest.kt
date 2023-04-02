package com.altieri.starling.common.bl

import com.altieri.starling.common.bl.currency.AppCurrency
import com.altieri.starling.common.bl.currency.AppCurrencyInfo
import com.altieri.starling.common.bl.currency.CurrencyFactory
import com.altieri.starling.common.bl.currency.CurrencyInfoHelper
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals


class CurrencyInfoHelperTest {

    @MockK
    private lateinit var factory: CurrencyFactory

    @MockK
    private lateinit var currency: AppCurrency

    private lateinit var currencyInfoHelper: CurrencyInfoHelper

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        currencyInfoHelper = CurrencyInfoHelper(factory)
    }

    @After
    fun tearDown() {
        confirmVerified(factory, currency)
    }

    @Test
    fun `GIVEN the currency code WHEN info THEN return the info`() {
        // given
        every { factory.invoke(CURRENCY) } returns currency
        every { currency.symbol } returns SYMBOL
        every { currency.fractionDigits } returns FRACTION_DIGITS

        // when
        val appCurrency = currencyInfoHelper.info(CURRENCY)

        // then
        verify { factory.invoke(CURRENCY) }
        verify { currency.symbol }
        verify { currency.fractionDigits }

        assertEquals(APP_CURRENCY, appCurrency)
    }

    private companion object {
        const val SYMBOL = "£"
        const val CURRENCY = "CURRENCY"
        const val FRACTION_DIGITS = 2
        val APP_CURRENCY = AppCurrencyInfo(
            currency = CURRENCY,
            fractionDigits = FRACTION_DIGITS,
            symbol = SYMBOL
        )
    }
}
