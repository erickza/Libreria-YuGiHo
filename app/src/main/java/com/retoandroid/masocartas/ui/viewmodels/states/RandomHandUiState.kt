package com.retoandroid.masocartas.ui.viewmodels.states

import com.retoandroid.masocartas.domain.model.Card

data class RandomHandUiState(
    val cards: List<Card> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
