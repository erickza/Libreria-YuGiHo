package com.retoandroid.masocartas.ui.viewmodels.states

import com.retoandroid.masocartas.domain.model.Card
import com.retoandroid.masocartas.domain.model.DetailContent

data class CardDetailUiState(
    val card: Card? = null,
    val content: DetailContent? = null,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)