package com.altieri.starling.transactions.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altieri.starling.di.DIConst.SINGLE_THREAD_DISPATCHER
import com.altieri.starling.di.DIConst.TRANSACTIONS_STATE_FEED
import com.altieri.starling.transactions.bl.RoundUpUpdateUseCase
import com.altieri.starling.transactions.bl.TransactionsStateUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    @Named(TRANSACTIONS_STATE_FEED)
    private val _transactionsState: MutableStateFlow<TransactionsStateUI>,
    private val roundUpUpdateUseCase: RoundUpUpdateUseCase,
    private val amountFormatter: AmountFormatter,
    @Named(SINGLE_THREAD_DISPATCHER)
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    fun onShowRoundUp(
        accountUid: String,
        category: String,
        currency: String,
        fractionDigits: Int
    ) {
        viewModelScope.launch(
            dispatcher +
                    CoroutineExceptionHandler { _, _ ->
                        _transactionsState.value = TransactionsStateUI.Error
                    }
        ) {
            if (transactionsState.value == TransactionsStateUI.Idle
                || transactionsState.value == TransactionsStateUI.Error
            ) {
                _transactionsState.value = TransactionsStateUI.Loading
                val roundUp = roundUpUpdateUseCase(
                    accountUid = accountUid,
                    category = category,
                    fractionDigits = fractionDigits
                )
                val formatted = amountFormatter(roundUp, fractionDigits)
                _transactionsState.value = TransactionsStateUI.RoundUp(
                    accountUid = accountUid,
                    currency = currency,
                    roundUpMinorUnits = roundUp,
                    roundUp = formatted,
                    saveInGoalButtonEnabled = roundUp > 0
                )
            }
        }
    }

    val transactionsState: StateFlow<TransactionsStateUI> = _transactionsState
}
