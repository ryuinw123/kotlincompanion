package com.example.kmitlcompanion.di

import android.app.Application
import android.content.Context
import com.example.kmitlcompanion.cache.database.AppDatabase
import com.example.kmitlcompanion.ui.mapboxview.utils.DpConverterUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConverterModule {
    @InstallIn(SingletonComponent::class)
    @Module
    object DpConverter{
        @Provides
        fun providesDisplayConverter(@ApplicationContext context: Context): DpConverterUtils {
            return DpConverterUtils(context)
        }
    }
}