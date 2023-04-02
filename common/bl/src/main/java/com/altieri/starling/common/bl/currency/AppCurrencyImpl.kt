package com.altieri.starling.common.bl.currency

import java.util.Currency

class AppCurrencyImpl(
    currencyCode: String
) : AppCurrency {
    private val currency = Currency.getInstance(currencyCode)
    override val fractionDigits: Int = currency.defaultFractionDigits
    override val symbol: String = currency.symbol
}
