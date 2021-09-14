package com.dirzaaulia.gamewish.ui.search

import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.Tv
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.myanimelist.ParentNode
import com.dirzaaulia.gamewish.data.model.rawg.Games
import com.dirzaaulia.gamewish.data.model.rawg.Genre
import com.dirzaaulia.gamewish.data.model.rawg.Platform
import com.dirzaaulia.gamewish.data.model.rawg.Publisher
import com.dirzaaulia.gamewish.extension.isSucceeded
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.ui.search.tab.anime.SearchAnime
import com.dirzaaulia.gamewish.ui.search.tab.game.SearchGame
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding
import java.util.*

@Composable
fun Search(
    homeViewModel: HomeViewModel,
    viewModel: SearchViewModel = hiltViewModel(),
    menuId: Int,
    navigateToGameDetails: (Long) -> Unit,
    navigateToAnimeDetails: (Long, String) -> Unit,
    upPress: () -> Unit
) {
    val accessTokenResult by homeViewModel.tokenResult.collectAsState()

    val menu = SearchNavMenu.values()
    val searchMenuId: Int by viewModel.selectedBottomNav.collectAsState()
    val scope = rememberCoroutineScope()
    val scaffoldStateGame = rememberBackdropScaffoldState(BackdropValue.Revealed)
    val lazyListStateSearchGames = rememberLazyListState()
    val lazyListStateGenre = rememberLazyListState()
    val lazyListStatePublisher = rememberLazyListState()
    val lazyListStatePlatform = rememberLazyListState()

    val genre: LazyPagingItems<Genre> = viewModel.genres.collectAsLazyPagingItems()
    val publisher: LazyPagingItems<Publisher> = viewModel.publishers.collectAsLazyPagingItems()
    val platform: LazyPagingItems<Platform> = viewModel.platforms.collectAsLazyPagingItems()
    val searchGameList: LazyPagingItems<Games> = viewModel.searchGameList.collectAsLazyPagingItems()
    val searchGameRequest by viewModel.searchGameRequest.collectAsState()

    val lazyListStateSeasonalAnime = rememberLazyListState()
    val searchAnimeQuery by viewModel.searchAnimeQuery.collectAsState()
    val seasonalAnimeQuery by viewModel.seasonalAnimeQuery.collectAsState()
    val seasonalAnimeList: LazyPagingItems<ParentNode> =
        viewModel.seasonalAnimeList.collectAsLazyPagingItems()

    when {
        accessTokenResult.isSucceeded -> {
            viewModel.getAccessToken()
        }
    }

    LaunchedEffect(menuId) {
        viewModel.selectBottomNavMenu(menuId)
    }

    Scaffold(
        backgroundColor = MaterialTheme.colors.primarySurface,
        bottomBar = {
            SearchBottomBar(menu = menu, menuId = searchMenuId, viewModel = viewModel)
        },
    ) { innerPadding ->
        Crossfade(
            targetState = SearchNavMenu.getSearchNavMenuFromResource(searchMenuId)
        ) { destination ->
            val innerModifier = Modifier.padding(innerPadding)
            when (destination) {
                SearchNavMenu.GAME -> {
                    SearchGame(
                        navigateToGameDetails = navigateToGameDetails,
                        modifier = innerModifier,
                        viewModel = viewModel,
                        upPress = upPress,
                        scope = scope,
                        scaffoldState = scaffoldStateGame,
                        searchGameList = searchGameList,
                        lazyListStateSearchGames = lazyListStateSearchGames,
                        lazyListStateGenre = lazyListStateGenre,
                        lazyListStatePublisher = lazyListStatePublisher,
                        lazyListStatePlatform = lazyListStatePlatform,
                        genre = genre,
                        publisher = publisher,
                        platform = platform,
                        searchGameRequest = searchGameRequest,
                    )
                }
                SearchNavMenu.ANIME -> {
                    SearchAnime(
                        modifier = innerModifier,
                        viewModel = viewModel,
                        homeViewModel = homeViewModel,
                        scope = scope,
                        accessTokenResult = accessTokenResult,
                        lazyListStateSeasonalAnime = lazyListStateSeasonalAnime,
                        searchAnimeQuery = searchAnimeQuery,
                        seasonalAnimeQuery = seasonalAnimeQuery,
                        seasonalAnimeList = seasonalAnimeList,
                        navigateToAnimeDetails = navigateToAnimeDetails,
                        upPress = upPress
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBottomBar(
    menu: Array<SearchNavMenu>,
    menuId: Int,
    viewModel: SearchViewModel
) {
    BottomNavigation(
        modifier = Modifier.navigationBarsHeight(56.dp)
    ) {
        menu.forEach { menu ->
            BottomNavigationItem(
                icon = { Icon(imageVector = menu.icon, contentDescription = null) },
                label = { Text(stringResource(menu.title).uppercase(Locale.getDefault())) },
                selected = menu == SearchNavMenu.getSearchNavMenuFromResource(menuId),
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

enum class SearchNavMenu (
    @StringRes val title: Int,
    val icon: ImageVector
) {
    GAME(R.string.game, Icons.Filled.Games),
    ANIME(R.string.anime_manga, Icons.Filled.Tv);

    companion object {
        fun getSearchNavMenuFromResource(@StringRes resources: Int) : SearchNavMenu {
            return when (resources) {
                R.string.game -> GAME
                R.string.anime_manga -> ANIME
                else -> GAME
            }
        }
    }
}