package com.altieri.starling.account.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altieri.starling.account.bl.AddMoneyToSavingGoalUseCase
import com.altieri.starling.account.bl.OnNewTokensUseCase
import com.altieri.starling.account.bl.SetupSavingGoalUseCase
import com.altieri.starling.account.bl.UpdateAccountDetailsUseCase
import com.altieri.starling.di.DIConst.ACCOUNTS_GOAL_STATE_FEED
import com.altieri.starling.di.DIConst.ACCOUNTS_STATE_FEED
import com.altieri.starling.di.DIConst.SINGLE_THREAD_DISPATCHER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AccountsViewModel @Inject constructor(
    @Named(ACCOUNTS_STATE_FEED) private val _accountState: MutableStateFlow<AccountsStateUI>,
    @Named(ACCOUNTS_GOAL_STATE_FEED) private val _savingGoalState: MutableStateFlow<SavingGoalStateUI>,
    private val updateAccountDetailsUseCase: UpdateAccountDetailsUseCase,
    private val setupSavingGoalUseCase: SetupSavingGoalUseCase,
    private val addMoneyToSavingGoalUseCase: AddMoneyToSavingGoalUseCase,
    private val onNewTokensUseCase: OnNewTokensUseCase,
    @Named(SINGLE_THREAD_DISPATCHER) private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    fun updateAccount() {
        viewModelScope.launch(dispatcher + CoroutineExceptionHandler { _, _ ->
            _accountState.value = AccountsStateUI.Error
        }) {
            if (accountState.value == AccountsStateUI.Idle) {
                _accountState.value = AccountsStateUI.Loading
                val account = updateAccountDetailsUseCase()
                _accountState.value = AccountsStateUI.PrimaryAccounts(
                    account = account
                )
            }
        }
    }

    fun addMoneyToSavingGoal(
        accountUid: String, currency: String, minorUnits: Int
    ) {
        viewModelScope.launch(dispatcher + CoroutineExceptionHandler { _, _ ->
            _savingGoalState.value = SavingGoalStateUI.Error
        }) {
            if (_savingGoalState.value != SavingGoalStateUI.Running) {
                _savingGoalState.value = SavingGoalStateUI.Running
                val savingsGoalUid = setupSavingGoalUseCase(
                    accountUid = accountUid, currency = currency, name = SAVING_GOAL_NAME
                )
                addMoneyToSavingGoalUseCase(
                    accountUid = accountUid,
                    currency = currency,
                    savingsGoalUid = savingsGoalUid,
                    minorUnits = minorUnits
                )
                _savingGoalState.value = SavingGoalStateUI.Success
            }
        }
    }

    fun onNewTokens(accessToken: String, refreshToken: String, expiresIn: Int) {
        viewModelScope.launch {
            onNewTokensUseCase(
                accessToken = accessToken,
                refreshToken = refreshToken,
                expiresIn = expiresIn
            )
        }

    }

    val accountState: StateFlow<AccountsStateUI> = _accountState
    val savingGoalState: StateFlow<SavingGoalStateUI> = _savingGoalState

    private companion object {
        const val SAVING_GOAL_NAME = "SAVING_GOAL_NAME" // hardcoded for now
    }
}
