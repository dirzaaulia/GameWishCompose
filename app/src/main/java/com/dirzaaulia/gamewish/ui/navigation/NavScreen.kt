package com.dirzaaulia.gamewish.ui.navigation

import com.dirzaaulia.gamewish.utils.Route

sealed class NavScreen(val route: String) {
    object Login : NavScreen(Route.LOGIN)

    object MyAnimeListLogin : NavScreen(Route.MYANIMELIST_LOGIN)

    object Home : NavScreen(Route.HOME)

    object Search : NavScreen(Route.SEARCH) {
        const val routeWithArgument: String = Route.SEARCH_ARGS
        const val argument0: String = Route.SEARCH_ARG_0
    }

    object GameDetails : NavScreen(Route.GAME_DETAILS) {
        const val routeWithArgument: String = Route.GAME_DETAILS_ARGS
        const val argument0: String = Route.GAME_DETAILS_ARG_0
    }

    object AnimeDetails : NavScreen(Route.ANIME_DETAILS) {
        const val routeWithArgument: String = Route.ANIME_DETAILS_ARGS
        const val argument0: String = Route.ANIME_DETAILS_ARG_0
        const val argument1: String = Route.ANIME_DETAILS_ARG_1
    }

    object MovieDetails: NavScreen(Route.MOVIE_DETAILS) {
        const val routeWithArgument: String = Route.MOVIE_DETAILS_ARGS
        const val argument0: String = Route.MOVIE_DETAILS_ARG_0
        const val argument1: String = Route.MOVIE_DETAILS_ARG_1
    }
}