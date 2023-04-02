package com.altieri.starling.networking.framework

import okhttp3.Interceptor
import okhttp3.Response

class EmptyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request())
    }
}
