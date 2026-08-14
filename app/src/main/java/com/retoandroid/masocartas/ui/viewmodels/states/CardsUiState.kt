package com.retoandroid.masocartas.ui.viewmodels.states

import com.retoandroid.masocartas.domain.model.Card

data class CardsUiState(
    val cards: List<Card> = emptyList(),
    val favoriteIds: Set<Long> = emptySet(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val offset: Int = 0,
    val endReached: Boolean = false
)
