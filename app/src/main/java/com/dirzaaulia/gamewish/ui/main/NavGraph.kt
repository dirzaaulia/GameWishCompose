package com.dirzaaulia.gamewish.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.dirzaaulia.gamewish.ui.details.GameDetails
import com.google.accompanist.insets.ProvideWindowInsets

@Composable
fun NavGraph(navController: NavHostController) {
    val actions = remember(navController) { MainActions(navController) }

    ProvideWindowInsets {
        NavHost(
            navController = navController,
            startDestination = NavScreen.Home.route
        ) {
            composable(NavScreen.Home.route) {
                StartApp(
                    viewModel = hiltViewModel(),
                    navigateToGameDetails = actions.navigateToGameDetails,
                )
            }
            composable(
                route = NavScreen.GameDetails.routeWithArgument,
                arguments = listOf(navArgument(NavScreen.GameDetails.argument0) {
                    type = NavType.LongType
                })
            ) { backStackEntry ->
                backStackEntry.arguments.let { bundle ->
                    bundle?.let { argument ->
                        GameDetails(
                            gameId = argument.getLong(NavScreen.GameDetails.argument0),
                            viewModel = hiltViewModel(),
                            upPress = actions.upPress
                        )
                    }
                }
            }
        }
    }
}
