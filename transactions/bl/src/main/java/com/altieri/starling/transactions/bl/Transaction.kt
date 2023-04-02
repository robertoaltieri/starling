package com.altieri.starling.transactions.bl

data class Transaction(
    val feedItemUid: String,
    val amount: Amount,
    val direction: Direction,
    val spendingCategory: SpendingCategory
) {
    enum class Direction {
        IN, OUT, OTHER
    }
    enum class SpendingCategory {
        SAVING, OTHER
    }
}
