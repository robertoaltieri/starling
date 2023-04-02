package com.altieri.starling.di

import com.altieri.starling.di.DIConst.APP_BASE_URL
import com.altieri.starling.networking.domain.ENV
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object NetworkConfigModule {
    @Provides
    @Named(APP_BASE_URL)
    fun providesAppBaseUrl(): String = ENV.PROD

}
