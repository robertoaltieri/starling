package com.altieri.starling.networking.framework

data class AuthTokenRaw(
    val access_token: String,
    val refresh_token: String,
    val expires_in: Int
)
