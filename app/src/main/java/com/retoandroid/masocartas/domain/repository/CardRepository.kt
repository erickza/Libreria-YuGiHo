package com.retoandroid.masocartas.domain.repository

import com.retoandroid.masocartas.domain.model.Card

interface CardRepository {
    suspend fun getCards(offset: Int, query: String?): List<Card>
    suspend fun getCardById(id: Long): Card
    suspend fun getRandomHand(size: Int): List<Card>
}