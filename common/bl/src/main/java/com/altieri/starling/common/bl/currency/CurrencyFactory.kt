package com.altieri.starling.common.bl.currency

fun interface CurrencyFactory {
    operator fun invoke(currencyCode: String): AppCurrency
}
