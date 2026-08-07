package com.retoandroid.masocartas.data.di

import android.content.Context
import androidx.room.Room
import com.retoandroid.masocartas.data.local.AppDatabase
import com.retoandroid.masocartas.data.local.dao.CardCacheDao
import com.retoandroid.masocartas.data.local.dao.FavoriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "yugioh_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCardCacheDao(db: AppDatabase): CardCacheDao = db.cardCacheDao()

    @Provides
    @Singleton
    fun provideFavoriteDao(db: AppDatabase): FavoriteDao = db.favoriteDao()
}