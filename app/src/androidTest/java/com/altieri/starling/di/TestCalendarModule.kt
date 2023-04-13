package com.altieri.starling.di

import com.altieri.starling.datetime.bl.CalendarFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [CalendarModule::class]
)
object TestCalendarModule {

    @Provides
    @Singleton
    fun providesCalendarFactory(): CalendarFactory = object : CalendarFactory {
        override fun newInstance(): Calendar = CALENDAR_TODAY
    }

    val CALENDAR_TODAY: Calendar = Calendar.getInstance().apply {
        set(2023, Calendar.APRIL, 8, 10, 10, 10)
        set(Calendar.MILLISECOND, 123)
        timeZone = TimeZone.getTimeZone("GMT")
    }
}
