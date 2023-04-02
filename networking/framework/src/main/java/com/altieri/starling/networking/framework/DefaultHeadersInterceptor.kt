package com.altieri.starling.networking.framework

import com.altieri.starling.networking.domain.Header
import com.altieri.starling.networking.domain.StoredToken
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class DefaultHeadersInterceptor(
    private val storedToken: StoredToken
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val accessToken: String? = storedToken.accessToken
        val builder = request.newBuilder()
        val newRequestBuilder =
            builder.apply {
                if (!accessToken.isNullOrEmpty()) {
                    builder.withAuthBearer(accessToken)
                }
            }
        return chain.proceed(
            newRequestBuilder.build()
        )
    }
}

private fun Request.Builder.withAuthBearer(token: String) =
    header(Header.Authorization, "${Header.Bearer} $token")
