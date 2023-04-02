package com.altieri.starling.networking.domain

class RefreshTokenUseCase(
    private val storedToken: StoredToken,
    private val checkAccessTokenUseCase: CheckAccessTokenUseCase,
    private val tokenRepository: TokenRepository
) {
    operator fun invoke() {
        if (checkAccessTokenUseCase.shouldRefresh()) {
            tokenRepository.refreshToken(storedToken.refreshToken!!)
        }
    }
}
