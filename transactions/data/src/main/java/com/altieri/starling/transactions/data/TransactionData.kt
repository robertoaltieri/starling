package com.altieri.starling.transactions.data

data class TransactionData(
    val feedItemUid: String?,
    val amount: AmountData?,
    val direction: String?,
    val spendingCategory: String?
)

data class AmountData(
    val currency: String?,
    val minorUnits: Long?
)
