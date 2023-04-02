package com.altieri.starling.transactions.bl

interface TransactionsRepository {

    suspend fun transactionsSince(
        accountUid: String,
        category: String,
        time: String
    ): List<Transaction>
}
