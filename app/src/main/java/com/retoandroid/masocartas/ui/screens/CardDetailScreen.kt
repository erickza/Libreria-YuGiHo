package com.retoandroid.masocartas.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.retoandroid.masocartas.ui.viewmodels.CardDetailViewModel

@Composable
fun CardDetailScreen(
    navController: NavHostController,
    viewModel: CardDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    DetailScreen(
        content = uiState.content,
        isFavorite = uiState.isFavorite,
        isLoading = uiState.isLoading,
        error = uiState.error,
        onBackClick = { navController.popBackStack() },
        onRetry = viewModel::retry,
        onToggleFavorite = viewModel::onToggleFavorite
    )
}