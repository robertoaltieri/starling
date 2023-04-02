package com.altieri.starling.transactions.data

import com.altieri.starling.transactions.bl.TransactionsRepository

class TransactionsRepositoryImpl(
    private val transactionsNetworkDataSource: TransactionsNetworkDataSource,
    private val mapTransaction: TransactionDataToTransactionMapper,
    private val isTransactionValid: TransactionValidator,
) : TransactionsRepository {

    override suspend fun transactionsSince(
        accountUid: String,
        category: String,
        time: String
    ) =
        transactionsNetworkDataSource.transactionsSince(accountUid, category, time)
            .filter {
                isTransactionValid(it)
            }.map {
                mapTransaction(it)
            }
}
