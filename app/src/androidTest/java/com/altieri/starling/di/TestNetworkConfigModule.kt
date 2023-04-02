package com.altieri.starling.di

import com.altieri.starling.di.DIConst.APP_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.mockwebserver.MockWebServer
import javax.inject.Named

val AppMockWebServer = MockWebServer()

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkConfigModule::class]
)
object TestNetworkConfigModule {
    @Provides
    fun providesMockWebServer() = AppMockWebServer

    @Provides
    @Named(APP_BASE_URL)
    fun providesAppBaseUrl(
        mockWebServer: MockWebServer
    ): String = mockWebServer.url("/").toString()
}
