package com.example.kmitlcompanion.di


import android.content.Context

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



}