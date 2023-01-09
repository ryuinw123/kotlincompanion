package com.example.kmitlcompanion.di


import android.app.Notification
import android.content.Context
import com.example.kmitlcompanion.ui.notification.NotificationFactory

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Module
    @InstallIn(SingletonComponent::class)
    object Application{
        @Provides
        fun providesContext(@ApplicationContext context: Context): Context {
            return context
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object NotificationModule{
        @Provides
        fun providesNotification(context : Context) : Notification {
            return NotificationFactory
                .createNotification(context)
                .build()
        }
    }



}