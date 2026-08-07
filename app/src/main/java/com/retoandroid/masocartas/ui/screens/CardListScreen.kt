package com.retoandroid.masocartas.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.retoandroid.masocartas.ui.components.AppSearchBar
import com.retoandroid.masocartas.ui.components.AppTopBar
import com.retoandroid.masocartas.ui.components.CardGridItem
import com.retoandroid.masocartas.ui.components.ErrorView
import com.retoandroid.masocartas.ui.components.LoadingView
import com.retoandroid.masocartas.ui.navegacion.Screen
import com.retoandroid.masocartas.ui.viewmodels.CardsListViewModel

@Composable
fun CardsListScreen(
    navController: NavHostController,
    viewModel: CardsListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val gridState = rememberLazyGridState()

    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisible ->
                if (lastVisible != null && lastVisible >= uiState.cards.size - 4) {
                    viewModel.loadMore()
                }
            }
    }

    Scaffold(
        topBar = { AppTopBar(title = "Cartas Yu-Gi-Oh", onBackClick = { navController.popBackStack() }) }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            AppSearchBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                placeholder = "Buscar carta..."
            )

            if (uiState.cards.isNotEmpty()) {
                Text(
                    text = "${uiState.cards.size} cartas encontradas",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            when {
                uiState.isLoading -> LoadingView(modifier = Modifier.weight(1f))
                uiState.error != null && uiState.cards.isEmpty() -> ErrorView(
                    message = uiState.error ?: "",
                    onRetry = viewModel::retry,
                    modifier = Modifier.weight(1f)
                )
                uiState.cards.isEmpty() -> EmptySearchView(modifier = Modifier.weight(1f))
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        state = gridState,
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(uiState.cards, key = { it.id }) { card ->
                            CardGridItem(
                                title = card.name,
                                imageUrl = card.imageUrl,
                                isFavorite = uiState.favoriteIds.contains(card.id),
                                onFavoriteClick = { viewModel.onToggleFavorite(card) },
                                onClick = { navController.navigate(Screen.CardDetail.createRoute(card.id)) }
                            )
                        }
                        if (uiState.isLoadingMore) {
                            item(span = { GridItemSpan(2) }) {
                                Box(
                                    Modifier.fillMaxWidth().padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(28.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptySearchView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.SearchOff,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(56.dp)
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "No se encontraron cartas",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}