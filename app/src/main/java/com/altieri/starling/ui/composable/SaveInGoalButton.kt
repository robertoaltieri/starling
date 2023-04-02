package com.altieri.starling.ui.composable

import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.altieri.starling.R
import com.altieri.starling.account.presentation.SavingGoalStateUI
import com.altieri.starling.transactions.bl.TransactionsStateUI
import com.altieri.starling.ui.composable.event.OnSaveInGoal

@Composable
fun SaveInGoalButton(
    modifier: Modifier,
    transactionsState: TransactionsStateUI.RoundUp,
    savingGoalState: SavingGoalStateUI,
    onSaveInGoal: OnSaveInGoal
) {
    if (savingGoalState.showButton) {
        val buttonEnabled =
            transactionsState.saveInGoalButtonEnabled && savingGoalState.enableButton

        Button(
            modifier = modifier,
            enabled = buttonEnabled,
            content = {
                AppText(
                    color = if (buttonEnabled) {
                        AppColor.buttonTextEnabled
                    } else {
                        AppColor.buttonTextDisabled
                    },
                    text = stringResource(
                        id = R.string.save_in_goal,
                    )
                )
            },
            onClick = {
                onSaveInGoal(
                    accountUid = transactionsState.accountUid,
                    currency = transactionsState.currency,
                    minorUnits = transactionsState.roundUpMinorUnits
                )
            }
        )
    }
}
