package com.altieri.starling.transactions.data

interface TransactionsNetworkDataSource {
    suspend fun transactionsSince(
        accountUid: String,
        category: String,
        time: String
    ): List<TransactionData>
}
