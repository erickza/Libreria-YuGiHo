package com.retoandroid.masocartas.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.retoandroid.masocartas.domain.model.Card
import com.retoandroid.masocartas.domain.usecase.GetRandomHandUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RandomHandUiState(
    val cards: List<Card> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class RandomHandViewModel @Inject constructor(
    private val getRandomHandUseCase: GetRandomHandUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RandomHandUiState())
    val uiState: StateFlow<RandomHandUiState> = _uiState.asStateFlow()

    init {
        drawHand()
    }

    fun drawHand() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val hand = getRandomHandUseCase(size = 5)
                _uiState.value = _uiState.value.copy(cards = hand, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "No hay cartas guardadas para formar una mano sin conexión"
                )
            }
        }
    }
}