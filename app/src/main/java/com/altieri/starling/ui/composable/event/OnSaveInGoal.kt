package com.altieri.starling.ui.composable.event

fun interface OnSaveInGoal {
    operator fun invoke(
        accountUid: String,
        currency: String,
        minorUnits: Int
    )
}

fun interface OnNewToken {
    operator fun invoke(
        accessToken: String,
        refreshToken: String,
        expiresIn: Int
    )
}
