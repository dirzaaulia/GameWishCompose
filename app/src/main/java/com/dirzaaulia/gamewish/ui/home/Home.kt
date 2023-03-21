package com.dirzaaulia.gamewish.ui.home

import HomeBottomBar
import HomeBottomNavMenu
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.primarySurface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dirzaaulia.gamewish.data.model.cheapshark.Deals
import com.dirzaaulia.gamewish.ui.home.about.About
import com.dirzaaulia.gamewish.ui.home.deals.Deals
import com.dirzaaulia.gamewish.ui.home.wishlist.Wishlist
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.isError
import kotlinx.coroutines.launch

@Composable
fun Home(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToGameDetails: (Long) -> Unit,
    navigateToAnimeDetails: (Long, String) -> Unit,
    navigateToMovieDetails: (Long, String) -> Unit,
    navigateToMyAnimeListLogin: () -> Unit,
    navigateToSearch: (Int) -> Unit
) {
    val menu = HomeBottomNavMenu.values()
    val menuId: Int by viewModel.selectedBottomNav.collectAsState(initial = OtherConstant.ZERO)
    val lazyListStateDeals = rememberLazyListState()
    val lazyDeals: LazyPagingItems<Deals> = viewModel.deals.collectAsLazyPagingItems()
    val googleProfileImage by viewModel.googleProfileImage.collectAsState()
    val googleUsername by viewModel.googleUsername.collectAsState()
    val myAnimeListUserResult by viewModel.myAnimeListUserResult.collectAsState(null)
    val myAnimeListUser by viewModel.myAnimeListUser.collectAsState()
    val myAnimeListTokenResponse by viewModel.myAnimeListTokenResult.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        modifier = Modifier.navigationBarsPadding(),
        backgroundColor = MaterialTheme.colors.primarySurface,
        bottomBar = {
            HomeBottomBar(menu = menu, menuId = menuId, viewModel = viewModel)
        },
    ) { innerPadding ->
        Crossfade(
            targetState = HomeBottomNavMenu.getHomeBottomNavMenuFromResource(menuId),
            label = OtherConstant.EMPTY_STRING
        ) { destination ->
            when {
                myAnimeListTokenResponse.isError -> {
                    LaunchedEffect(myAnimeListTokenResponse.isError) {
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(errorMessage)
                        }
                    }
                }
            }

            val innerModifier = Modifier.padding(innerPadding)
            when (destination) {
                HomeBottomNavMenu.WISHLIST -> {
                    Wishlist(
                        modifier = innerModifier,
                        viewModel = viewModel,
                        myAnimeListUser = myAnimeListUser,
                        navigateToGameDetails = navigateToGameDetails,
                        navigateToAnimeDetails = navigateToAnimeDetails,
                        navigateToMovieDetails = navigateToMovieDetails,
                        navigateToSearch = navigateToSearch
                    )
                }
                HomeBottomNavMenu.DEALS -> Deals(
                    viewModel = viewModel,
                    modifier = innerModifier,
                    lazyListState = lazyListStateDeals,
                    data = lazyDeals
                )
                HomeBottomNavMenu.ABOUT -> About(
                    viewModel = viewModel,
                    googleProfileImage = googleProfileImage,
                    googleUsername = googleUsername,
                    myAnimeListUserResult = myAnimeListUserResult,
                    myAnimeListUser = myAnimeListUser,
                    navigateToMyAnimeListLogin = navigateToMyAnimeListLogin,
                    modifier = innerModifier,
                )
            }
        }
    }
}
