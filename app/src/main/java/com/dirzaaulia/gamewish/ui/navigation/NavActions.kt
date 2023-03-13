package com.dirzaaulia.gamewish.ui.navigation

import androidx.navigation.NavHostController

class NavActions(navController: NavHostController) {
    val navigateToHome: () -> Unit = {
        NavScreen.Login.apply {
            navController.navigate(this.route)
        }
    }

    val navigateToMyAnimeListLogin: () -> Unit = {
        NavScreen.MyAnimeListLogin.apply {
            navController.navigate(this.route)
        }
    }

    val navigateToSearch: (Int) -> Unit = { menuId: Int ->
        NavScreen.Search.apply {
            navController.navigate(
                routeWithArgument.replace("{$argument0}", menuId.toString())
            )
        }
    }

    val navigateToAnimeDetails: (Long, String) -> Unit = {id: Long, type: String ->
        NavScreen.AnimeDetails.apply {
           var route = routeWithArgument.replace("{$argument0}", id.toString())
            route = route.replace("{$argument1}", type)

            navController.navigate(route)
        }
    }

    val navigateToGameDetails: (Long) -> Unit = { id: Long ->
        NavScreen.GameDetails.apply {
            navController.navigate(routeWithArgument.replace("{$argument0}", id.toString()))
        }
    }

    val navigateToMovieDetails: (Long, String) -> Unit = { id: Long, type: String ->
        NavScreen.MovieDetails.apply {
            var route = routeWithArgument.replace("{$argument0}", id.toString())
            route = route.replace("{$argument1}", type)

            navController.navigate(route)
        }
    }

    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}