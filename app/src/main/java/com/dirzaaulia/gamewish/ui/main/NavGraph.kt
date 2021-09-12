package com.dirzaaulia.gamewish.ui.main

import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.navArgument
import com.dirzaaulia.gamewish.ui.common.WebViewMyAnimeList
import com.dirzaaulia.gamewish.ui.details.GameDetails
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.ui.search.Search
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@Composable
fun NavGraph(navController: NavHostController) {
    val actions = remember(navController) { MainActions(navController) }
    val homeViewModel: HomeViewModel = hiltViewModel()

    ProvideWindowInsets {
        AnimatedNavHost(
            navController = navController,
            startDestination = NavScreen.Home.route
        ) {
            composable(NavScreen.Home.route) {
                StartApp(
                    viewModel = homeViewModel,
                    navigateToGameDetails = actions.navigateToGameDetails,
                    navigateToMyAnimeListLogin = actions.navigateToMyAnimeListLogin,
                    navigateToSearch = actions.navigateToSearch
                )
            }
            composable(
                route = NavScreen.Search.routeWithArgument,
                arguments = listOf(navArgument(NavScreen.Search.argument0) {
                    type = NavType.IntType
                }),
                enterTransition = { _, _ ->
                    fadeIn(animationSpec = tween(700))
                },
                exitTransition = { _, _ ->
                    fadeOut(animationSpec = tween(700))
                }
            ) { backStackEntry ->
                backStackEntry.arguments.let { bundle ->
                    bundle?.let { argument ->
                        Search(
                            menuId = argument.getInt(NavScreen.Search.argument0),
                            navigateToGameDetails = actions.navigateToGameDetails,
                            upPress = actions.upPress
                        )
                    }
                }
            }
            composable(
                route = NavScreen.GameDetails.routeWithArgument,
                arguments = listOf(navArgument(NavScreen.GameDetails.argument0) {
                    type = NavType.LongType
                }),
                enterTransition = {  _, _ ->
                    expandIn(animationSpec = tween(700))
                },
                exitTransition = { _, _ ->
                    shrinkOut(animationSpec = tween(700))
                }
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
            composable(NavScreen.MyAnimeListLogin.route) {
                WebViewMyAnimeList(
                    viewModel = homeViewModel,
                    upPress = actions.upPress
                )
            }
        }
    }
}
