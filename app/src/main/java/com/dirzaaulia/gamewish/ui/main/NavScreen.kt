package com.dirzaaulia.gamewish.ui.main

sealed class NavScreen (val route : String) {
    object Home : NavScreen("Home")
    object DetailsWishlist : NavScreen("DetailsWishlist") {
        const val routeWithArgument : String = "DetailsWishlist/{wishlistId}"
        const val argument0 : String = "wishlistId"
    }
}