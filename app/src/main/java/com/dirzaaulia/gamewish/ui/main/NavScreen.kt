package com.dirzaaulia.gamewish.ui.main

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
}