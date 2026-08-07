package com.retoandroid.masocartas.ui.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.retoandroid.masocartas.ui.screens.CardDetailScreen
import com.retoandroid.masocartas.ui.screens.CardsListScreen
import com.retoandroid.masocartas.ui.screens.FavoritesScreen
import com.retoandroid.masocartas.ui.screens.HomeScreen
import com.retoandroid.masocartas.ui.screens.RandomHandScreen

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.CardsList.route) { CardsListScreen(navController) }
        composable(
            route = Screen.CardDetail.route,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { CardDetailScreen(navController) }
        composable(Screen.Favorites.route) { FavoritesScreen(navController) }
        composable(Screen.RandomHand.route) {
            RandomHandScreen(navController = navController)
        }
    }
}
