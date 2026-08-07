package com.retoandroid.masocartas.data.di

import com.retoandroid.masocartas.data.repository.CardRepositoryImpl
import com.retoandroid.masocartas.data.repository.FavoriteRepository
import com.retoandroid.masocartas.data.repository.FavoriteRepositoryImpl
import com.retoandroid.masocartas.domain.repository.CardRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCardRepository(impl: CardRepositoryImpl): CardRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository
}