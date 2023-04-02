package com.altieri.starling.transactions.framework

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TransactionsApiService {
    @GET("api/v2/feed/account/{account}/category/{category}")
    suspend fun transactions(
        @Path(value = "account") account: String,
        @Path(value = "category") category: String,
        @Query(value = "changesSince") changesSince: String,
    ): TransactionsRaw

}

data class TransactionsRaw(
    val feedItems: List<TransactionRaw>
)

data class TransactionRaw(
    val feedItemUid: String?,
    val amount: AmountRaw?,
    val direction: String,
    val spendingCategory: String
)

data class AmountRaw(
    val currency: String?,
    val minorUnits: Long?
)
