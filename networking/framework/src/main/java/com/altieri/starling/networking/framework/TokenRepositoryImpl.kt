package com.altieri.starling.networking.framework

import com.altieri.starling.networking.domain.AuthToken
import com.altieri.starling.networking.domain.CheckAccessTokenUseCase
import com.altieri.starling.networking.domain.StoredToken
import com.altieri.starling.networking.domain.TokenRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

// this should have been in the data layer and it should have used a data source and not directly the TokenAPIService // !!!!
class TokenRepositoryImpl(
    private val service: TokenAPIService,
    private val storedToken: StoredToken,
    private val checkAccessTokenUseCase: CheckAccessTokenUseCase,
    private val mapAuthToken: AuthTokenRawToAuthTokenMapper,
    private var updatingTokens: Boolean,
    private val dispatcher: CoroutineDispatcher
) : TokenRepository {

    private val scope =
        CoroutineScope(SupervisorJob() + dispatcher + CoroutineExceptionHandler { _, _ -> })

    override fun refreshToken(refreshToken: String) {
        if (!updatingTokens) {
            scope.launch {
                if (!updatingTokens) {
                    updatingTokens = true
                    if (checkAccessTokenUseCase.shouldRefresh()) {
                        val authToken = refreshAccessToken(refreshToken)
                        storedToken.storeTokens(authToken)
                    }
                }
                updatingTokens = false
            }
        }
    }

    override suspend fun onNewTokens(authToken: AuthToken) {
        if (!updatingTokens) {
            withContext(dispatcher) {
                if (!updatingTokens) {
                    updatingTokens = true
                    storedToken.storeTokens(authToken)
                }
                updatingTokens = false
            }
        }
    }

    private fun AuthTokenRaw.toAuthToken() =
        mapAuthToken(this)

    private suspend fun refreshAccessToken(refreshToken: String): AuthToken =
        service.refreshToken(
            refreshToken = refreshToken,
            clientId = BuildConfig.STARLING_CLIENT_ID,
            clientSecret = BuildConfig.STARLING_CLIENT_SECRET,
            grantType = GrantType.REFRESH_TOKEN
        ).toAuthToken()

}

object GrantType {
    const val REFRESH_TOKEN = "refresh_token"
}
