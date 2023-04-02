package com.altieri.starling.account.bl

import com.altieri.starling.common.bl.currency.AppCurrencyInfo

data class Account(
    val accountUid: String,
    val accountType: String,
    val defaultCategory: String,
    val currency: AppCurrencyInfo,
)
