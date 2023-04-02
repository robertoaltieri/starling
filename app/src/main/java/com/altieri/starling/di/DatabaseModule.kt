package com.altieri.starling.di

import android.content.Context
import com.altieri.starling.database.framework.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun providesAppDatabase(
        @ApplicationContext
        context: Context
    ): AppDatabase =
        AppDatabase.instance(context)

}
