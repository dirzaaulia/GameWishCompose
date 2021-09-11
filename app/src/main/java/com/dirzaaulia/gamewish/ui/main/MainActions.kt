package com.dirzaaulia.gamewish.ui.main

import androidx.navigation.NavHostController

class MainActions(navController: NavHostController) {
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

    val navigateToGameDetails: (Long) -> Unit = { id: Long ->
        NavScreen.GameDetails.apply {
            navController.navigate(routeWithArgument.replace("{$argument0}", id.toString()))
        }
    }
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}