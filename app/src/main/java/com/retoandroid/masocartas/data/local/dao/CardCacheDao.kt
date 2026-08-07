package com.retoandroid.masocartas.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.retoandroid.masocartas.data.local.entity.CardCacheEntity

@Dao
interface CardCacheDao {

    @Query("SELECT * FROM card_cache LIMIT :limit OFFSET :offset")
    suspend fun getPage(limit: Int, offset: Int): List<CardCacheEntity>

    @Query("SELECT * FROM card_cache WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): CardCacheEntity?

    @Query("SELECT * FROM card_cache WHERE name LIKE '%' || :query || '%'")
    suspend fun search(query: String): List<CardCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cards: List<CardCacheEntity>)

    @Query("SELECT * FROM card_cache ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandom(limit: Int): List<CardCacheEntity>
}