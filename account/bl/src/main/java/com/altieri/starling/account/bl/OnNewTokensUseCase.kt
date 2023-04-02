package com.altieri.starling.account.bl

import com.altieri.starling.networking.domain.AuthToken
import com.altieri.starling.networking.domain.TokenRepository

// for now this use case is pointless. It is just a wrapper of the repository but it might have more logic in the future

/**
 * Saves the new tokens. This is triggered by a deep link
 */
class OnNewTokensUseCase(
    private val repository: TokenRepository
) {
    suspend operator fun invoke(
        accessToken: String,
        refreshToken: String,
        expiresIn: Int
    ) {
        repository.onNewTokens(
            authToken = AuthToken(
                accessToken = accessToken,
                refreshToken = refreshToken,
                expiresIn = expiresIn
            )
        )
    }
}
