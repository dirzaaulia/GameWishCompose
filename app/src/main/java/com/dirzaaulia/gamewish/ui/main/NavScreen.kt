package com.dirzaaulia.gamewish.ui.main

sealed class NavScreen(val route: String) {
    object Home : NavScreen("Home")
    object GameDetails : NavScreen("GameDetails") {
        const val routeWithArgument: String = "GameDetails/{gameId}"
        const val argument0: String = "gameId"
    }
}