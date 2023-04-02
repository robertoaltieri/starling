package com.altieri.starling.networking.framework

import com.altieri.starling.networking.domain.AuthToken

class AuthTokenRawToAuthTokenMapper {
    operator fun invoke(authTokenRaw: AuthTokenRaw): AuthToken =
        AuthToken(
            accessToken = authTokenRaw.access_token,
            refreshToken = authTokenRaw.refresh_token,
            expiresIn = authTokenRaw.expires_in
        )
}
