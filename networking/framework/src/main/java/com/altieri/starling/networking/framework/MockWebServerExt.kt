package com.altieri.starling.networking.framework

import android.content.Context
import com.altieri.starling.common.framework.AppAssetsManager
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.lang.IllegalArgumentException
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

private const val RESPONSE_DELAY = 400L
fun MockWebServer.enqueueSuccess(context: Context, fileName: String): MockWebServer {
    enqueue(MockResponse().setSuccess(context, fileName).setBodyDelay(1, TimeUnit.SECONDS))
    return this
}

fun MockWebServer.enqueueSuccesses(
    context: Context,
    vararg pairs: Pair<String, String>
): MockWebServer {
    val map = pairs.toMap()
    dispatcher =
        object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val mockResponse = MockResponse()
                return map[request.path]?.let { body: String ->
                    mockResponse.setBodyDelay(RESPONSE_DELAY, TimeUnit.MILLISECONDS)
                        .setSuccess(context, body)
                } ?: let {
                    throw IllegalArgumentException("MockWebServer doesn't handle path=${request.path}")
                }
            }
        }
    return this
}

fun MockWebServer.enqueueHttpOk(): MockWebServer {
    enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_OK))
    return this
}

fun MockWebServer.enqueueFailure(): MockWebServer {
    enqueue(
        MockResponse()
            .setBodyDelay(1, TimeUnit.SECONDS)
            .setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
    )
    return this
}

fun MockResponse.setSuccess(context: Context, fileName: String): MockResponse {
    val body: String = AppAssetsManager.readFile(context, fileName)
    setResponseCode(HttpURLConnection.HTTP_OK).setBody(body)
    return this
}
