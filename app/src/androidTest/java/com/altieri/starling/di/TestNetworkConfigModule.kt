package com.altieri.starling.di

import com.altieri.starling.di.DIConst.APP_BASE_URL
import com.altieri.starling.networking.framework.test.TEST_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Named

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkConfigModule::class]
)
object TestNetworkConfigModule {
    @Provides
    @Named(APP_BASE_URL)
    fun providesAppBaseUrl(): String = TEST_BASE_URL
}
