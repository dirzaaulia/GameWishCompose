package com.dirzaaulia.gamewish.ui.main.navigation

sealed class NavScreen(val route: String) {
    object Login : NavScreen("Login")

    object MyAnimeListLogin : NavScreen("MyAnimeListLogin")

    object Home : NavScreen("Home")

    object Search : NavScreen("Search") {
        const val routeWithArgument: String = "Search/{menuId}"
        const val argument0: String = "menuId"
    }

    object GameDetails : NavScreen("GameDetails") {
        const val routeWithArgument: String = "GameDetails/{gameId}"
        const val argument0: String = "gameId"
    }

    object AnimeDetails : NavScreen("AnimeDetails") {
        const val routeWithArgument: String = "AnimeDetails/{animeId}/{type}"
        const val argument0: String = "animeId"
        const val argument1: String = "type"
    }

    object MovieDetails: NavScreen("MovieDetails") {
        const val routeWithArgument: String = "MovieDetails/{movieId}/{type}"
        const val argument0: String = "movieId"
        const val argument1: String = "type"
    }
}