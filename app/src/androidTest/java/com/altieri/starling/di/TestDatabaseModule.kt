package com.altieri.starling.di

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.altieri.starling.database.framework.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestDatabaseModule {
    @Provides
    fun providesAppDatabase(
    ): AppDatabase {
        val context = InstrumentationRegistry.getInstrumentation().context
        return Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()
    }
}
