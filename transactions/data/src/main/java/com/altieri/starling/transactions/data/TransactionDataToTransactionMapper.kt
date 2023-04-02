package com.altieri.starling.transactions.data

import com.altieri.starling.transactions.bl.Amount
import com.altieri.starling.transactions.bl.Transaction

class TransactionDataToTransactionMapper {
    // call this only after having checked the validity of the transactionData
    operator fun invoke(transactionData: TransactionData): Transaction =
        Transaction(
            feedItemUid = transactionData.feedItemUid!!,
            amount = Amount(
                currency = transactionData.amount!!.currency!!,
                minorUnits = transactionData.amount.minorUnits!!,
            ),
            direction = when (transactionData.direction) {
                OUT -> {
                    Transaction.Direction.OUT
                }
                IN -> {
                    Transaction.Direction.IN
                }
                else -> {
                    Transaction.Direction.OTHER
                }
            },
            spendingCategory = when(transactionData.spendingCategory) {
                SAVING -> Transaction.SpendingCategory.SAVING
                else -> Transaction.SpendingCategory.OTHER
            }
        )

    private companion object {
        const val OUT = "OUT"
        const val IN = "IN"
        const val SAVING = "SAVING"
    }
}
