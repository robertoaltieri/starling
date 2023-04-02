package com.altieri.starling.account.presentation

sealed class SavingGoalStateUI(
    val enableButton: Boolean,
    val showButton: Boolean
) {
    object Idle : SavingGoalStateUI(enableButton = true, showButton = true)
    object Running : SavingGoalStateUI(enableButton = false, showButton = true)
    object Error : SavingGoalStateUI(enableButton = true, showButton = true)
    object Success : SavingGoalStateUI(enableButton = false, showButton = false)
}
