package com.altieri.starling.networking.framework

import com.altieri.starling.networking.domain.RefreshTokenUseCase
import okhttp3.Interceptor
import okhttp3.Response

class RefreshTokenInterceptor(
    private val refreshTokenUseCase: RefreshTokenUseCase
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        refreshTokenUseCase()
        val request = chain.request()
        val builder = request.newBuilder()
        return chain.proceed(
            builder.build()
        )
    }
}
