package com.retoandroid.masocartas.data.repository

import android.util.Log
import com.retoandroid.masocartas.data.local.dao.CardCacheDao
import com.retoandroid.masocartas.data.local.dao.FavoriteDao
import com.retoandroid.masocartas.data.local.entity.CardCacheEntity
import com.retoandroid.masocartas.data.local.entity.FavoriteEntity
import com.retoandroid.masocartas.data.local.entity.toCacheEntity
import com.retoandroid.masocartas.data.mapper.toDomain
import com.retoandroid.masocartas.data.remote.api.ApiYiGiOh
import com.retoandroid.masocartas.domain.model.Card
import com.retoandroid.masocartas.domain.repository.CardRepository
import com.retoandroid.masocartas.util.Constants
import java.io.IOException
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(
    private val api: ApiYiGiOh,
    private val cacheDao: CardCacheDao,
    private val favoriteDao: FavoriteDao
) : CardRepository {

    override suspend fun getCards(offset: Int, query: String?): List<Card> {
        return try {
            val response = api.getCards(offset = offset, fname = query)
            val cards = response.data.map { it.toDomain() }
            if (query.isNullOrBlank()) {
                cacheDao.insertAll(cards.map { it.toCacheEntity() })
            }
            cards
        } catch (e: IOException) {
            val cached = if (query.isNullOrBlank()) {
                cacheDao.getPage(Constants.PAGE_SIZE, offset)
            } else {
                cacheDao.search(query)
            }
            cached.map { it.toDomain() }
        }
    }

    override suspend fun getCardById(id: Long): Card {
        return try {
            val card = api.getCardById(id).data.first().toDomain()
            cacheDao.insertAll(listOf(card.toCacheEntity()))
            card
        } catch (e: IOException) {
            cacheDao.getById(id)?.toDomain()
                ?: favoriteDao.getById(id)?.toDomain()
                ?: throw e
        }
    }

    override suspend fun getRandomHand(size: Int): List<Card> {
        return try {
            val maxOffset = 12000
            val randomOffset = (0..maxOffset).random()
            val response = api.getCards(num = size * 3, offset = randomOffset)
            response.data.map { it.toDomain() }.shuffled().take(size)
        } catch (e: IOException) {
            val cached = cacheDao.getRandom(size)
            if (cached.isEmpty()) throw e
            cached.map { it.toDomain() }
        }
    }

    private fun CardCacheEntity.toDomain() = Card(
        id = id, name = name, type = type, desc = desc, race = race,
        archetype = archetype, atk = atk, def = def, level = level,
        imageUrl = imageUrl, sets = emptyList(), price = null
    )

    private fun FavoriteEntity.toDomain() = Card(
        id = id, name = name, type = "Sin conexión", desc = "Información completa no disponible sin internet",
        race = null, archetype = null, atk = null, def = null, level = null,
        imageUrl = imageUrl, sets = emptyList(), price = null
    )
}