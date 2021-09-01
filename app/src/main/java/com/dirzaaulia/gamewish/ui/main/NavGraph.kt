package com.dirzaaulia.gamewish.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.dirzaaulia.gamewish.ui.home.OnBoarding


@Composable
fun NavGraph(navController: NavHostController) {
    val actions = remember(navController) { MainActions(navController) }

    NavHost(
        navController = navController,
        startDestination = NavScreen.Home.route
    ) {
        composable(NavScreen.Home.route) {
            OnBoarding(
                viewModel = hiltViewModel(),
                navigateToDetailsWishlist = actions.navigateToDetailsWishlist
            )
        }
        composable(
            route = NavScreen.DetailsWishlist.routeWithArgument,
            arguments = listOf(navArgument(NavScreen.DetailsWishlist.argument0) {
                type = NavType.LongType } )
        ) { backStackEntry ->  
            backStackEntry.arguments.let {

            }
        }
    }
}
