package com.retoandroid.masocartas.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.retoandroid.masocartas.data.local.dao.CardCacheDao
import com.retoandroid.masocartas.data.local.dao.FavoriteDao
import com.retoandroid.masocartas.data.local.entity.CardCacheEntity
import com.retoandroid.masocartas.data.local.entity.FavoriteEntity

@Database(
    entities = [CardCacheEntity::class, FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardCacheDao(): CardCacheDao
    abstract fun favoriteDao(): FavoriteDao
}