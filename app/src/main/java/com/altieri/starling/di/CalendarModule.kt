package com.altieri.starling.di

import com.altieri.starling.datetime.bl.CalendarFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CalendarModule {
    @Provides
    @Singleton
    fun providesCalendarFactory(): CalendarFactory = object : CalendarFactory {}

}
