package com.example.kmitlcompanion.di

import com.example.kmitlcompanion.data.DataRepository
import com.example.kmitlcompanion.domain.repository.DomainRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class DataModule {
    @Binds
    abstract fun bindDataRepository(dataRepository: DataRepository): DomainRepository
}