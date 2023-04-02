package com.altieri.starling.transactions.bl

import com.altieri.starling.datetime.bl.AppDateTime

class RoundUpUpdateUseCase(
    private val transactionsRepository: TransactionsRepository,
    private val roundUpCalculator: RoundUpCalculator,
    private val dateTime: AppDateTime
) {
    suspend operator fun invoke(
        accountUid: String,
        category: String,
        fractionDigits: Int
    ): Int {
        val transactions = transactionsRepository
            .transactionsSince(
                accountUid = accountUid,
                category = category,
                time = dateTime.oneWeekAgo()
            ).filter {
                it.direction == Transaction.Direction.OUT &&
                        it.spendingCategory != Transaction.SpendingCategory.SAVING
            }
        return roundUpCalculator.roundUp(
            list = transactions,
            fractionDigits = fractionDigits
        )
    }
}
