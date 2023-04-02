package com.altieri.starling.account.bl

/**
 * Fetches the current saving goals and checks whether the goal with the specified name is already there.
 * In case it is missing it tries to create it using the [AccountRepository].
 * In case of success it returns the SavingGoalUID
 */
class SetupSavingGoalUseCase(
    private val repository: AccountRepository,
    private val filterSavingGoal: SavingGoalFilter
) {
    suspend operator fun invoke(
        accountUid: String,
        currency: String,
        name: String
    ): String {
        val list = repository.savingGoals(accountUid)
        val savingGoal = filterSavingGoal.goalWith(list, name)
        return savingGoal?.savingsGoalUid ?: repository.createSavingGoal(
            accountUid = accountUid,
            currency = currency,
            name = name
        )
    }
}
