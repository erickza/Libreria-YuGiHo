package com.retoandroid.masocartas.data.mapper

import com.retoandroid.masocartas.data.remote.dto.CardDto
import com.retoandroid.masocartas.domain.model.Card

fun CardDto.toDomain(): Card {
    return Card(
        id = id,
        name = name,
        type = type,
        desc = desc,
        race = race,
        archetype = archetype,
        atk = atk,
        def = def,
        level = level,
        imageUrl = card_images.firstOrNull()?.image_url,
        sets = card_sets?.map { "${it.set_name} (${it.set_code}) - ${it.set_rarity}" } ?: emptyList(),
        price = card_prices?.firstOrNull()?.cardmarket_price?.let { "$$it USD" }
    )
}