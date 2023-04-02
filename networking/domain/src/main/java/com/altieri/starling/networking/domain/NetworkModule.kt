package com.altieri.starling.networking.domain

/// this should also contain the refresh token
data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Int
)

interface StoredToken {
    fun storeTokens(authToken: AuthToken)
    val accessToken: String? // this would be null before the sign in
    val refreshToken: String?
    val expiryTime: Long
    val expiresIn: Int
}

fun StoredToken.canRefreshToken() = !(accessToken.isNullOrEmpty() || refreshToken.isNullOrEmpty())
