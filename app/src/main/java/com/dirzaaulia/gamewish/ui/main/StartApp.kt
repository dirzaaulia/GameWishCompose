package com.dirzaaulia.gamewish.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.dirzaaulia.gamewish.extension.isError
import com.dirzaaulia.gamewish.extension.isSucceeded
import com.dirzaaulia.gamewish.ui.common.ErrorConnect
import com.dirzaaulia.gamewish.ui.home.Home
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.ui.main.login.Login

@Composable
fun StartApp(
    viewModel: HomeViewModel,
    navigateToGameDetails: (Long) -> Unit,
) {
    val userAuthId by viewModel.userAuthId.collectAsState(null)
    when {
        userAuthId.isSucceeded -> {
            Home(
                viewModel = viewModel,
                navigateToGameDetails = navigateToGameDetails)
        }
        userAuthId.isError -> {
            Login {
                viewModel.getUserAuthStatus()
            }
        }
        else -> Splash()
    }
}