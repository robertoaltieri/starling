package com.altieri.starling.transactions.bl

sealed interface TransactionsStateUI {
    object Idle: TransactionsStateUI
    object Loading: TransactionsStateUI
    object Error: TransactionsStateUI
    data class RoundUp(
        val accountUid: String,
        val roundUp: String,
        val currency: String,
        val roundUpMinorUnits: Int,
        val saveInGoalButtonEnabled: Boolean
    ): TransactionsStateUI
}
