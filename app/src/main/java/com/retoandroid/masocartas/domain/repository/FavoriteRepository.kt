package com.retoandroid.masocartas.domain.repository

import com.retoandroid.masocartas.domain.model.FavoriteItem
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getFavorites(): Flow<List<FavoriteItem>>
    fun isFavorite(id: Long): Flow<Boolean>
    suspend fun toggleFavorite(item: FavoriteItem, isCurrentlyFavorite: Boolean)
}