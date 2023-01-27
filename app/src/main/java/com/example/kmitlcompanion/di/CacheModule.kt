package com.example.kmitlcompanion.di

import android.app.Application
import com.example.kmitlcompanion.cache.RoomRepository
import com.example.kmitlcompanion.cache.database.AppDatabase
import com.example.kmitlcompanion.data.repository.CacheRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CacheModule {

    @Module
    @InstallIn(SingletonComponent::class)
    object DatabaseModule{
        @Provides
        fun providesDataBase(application: Application): AppDatabase {
            return AppDatabase.getInstance(application)
        }
    }


    @Binds
    abstract fun bindCacheRepository(roomRepository: RoomRepository): CacheRepository


}