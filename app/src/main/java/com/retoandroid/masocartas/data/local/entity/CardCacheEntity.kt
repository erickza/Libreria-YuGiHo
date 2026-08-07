package com.retoandroid.masocartas.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.retoandroid.masocartas.domain.model.Card

@Entity(tableName = "card_cache")
data class CardCacheEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val imageUrl: String?,
    val type: String,
    val desc: String,
    val race: String?,
    val archetype: String?,
    val atk: Int?,
    val def: Int?,
    val level: Int?
)

fun Card.toCacheEntity() = CardCacheEntity(
    id = id,
    name = name,
    imageUrl = imageUrl,
    type = type,
    desc = desc,
    race = race,
    archetype = archetype,
    atk = atk,
    def = def,
    level = level
)
