package com.example.kmitlcompanion.di

import com.example.kmitlcompanion.domain.executor.PostExecutionThread
import com.example.kmitlcompanion.scheduler.UiThread
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UiModule {
    @Binds
    abstract fun bindPostExecutionThread(uiThread: UiThread): PostExecutionThread
}