package com.example.kmitlcompanion.di

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import com.example.kmitlcompanion.ui.mainactivity.utils.BottomBarUtils
import com.example.kmitlcompanion.ui.mapboxview.utils.DpConverterUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UtilsModule {
    @InstallIn(SingletonComponent::class)
    @Module
    object BottomBar{
        @Singleton
        @Provides
        fun providesBottomBar(): BottomBarUtils {
            return BottomBarUtils()
        }
    }

    @InstallIn(SingletonComponent::class)
    @Module
    object Handler{
        @Singleton
        @Provides
        fun providesHandler(): android.os.Handler {
            val handlerThread =  HandlerThread("BackgroudThread")
            handlerThread.start()
            return Handler(handlerThread.looper)
        }
    }
}