package com.altieri.starling.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.altieri.starling.R
import com.altieri.starling.account.presentation.SavingGoalStateUI
import com.altieri.starling.transactions.bl.TransactionsStateUI
import com.altieri.starling.ui.composable.event.OnSaveInGoal

@Composable
fun SaveInGoalScreen(
    transactionsState: TransactionsStateUI,
    savingGoalState: SavingGoalStateUI,
    onSaveInGoal: OnSaveInGoal
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (transactionsState) {
            is TransactionsStateUI.RoundUp -> {
                Row(
                    modifier = Modifier
                        .appPadding()
                        .height(AppSize.roundUpRowHeight)
                ) {
                    AppText(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .paddingStart(),
                        text = stringResource(
                            id = if (savingGoalState.showButton) {
                                R.string.roundup
                            } else{
                                R.string.roundup_saved
                            },
                            transactionsState.roundUp,
                            transactionsState.currency,
                        )
                    )
                    SaveInGoalButton(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .paddingStart(),
                        transactionsState = transactionsState,
                        savingGoalState = savingGoalState,
                        onSaveInGoal = onSaveInGoal
                    )
                    if (savingGoalState is SavingGoalStateUI.Error) {
                        AppText(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .paddingStart(),
                            text = stringResource(id = R.string.error))
                    }
                }
            }

            is TransactionsStateUI.Loading -> {
                FullScreenLoadingAnimation(true)
            }

            TransactionsStateUI.Idle -> {}
            TransactionsStateUI.Error -> {
                AppText(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(id = R.string.error)
                )
            }
        }
    }
}
