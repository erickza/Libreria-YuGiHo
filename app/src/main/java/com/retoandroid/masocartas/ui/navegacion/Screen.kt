package com.retoandroid.masocartas.ui.navegacion

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CardsList : Screen("cards_list")
    object CardDetail : Screen("card_detail/{id}") {
        fun createRoute(id: Long) = "card_detail/$id"
    }
    object Favorites : Screen("favorites")
    object RandomHand : Screen("random_hand")
}