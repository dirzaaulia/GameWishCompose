package com.dirzaaulia.gamewish.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.dirzaaulia.gamewish.ui.home.Home
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.ui.login.Login

@Composable
fun StartApp(
    viewModel: HomeViewModel,
    navigateToGameDetails: (Long) -> Unit,
    navigateToAnimeDetails: (Long, String) -> Unit,
    navigateToMovieDetails: (Long, String) -> Unit,
    navigateToMyAnimeListLogin: () -> Unit,
    navigateToSearch: (Int) -> Unit,
) {
    val userId by viewModel.userAuthId.collectAsState()
    if (userId?.isBlank() == true) {
        Login(viewModel)
    } else {
        Home(
            viewModel = viewModel,
            navigateToGameDetails = navigateToGameDetails,
            navigateToAnimeDetails = navigateToAnimeDetails,
            navigateToMovieDetails = navigateToMovieDetails,
            navigateToMyAnimeListLogin = navigateToMyAnimeListLogin,
            navigateToSearch = navigateToSearch
        )
    }
}