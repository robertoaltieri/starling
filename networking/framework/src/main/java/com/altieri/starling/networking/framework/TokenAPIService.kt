package com.altieri.starling.networking.framework

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface TokenAPIService {
    @FormUrlEncoded
    @POST("oauth/access-token")
    @Headers(
        "Content-Type: application/x-www-form-urlencoded"
    )
    suspend fun refreshToken(
        @Field("refresh_token") refreshToken: String,
        @Field("client_id") clientId : String,
        @Field("client_secret") clientSecret : String,
        @Field("grant_type") grantType : String
    ): AuthTokenRaw
}
