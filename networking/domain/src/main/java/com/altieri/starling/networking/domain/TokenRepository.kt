package com.altieri.starling.networking.domain

interface TokenRepository {
    fun refreshToken(refreshToken: String)
    suspend fun onNewTokens(authToken: AuthToken)
}
