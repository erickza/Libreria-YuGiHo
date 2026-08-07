package com.retoandroid.masocartas.domain.model

data class Card(
    val id: Long,
    val name: String,
    val type: String,
    val desc: String,
    val race: String?,
    val archetype: String?,
    val atk: Int?,
    val def: Int?,
    val level: Int?,
    val imageUrl: String?,
    val sets: List<String>,
    val price: String?
)
