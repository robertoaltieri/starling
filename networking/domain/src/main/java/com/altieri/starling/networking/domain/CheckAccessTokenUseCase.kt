package com.altieri.starling.networking.domain

import com.altieri.starling.datetime.bl.AppDateTime

/**
 * If the access token and the refresh token are present it returns whether the current time is after the time the tokens where set + 70% of the expiresIn
 * Otherwise it returns false.
 */
class CheckAccessTokenUseCase(
    private val storedToken: StoredToken,
    private val dateTimeUtil: AppDateTime
) {
    fun shouldRefresh() =
        storedToken.canRefreshToken() &&
                dateTimeUtil.time() > storedToken.expiryTime - storedToken.expiresIn * 0.3
}
