package com.dirzaaulia.gamewish.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.dirzaaulia.gamewish.ui.home.Home
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.ui.main.login.Login

@Composable
fun StartApp(
    viewModel: HomeViewModel,
    navigateToGameDetails: (Long) -> Unit,
    navigateToAnimeDetails: (Long, String) -> Unit,
    navigateToMyAnimeListLogin: () -> Unit,
    navigateToSearch: (Int) -> Unit,
) {
    val userAuthId by viewModel.userAuthId.collectAsState()
    if (userAuthId == null) {
        Splash()
    } else {
        if (userAuthId!!.isBlank()) {
            Login(viewModel)
        } else {
            Home(
                viewModel = viewModel,
                navigateToGameDetails = navigateToGameDetails,
                navigateToAnimeDetails = navigateToAnimeDetails,
                navigateToMyAnimeListLogin = navigateToMyAnimeListLogin,
                navigateToSearch = navigateToSearch
            )
        }
    }
}