package com.retoandroid.masocartas.domain.usecase

import com.retoandroid.masocartas.data.repository.FavoriteRepository
import com.retoandroid.masocartas.domain.model.FavoriteItem
import com.retoandroid.masocartas.domain.repository.CardRepository
import javax.inject.Inject

class GetCardsUseCase @Inject constructor(private val repo: CardRepository) {
    suspend operator fun invoke(offset: Int, query: String?) =
        repo.getCards(offset, query?.takeIf { it.isNotBlank() })
}

class GetCardByIdUseCase @Inject constructor(private val repo: CardRepository) {
    suspend operator fun invoke(id: Long) = repo.getCardById(id)
}

class GetFavoritesUseCase @Inject constructor(private val repo: FavoriteRepository) {
    operator fun invoke() = repo.getFavorites()
}

class ObserveIsFavoriteUseCase @Inject constructor(private val repo: FavoriteRepository) {
    operator fun invoke(id: Long) = repo.isFavorite(id)
}

class ToggleFavoriteUseCase @Inject constructor(private val repo: FavoriteRepository) {
    suspend operator fun invoke(item: FavoriteItem, isFav: Boolean) = repo.toggleFavorite(item, isFav)
}