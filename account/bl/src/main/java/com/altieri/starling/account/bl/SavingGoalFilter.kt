package com.altieri.starling.account.bl

class SavingGoalFilter {
    fun goalWith(list: List<SavingGoal>, name: String) =
        list.firstOrNull { it.name == name }
}
