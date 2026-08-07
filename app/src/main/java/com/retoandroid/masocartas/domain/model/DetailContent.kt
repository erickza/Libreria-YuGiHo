package com.retoandroid.masocartas.domain.model

data class DetailContent(
    val title: String,
    val imageUrl: String?,
    val sections: List<DetailSection>
)

data class DetailSection(
    val label: String,
    val values: List<String>
)
