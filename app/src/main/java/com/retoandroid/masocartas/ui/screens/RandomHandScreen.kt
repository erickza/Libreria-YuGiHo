package com.retoandroid.masocartas.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.retoandroid.masocartas.ui.components.AppTopBar
import com.retoandroid.masocartas.ui.components.CardGridItem
import com.retoandroid.masocartas.ui.components.ErrorView
import com.retoandroid.masocartas.ui.components.LoadingView
import com.retoandroid.masocartas.ui.navegacion.Screen
import com.retoandroid.masocartas.ui.viewmodels.RandomHandViewModel
import kotlinx.coroutines.delay

@Composable
fun RandomHandScreen(
    navController: NavHostController,
    viewModel: RandomHandViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { AppTopBar(title = "Tu Mano", onBackClick = { navController.popBackStack() }) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.drawHand() },
                icon = { Icon(Icons.Default.Refresh, contentDescription = null) },
                text = { Text("Robar de nuevo") },
                containerColor = MaterialTheme.colorScheme.tertiary
            )
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> LoadingView(modifier = Modifier.padding(innerPadding))
            uiState.error != null -> ErrorView(
                message = uiState.error ?: "",
                onRetry = viewModel::drawHand,
                modifier = Modifier.padding(innerPadding)
            )
            else -> {
                Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                    HandHeader(cardCount = uiState.cards.size)

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(12.dp, 4.dp, 12.dp, 88.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(uiState.cards, key = { _, card -> card.id }) { index, card ->
                            AnimatedHandCard(index = index) {
                                CardGridItem(
                                    title = card.name,
                                    imageUrl = card.imageUrl,
                                    isFavorite = false,
                                    onFavoriteClick = { },
                                    onClick = { navController.navigate(Screen.CardDetail.createRoute(card.id)) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HandHeader(cardCount: Int) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Style,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "Tienes $cardCount ${if (cardCount == 1) "carta" else "cartas"} en mano",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun AnimatedHandCard(index: Int, content: @Composable () -> Unit) {
    var visible by remember(index) { mutableStateOf(false) }

    LaunchedEffect(index) {
        delay(index * 60L)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(250)) + slideInVertically(
            initialOffsetY = { it / 3 },
            animationSpec = tween(300)
        )
    ) {
        content()
    }
}