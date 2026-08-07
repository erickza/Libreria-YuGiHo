package com.retoandroid.masocartas.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.retoandroid.masocartas.domain.model.FavoriteItem
import com.retoandroid.masocartas.domain.usecase.GetFavoritesUseCase
import com.retoandroid.masocartas.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    val favorites: StateFlow<List<FavoriteItem>> =
        getFavoritesUseCase().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun removeFavorite(item: FavoriteItem) {
        viewModelScope.launch { toggleFavoriteUseCase(item, isFav = true) }
    }
}

