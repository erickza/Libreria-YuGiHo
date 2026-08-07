package com.retoandroid.masocartas.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.retoandroid.masocartas.domain.model.DetailContent
import com.retoandroid.masocartas.ui.components.AppAsyncImage
import com.retoandroid.masocartas.ui.components.AppTopBar
import com.retoandroid.masocartas.ui.components.ErrorView
import com.retoandroid.masocartas.ui.components.FavoriteStar
import com.retoandroid.masocartas.ui.components.LoadingView

@Composable
fun DetailScreen(
    content: DetailContent?,
    isFavorite: Boolean,
    isLoading: Boolean,
    error: String?,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Scaffold(
        topBar = { AppTopBar(title = content?.title ?: "Detalle", onBackClick = onBackClick) }
    ) { innerPadding ->
        when {
            isLoading -> LoadingView(modifier = Modifier.padding(innerPadding))
            error != null -> ErrorView(message = error, onRetry = onRetry, modifier = Modifier.padding(innerPadding))
            content != null -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    CardHero(
                        title = content.title,
                        imageUrl = content.imageUrl,
                        isFavorite = isFavorite,
                        onToggleFavorite = onToggleFavorite
                    )

                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = content.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(20.dp))

                        content.sections.forEach { section ->
                            if (section.values.isNotEmpty()) {
                                CardSectionPanel(label = section.label, values = section.values)
                                Spacer(Modifier.height(14.dp))
                            }
                        }
                    }
                }
            }
            else -> ErrorView(
                message = "No se encontró información",
                onRetry = onRetry,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun CardHero(
    title: String,
    imageUrl: String?,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .padding(top = 24.dp, bottom = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(260.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.35f),
                            Color.Transparent
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .width(220.dp)
                .aspectRatio(0.7f)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = MaterialTheme.colorScheme.tertiary,
                    spotColor = MaterialTheme.colorScheme.tertiary
                )
                .clip(RoundedCornerShape(16.dp))
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.tertiary,
                            MaterialTheme.colorScheme.primary
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            AppAsyncImage(
                imageUrl = imageUrl,
                contentDescription = title,
                modifier = Modifier.fillMaxSize()
            )

            FavoriteStar(
                isFavorite = isFavorite,
                onClick = onToggleFavorite,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CardSectionPanel(label: String, values: List<String>, modifier: Modifier = Modifier) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.sp
            )
            Spacer(Modifier.height(8.dp))


            val isShortValues = values.all { it.length <= 20 }

            if (isShortValues && values.size > 1) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    values.forEach { value ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = value,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            } else {
                values.forEach { value ->
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}