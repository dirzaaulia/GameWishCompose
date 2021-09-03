package com.dirzaaulia.gamewish.ui.home

import androidx.compose.runtime.Composable

/**
 * GameWishApp to show Splash Screen
 */
@Composable
fun OnBoarding(
    viewModel: HomeViewModel,
    navigateToGameDetails: (Long) -> Unit,
    upPress: () -> Unit
) {
    //TODO Need to add Google SignIn before go to Home
    Home(
        viewModel = viewModel,
        navigateToGameDetails = navigateToGameDetails,
        upPress = upPress
    )
}