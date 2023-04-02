package com.altieri.starling.account.framework

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface AccountsApiService {
    @GET("api/v2/accounts")
    suspend fun accounts(): AccountsRaw

    @PUT("api/v2/account/{accountUid}/savings-goals")
    suspend fun createSavingGoal(
        @Path(value = "accountUid") accountUid: String,
        @Body createSavingGoalRequest: CreateSavingGoalRequest
    ): SavingGoalRaw

    @PUT("api/v2/account/{accountUid}/savings-goals/{savingsGoalUid}/add-money/{transferUid}")
    suspend fun addMoneyToGoal(
        @Path(value = "accountUid") accountUid: String,
        @Path(value = "savingsGoalUid") savingsGoalUid: String,
        @Path(value = "transferUid") transferUid: String,
        @Body addMoneyToGoalRequest: AddMoneyToGoalRequest
    ): AddMoneyToGoalResponse

    @GET("api/v2/account/{accountUid}/savings-goals")
    suspend fun savingGoals(
        @Path(value = "accountUid") accountUid: String
    ): SavingGoalsRaw

}

// there are more info here but I'm considering only the savingsGoalUid
// I'm assuming for example that the currency is the same as the one for the primary account
data class SavingGoalsRaw(
    val savingsGoalList: List<SavingGoalRaw>
)

data class SavingGoalRaw(
    val savingsGoalUid: String?,
    val name: String?
)

data class CreateSavingGoalRequest(
    val currency: String,
    val name: String
)
data class AddMoneyToGoalResponse(
    val transferUid: String
)

data class CurrencyAndAmountRaw(
    val currency: String,
    val minorUnits: Int
)
data class AddMoneyToGoalRequest(
    val amount: CurrencyAndAmountRaw
)

data class AccountsRaw(
    val accounts: List<AccountRaw>
)
