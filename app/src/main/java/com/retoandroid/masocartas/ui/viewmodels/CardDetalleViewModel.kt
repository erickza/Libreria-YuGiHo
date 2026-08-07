package com.retoandroid.masocartas.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.retoandroid.masocartas.domain.model.Card
import com.retoandroid.masocartas.domain.model.DetailContent
import com.retoandroid.masocartas.domain.model.DetailSection
import com.retoandroid.masocartas.domain.model.FavoriteItem
import com.retoandroid.masocartas.domain.usecase.GetCardByIdUseCase
import com.retoandroid.masocartas.domain.usecase.ObserveIsFavoriteUseCase
import com.retoandroid.masocartas.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CardDetailUiState(
    val card: Card? = null,
    val content: DetailContent? = null,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CardDetailViewModel @Inject constructor(
    private val getCardByIdUseCase: GetCardByIdUseCase,
    private val observeIsFavoriteUseCase: ObserveIsFavoriteUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val cardId: Long = checkNotNull(savedStateHandle["id"])

    private val _uiState = MutableStateFlow(CardDetailUiState())
    val uiState: StateFlow<CardDetailUiState> = _uiState.asStateFlow()

    init {
        loadDetail()
        observeFavoriteStatus()
    }

    private fun observeFavoriteStatus() {
        viewModelScope.launch {
            observeIsFavoriteUseCase(cardId).collect { isFav ->
                _uiState.value = _uiState.value.copy(isFavorite = isFav)
            }
        }
    }

    fun onToggleFavorite() {
        val card = _uiState.value.card ?: return
        viewModelScope.launch {
            toggleFavoriteUseCase(FavoriteItem(card.id, card.name, card.imageUrl), _uiState.value.isFavorite)
        }
    }

    fun retry() = loadDetail()

    private fun loadDetail() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val card = getCardByIdUseCase(cardId)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    card = card,
                    content = DetailContent(
                        title = card.name,
                        imageUrl = card.imageUrl,
                        sections = listOfNotNull(
                            DetailSection("Tipo", listOf(card.type)),
                            card.race?.let { DetailSection("Raza/Categoría", listOf(it)) },
                            card.archetype?.let { DetailSection("Arquetipo", listOf(it)) },
                            if (card.atk != null || card.def != null)
                                DetailSection("Stats", listOfNotNull(
                                    card.atk?.let { "ATK: $it" },
                                    card.def?.let { "DEF: $it" }
                                ))
                            else null,
                            DetailSection("Descripción", listOf(card.desc)),
                            if (card.sets.isNotEmpty()) DetailSection("Sets", card.sets) else null
                        )
                    )
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "Error al obtener el detalle"
                )
            }
        }
    }
}