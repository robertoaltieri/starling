package com.altieri.starling.transactions.bl

import kotlin.math.ceil
import kotlin.math.pow

class RoundUpCalculator {
    fun roundUp(list: List<Transaction>, fractionDigits: Int): Int =
        (list.fold(0.0) { acc, transaction ->
            val amount =
                transaction.amount.minorUnits.toDouble() / 10.0.pow(fractionDigits.toDouble())
            acc + (ceil(amount) - amount)
        } *  10.0.pow(fractionDigits)).toInt()
}
