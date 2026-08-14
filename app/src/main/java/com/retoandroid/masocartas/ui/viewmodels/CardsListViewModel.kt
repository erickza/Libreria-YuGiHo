package com.retoandroid.masocartas.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.retoandroid.masocartas.domain.model.Card
import com.retoandroid.masocartas.domain.model.FavoriteItem
import com.retoandroid.masocartas.domain.usecase.GetCardsUseCase
import com.retoandroid.masocartas.domain.usecase.GetFavoritesUseCase
import com.retoandroid.masocartas.domain.usecase.ToggleFavoriteUseCase
import com.retoandroid.masocartas.ui.viewmodels.states.CardsUiState
import com.retoandroid.masocartas.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardsListViewModel @Inject constructor(
    private val getCardsUseCase: GetCardsUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CardsUiState())
    val uiState: StateFlow<CardsUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadCards(reset = true)
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            getFavoritesUseCase().collect { favorites ->
                _uiState.value = _uiState.value.copy(favoriteIds = favorites.map { it.id }.toSet())
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(400)
            loadCards(reset = true)
        }
    }

    fun loadMore() {
        if (_uiState.value.isLoadingMore || _uiState.value.endReached) return
        loadCards(reset = false)
    }

    fun retry() = loadCards(reset = true)

    fun onToggleFavorite(card: Card) {
        viewModelScope.launch {
            val isFav = _uiState.value.favoriteIds.contains(card.id)
            toggleFavoriteUseCase(FavoriteItem(card.id, card.name, card.imageUrl), isFav)
        }
    }

    private fun loadCards(reset: Boolean) {
        viewModelScope.launch {
            val currentOffset = if (reset) 0 else _uiState.value.offset

            _uiState.value = _uiState.value.copy(
                isLoading = reset,
                isLoadingMore = !reset,
                error = null
            )

            try {
                val result = getCardsUseCase(currentOffset, _uiState.value.searchQuery)
                _uiState.value = _uiState.value.copy(
                    cards = if (reset) result else _uiState.value.cards + result,
                    offset = currentOffset + Constants.PAGE_SIZE,
                    isLoading = false,
                    isLoadingMore = false,
                    endReached = result.isEmpty() || result.size < Constants.PAGE_SIZE
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoadingMore = false,
                    error = e.localizedMessage ?: "Error al obtener cartas"
                )
            }
        }
    }
}