package com.retoandroid.masocartas.data.mapper

import com.retoandroid.masocartas.data.local.entity.CardCacheEntity
import com.retoandroid.masocartas.data.local.entity.FavoriteEntity
import com.retoandroid.masocartas.domain.model.Card

private fun Card.toCacheEntity() = CardCacheEntity(
    id = id, name = name, imageUrl = imageUrl, type = type, desc = desc,
    race = race, archetype = archetype, atk = atk, def = def, level = level
)

fun CardCacheEntity.toDomain() = Card(
    id = id, name = name, type = type, desc = desc, race = race,
    archetype = archetype, atk = atk, def = def, level = level,
    imageUrl = imageUrl, sets = emptyList(), price = null
)

fun FavoriteEntity.toDomain() = Card(
    id = id, name = name, type = "Sin conexión", desc = "Información completa no disponible sin internet",
    race = null, archetype = null, atk = null, def = null, level = null,
    imageUrl = imageUrl, sets = emptyList(), price = null
)