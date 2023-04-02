package com.altieri.starling.transactions.data

class TransactionValidator {
    operator fun invoke(transactionData: TransactionData): Boolean =
        transactionData.feedItemUid != null &&
                transactionData.amount?.currency != null &&
                transactionData.amount.minorUnits != null &&
                transactionData.direction != null &&
                transactionData.spendingCategory != null
}
