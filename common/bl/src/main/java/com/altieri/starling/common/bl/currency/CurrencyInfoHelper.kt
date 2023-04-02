package com.altieri.starling.common.bl.currency

class CurrencyInfoHelper(
    private val createCurrency: CurrencyFactory = CurrencyFactory { currencyCode ->
        AppCurrencyImpl(currencyCode)
    }
) {
    fun info(currencyCode: String): AppCurrencyInfo {
        val currency = createCurrency(currencyCode)
        return AppCurrencyInfo(
            currency = currencyCode,
            fractionDigits = currency.fractionDigits,
            symbol = currency.symbol
        )
    }
}

