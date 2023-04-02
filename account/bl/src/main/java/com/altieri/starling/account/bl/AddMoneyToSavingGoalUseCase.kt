package com.altieri.starling.account.bl

// for now this use case is pointless. It is just a wrapper of the repository but it might have more logic in the future
class AddMoneyToSavingGoalUseCase(
    private val repository: AccountRepository
) {
    suspend operator fun invoke(
        accountUid: String,
        currency: String,
        savingsGoalUid: String,
        minorUnits: Int
    ) {
        repository.addMoneyToGoal(
            accountUid = accountUid,
            currency = currency,
            savingsGoalUid = savingsGoalUid,
            minorUnits = minorUnits
        )
    }
}
