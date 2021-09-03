package com.dirzaaulia.gamewish.ui.main

import androidx.navigation.NavHostController

class MainActions(navController: NavHostController) {
    val navigateToGameDetails: (Long) -> Unit = { id: Long ->
        NavScreen.DetailsWishlist.apply {
            navController.navigate(routeWithArgument.replace("{$argument0}", id.toString()))
        }
    }
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}