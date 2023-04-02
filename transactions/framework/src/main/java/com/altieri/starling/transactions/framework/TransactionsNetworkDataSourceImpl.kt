package com.altieri.starling.transactions.framework

import com.altieri.starling.transactions.data.AmountData
import com.altieri.starling.transactions.data.TransactionData
import com.altieri.starling.transactions.data.TransactionsNetworkDataSource

class TransactionsNetworkDataSourceImpl(
    private val apiService: TransactionsApiService,
    private val rawTransactionMapper: RawTransactionMapper
) : TransactionsNetworkDataSource {
    override suspend fun transactionsSince(
        accountUid: String,
        category: String,
        time: String
    ): List<TransactionData> =
        apiService.transactions(
            account = accountUid,
            category = category,
            changesSince = time
        ).feedItems.map {
            rawTransactionMapper(it)
        }
}

class RawTransactionMapper {
    operator fun invoke(transactionRaw: TransactionRaw) =
        TransactionData(
            feedItemUid = transactionRaw.feedItemUid,
            amount = AmountData(
                currency = transactionRaw.amount?.currency,
                minorUnits = transactionRaw.amount?.minorUnits
            ),
            direction = transactionRaw.direction,
            spendingCategory = transactionRaw.spendingCategory
        )
}
