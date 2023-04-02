package com.altieri.starling.di

import com.altieri.starling.datetime.bl.AppDateTime
import com.altieri.starling.datetime.bl.AppDateTimeImpl
import com.altieri.starling.datetime.bl.CalendarFactory
import com.altieri.starling.di.DIConst.BOOLEAN_FEED
import com.altieri.starling.di.DIConst.DEFAULT_DISPATCHER
import com.altieri.starling.di.DIConst.SINGLE_THREAD_DISPATCHER
import com.altieri.starling.di.DIConst.STRING_FEED
import com.altieri.starling.di.DIConst.TOKEN_REPOSITORY_DISPATCHER
import com.altieri.starling.transactions.presentation.AMOUNT_FORMATTER
import com.altieri.starling.transactions.presentation.AmountFormatter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.Executors
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {
    @Provides
    @Named(BOOLEAN_FEED)
    fun providesBooleanFeed(
    ): MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    @Provides

    @Named(STRING_FEED)
    fun providesStringFeed(
    ): MutableStateFlow<String> =
        MutableStateFlow("")

    @Provides
    @Named(SINGLE_THREAD_DISPATCHER)
    fun providesSingleThreadDispatcher(): CoroutineDispatcher =
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    @Provides
    @Named(DEFAULT_DISPATCHER)
    fun providesDefaultDispatcher(): CoroutineDispatcher =
        Dispatchers.Default


    @Provides
    @Singleton
    fun providesFormatAmount(): AmountFormatter = AMOUNT_FORMATTER

    @Provides
    @Singleton
    fun providesCalendarFactory(): CalendarFactory = CalendarFactory()

    @Provides
    @Singleton
    fun providesAppDateTime(
        calendarFactory: CalendarFactory
    ): AppDateTime = AppDateTimeImpl(calendarFactory)
}
