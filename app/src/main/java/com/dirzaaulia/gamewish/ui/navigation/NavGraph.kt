package com.dirzaaulia.gamewish.ui.navigation

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
import androidx.navigation.navArgument
import com.dirzaaulia.gamewish.ui.common.MyAnimeListWebViewClient
import com.dirzaaulia.gamewish.ui.details.anime.AnimeDetails
import com.dirzaaulia.gamewish.ui.details.game.GameDetails
import com.dirzaaulia.gamewish.ui.details.movie.MovieDetails
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.ui.main.StartApp
import com.dirzaaulia.gamewish.ui.search.Search
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@Composable
fun NavGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel
) {
    val actions = remember(navController) { NavActions(navController) }

    AnimatedNavHost(
        navController = navController,
        startDestination = NavScreen.Home.route
    ) {
        composable(NavScreen.Home.route) {
            StartApp(
                viewModel = homeViewModel,
                navigateToGameDetails = actions.navigateToGameDetails,
                navigateToAnimeDetails = actions.navigateToAnimeDetails,
                navigateToMovieDetails = actions.navigateToMovieDetails,
                navigateToMyAnimeListLogin = actions.navigateToMyAnimeListLogin,
                navigateToSearch = actions.navigateToSearch
            )
        }
        composable(
            route = NavScreen.Search.routeWithArgument,
            arguments = listOf(navArgument(NavScreen.Search.argument0) {
                type = NavType.IntType
            }),
            enterTransition = {
                fadeIn(animationSpec = tween(700))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(700))
            }
        ) { backStackEntry ->
            backStackEntry.arguments.let { bundle ->
                bundle?.let { argument ->
                    Search(
                        homeViewModel = homeViewModel,
                        menuId = argument.getInt(NavScreen.Search.argument0),
                        navigateToGameDetails = actions.navigateToGameDetails,
                        navigateToAnimeDetails = actions.navigateToAnimeDetails,
                        navigateToMovieDetails = actions.navigateToMovieDetails,
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
            enterTransition = {
                expandIn(animationSpec = tween(700))
            },
            exitTransition = {
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
        composable(
            route = NavScreen.AnimeDetails.routeWithArgument,
            arguments = listOf(
                navArgument(NavScreen.AnimeDetails.argument0) {
                    type = NavType.LongType
                },
                navArgument(NavScreen.AnimeDetails.argument1) {
                    type = NavType.StringType
                }
            ),
            enterTransition = {
                expandIn(animationSpec = tween(700))
            },
            exitTransition = {
                shrinkOut(animationSpec = tween(700))
            }
        ) { backStackEntry ->
            backStackEntry.arguments.let { bundle ->
                bundle?.let { argument ->
                    argument.getString(NavScreen.AnimeDetails.argument1)?.let {
                        AnimeDetails(
                            animeId = argument.getLong(NavScreen.AnimeDetails.argument0),
                            type = it,
                            viewModel = hiltViewModel(),
                            navigateToAnimeDetails = actions.navigateToAnimeDetails,
                            upPress = actions.upPress
                        )
                    }
                }
            }
        }
        composable(
            route = NavScreen.MovieDetails.routeWithArgument,
            arguments = listOf(
                navArgument(NavScreen.MovieDetails.argument0) {
                    type = NavType.LongType
                },
                navArgument(NavScreen.MovieDetails.argument1) {
                    type = NavType.StringType
                }
            ),
            enterTransition = {
                expandIn(animationSpec = tween(700))
            },
            exitTransition = {
                shrinkOut(animationSpec = tween(700))
            }
        ) { backStackEntry ->
            backStackEntry.arguments.let { bundle ->
                bundle?.let { argument ->
                    argument.getString(NavScreen.MovieDetails.argument1)?.let {
                        MovieDetails(
                            upPress = actions.upPress,
                            movieId = argument.getLong(NavScreen.MovieDetails.argument0),
                            type = it,
                            navigateToMovieDetails = actions.navigateToMovieDetails
                        )
                    }
                }
            }
        }
        composable(NavScreen.MyAnimeListLogin.route) {
            MyAnimeListWebViewClient(
                from = 1,
                viewModel = homeViewModel,
                upPress = actions.upPress
            )
        }
    }
}
