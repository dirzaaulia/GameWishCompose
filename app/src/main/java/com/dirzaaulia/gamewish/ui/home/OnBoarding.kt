package com.dirzaaulia.gamewish.ui.home

import androidx.compose.runtime.Composable

/**
 * GameWishApp to show Splash Screen
 */
@Composable
fun OnBoarding(
    viewModel: HomeViewModel,
    navigateToDetailsWishlist: (Long) -> Unit
) {
    //TODO Need to add Google SignIn before go to Home
    Home(
        viewModel = viewModel,
        navigateToWishlistDetails = navigateToDetailsWishlist
    )
}