package com.altieri.starling.networking.api

import com.altieri.starling.account.framework.AccountsApiService
import com.altieri.starling.transactions.framework.TransactionsApiService

interface AppApiService :
    AccountsApiService,
    TransactionsApiService
