package com.retoandroid.masocartas.data.remote.dto

data class CardResponseDto(
    val data: List<CardDto>
)

data class CardDto(
    val id: Long,
    val name: String,
    val type: String,
    val desc: String,
    val race: String?,
    val archetype: String?,
    val atk: Int?,
    val def: Int?,
    val level: Int?,
    val card_images: List<CardImageDto>,
    val card_sets: List<CardSetDto>?,
    val card_prices: List<CardPriceDto>?
)

data class CardImageDto(
    val id: Long,
    val image_url: String,
    val image_url_small: String,
    val image_url_cropped: String
)

data class CardSetDto(
    val set_name: String,
    val set_code: String,
    val set_rarity: String
)

data class CardPriceDto(
    val cardmarket_price: String?,
    val tcgplayer_price: String?
)
