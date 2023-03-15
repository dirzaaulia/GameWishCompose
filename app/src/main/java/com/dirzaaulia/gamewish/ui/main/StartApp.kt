package com.dirzaaulia.gamewish.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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

@Composable
@Preview
fun TestPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "Test"
        )
    }
}