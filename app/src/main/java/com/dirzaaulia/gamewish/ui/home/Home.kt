package com.dirzaaulia.gamewish.ui.home

import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.cheapshark.Deals
import com.dirzaaulia.gamewish.extension.isError
import com.dirzaaulia.gamewish.ui.home.about.About
import com.dirzaaulia.gamewish.ui.home.deals.Deals
import com.dirzaaulia.gamewish.ui.home.wishlist.Wishlist
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding
import kotlinx.coroutines.launch
import java.util.*

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
    val menuId: Int by viewModel.selectedBottomNav.collectAsState(initial = 0)
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
        backgroundColor = MaterialTheme.colors.primarySurface,
        bottomBar = {
            HomeBottomBar(menu = menu, menuId = menuId, viewModel = viewModel)
        },
    ) { innerPadding ->
        Crossfade(
            targetState = HomeBottomNavMenu.getHomeBottomNavMenuFromResource(menuId)
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
                )
            }
        }
    }
}

@Composable
fun HomeBottomBar(
    menu: Array<HomeBottomNavMenu>,
    menuId: Int,
    viewModel: HomeViewModel
) {
    BottomNavigation(
        modifier = Modifier.navigationBarsHeight(56.dp)
    ) {
        menu.forEach { menu ->
            BottomNavigationItem(
                icon = { Icon(imageVector = menu.icon, contentDescription = null) },
                label = { Text(stringResource(menu.title).uppercase(Locale.getDefault())) },
                selected = menu == HomeBottomNavMenu.getHomeBottomNavMenuFromResource(menuId),
                onClick = {
                    viewModel.selectBottomNavMenu(menu.title)
                },
                selectedContentColor = MaterialTheme.colors.secondary,
                unselectedContentColor = LocalContentColor.current,
                modifier = Modifier.navigationBarsPadding()
            )
        }
    }
}

enum class HomeBottomNavMenu(
    @StringRes val title: Int,
    val icon: ImageVector
) {
    WISHLIST(R.string.wishlist, Icons.Filled.FavoriteBorder),
    DEALS(R.string.deals, Icons.Filled.LocalOffer),
    ABOUT(R.string.about, Icons.Filled.Info);

    companion object {
        fun getHomeBottomNavMenuFromResource(@StringRes resources: Int): HomeBottomNavMenu {
            return when (resources) {
                R.string.wishlist -> WISHLIST
                R.string.deals -> DEALS
                R.string.about -> ABOUT
                else -> WISHLIST
            }
        }
    }
}

