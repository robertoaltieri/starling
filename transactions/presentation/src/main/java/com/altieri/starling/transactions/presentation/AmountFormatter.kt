package com.altieri.starling.transactions.presentation

import kotlin.math.pow

fun interface AmountFormatter {
    operator fun invoke(value: Int, fractionDigits: Int): String
}

val AMOUNT_FORMATTER: AmountFormatter = AmountFormatter { value, fractionDigits ->
    String.format("%.${fractionDigits}f", value / (10f.pow(fractionDigits)))
}

