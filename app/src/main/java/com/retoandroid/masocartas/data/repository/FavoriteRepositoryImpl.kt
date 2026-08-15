package com.retoandroid.masocartas.data.repository

import com.retoandroid.masocartas.data.local.dao.FavoriteDao
import com.retoandroid.masocartas.data.local.entity.FavoriteEntity
import com.retoandroid.masocartas.domain.model.FavoriteItem
import com.retoandroid.masocartas.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject



class FavoriteRepositoryImpl @Inject constructor(
    private val dao: FavoriteDao
) : FavoriteRepository {

    override fun getFavorites(): Flow<List<FavoriteItem>> =
        dao.getAllFavorites().map { list -> list.map { FavoriteItem(it.id, it.name, it.imageUrl) } }

    override fun isFavorite(id: Long): Flow<Boolean> = dao.isFavorite(id)

    override suspend fun toggleFavorite(item: FavoriteItem, isCurrentlyFavorite: Boolean) {
        if (isCurrentlyFavorite) dao.delete(item.id)
        else dao.insert(FavoriteEntity(item.id, item.name, item.imageUrl))
    }
}