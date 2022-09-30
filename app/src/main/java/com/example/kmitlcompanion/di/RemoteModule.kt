package com.example.kmitlcompanion.di

import com.example.kmitlcompanion.BuildConfig
import com.example.kmitlcompanion.data.repository.RemoteRepository
import com.example.kmitlcompanion.remote.RetrofitClient
import com.example.kmitlcompanion.remote.service.RetrofitServiceFactory
import com.example.kmitlcompanion.remote.service.RetrofitTestClient
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteModule {

    @Module
    @InstallIn(SingletonComponent::class)
    object Retrofit {
        @Provides
        fun provideRetrofitTestClient(): RetrofitTestClient {
            return RetrofitServiceFactory.makeRetrofitTestClient(BuildConfig.DEBUG)
        }

    }

    @Binds
    abstract fun bindProjectsRemote(retrofitClient: RetrofitClient): RemoteRepository
}