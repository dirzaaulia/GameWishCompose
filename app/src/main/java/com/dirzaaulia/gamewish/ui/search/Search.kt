package com.dirzaaulia.gamewish.ui.search

import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.Theaters
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.primarySurface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.myanimelist.ParentNode
import com.dirzaaulia.gamewish.data.model.rawg.Games
import com.dirzaaulia.gamewish.data.model.rawg.SearchTab
import com.dirzaaulia.gamewish.data.model.tmdb.Movie
import com.dirzaaulia.gamewish.data.model.wishlist.SearchMenu
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.ui.search.tab.anime.SearchAnime
import com.dirzaaulia.gamewish.ui.search.tab.game.SearchGame
import com.dirzaaulia.gamewish.ui.search.tab.movie.Movie
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.isSucceeded

@Composable
fun Search(
    homeViewModel: HomeViewModel,
    viewModel: SearchViewModel = hiltViewModel(),
    menuId: Int,
    navigateToGameDetails: (Long) -> Unit,
    navigateToAnimeDetails: (Long, String) -> Unit,
    navigateToMovieDetails: (Long, String) -> Unit,
    upPress: () -> Unit
) {
    val accessTokenResult by homeViewModel.tokenResult.collectAsState()

    val menu = SearchNavMenu.values()
    val searchMenuId: Int by viewModel.selectedBottomNav.collectAsState()
    val scope = rememberCoroutineScope()
    val scaffoldStateGame = rememberScaffoldState()
    val lazyListStateSearchGames = rememberLazyListState()
    val lazyListStateGenre = rememberLazyListState()
    val lazyListStatePublisher = rememberLazyListState()
    val lazyListStatePlatform = rememberLazyListState()

    val genre: LazyPagingItems<SearchTab> = viewModel.genres.collectAsLazyPagingItems()
    val publisher: LazyPagingItems<SearchTab> = viewModel.publishers.collectAsLazyPagingItems()
    val platform: LazyPagingItems<SearchTab> = viewModel.platforms.collectAsLazyPagingItems()
    val searchGameList: LazyPagingItems<Games> = viewModel.searchGameList.collectAsLazyPagingItems()
    val searchGameRequest by viewModel.searchGameRequest.collectAsState()

    val lazyListStateAnime = rememberLazyListState()
    val lazyListStateManga = rememberLazyListState()
    val lazyListStateSeasonalAnime = rememberLazyListState()
    val searchAnimeQuery by viewModel.searchAnimeQuery.collectAsState()
    val seasonalAnimeQuery by viewModel.seasonalAnimeQuery.collectAsState()
    val searchAnimeList: LazyPagingItems<ParentNode> =
        viewModel.searchAnimeList.collectAsLazyPagingItems()
    val searchMangaList: LazyPagingItems<ParentNode> =
        viewModel.searchMangaList.collectAsLazyPagingItems()
    val seasonalAnimeList: LazyPagingItems<ParentNode> =
        viewModel.seasonalAnimeList.collectAsLazyPagingItems()

    val lazyListStateMovie = rememberLazyListState()
    val lazyListStateTv = rememberLazyListState()
    val searchMovieQuery by viewModel.searchMovieQuery.collectAsState()
    val searchMovieList: LazyPagingItems<Movie> =
        viewModel.searchMovieList.collectAsLazyPagingItems()
    val searchTvList: LazyPagingItems<Movie> =
        viewModel.searchTvList.collectAsLazyPagingItems()

    when {
        accessTokenResult.isSucceeded -> {
            viewModel.getAccessToken()
        }
    }

    LaunchedEffect(menuId) {
        viewModel.selectBottomNavMenu(menuId)
    }

    Scaffold(
        modifier = Modifier.navigationBarsPadding().imePadding(),
        backgroundColor = MaterialTheme.colors.primarySurface,
        bottomBar = {
            SearchBottomBar(menu = menu, menuId = searchMenuId, viewModel = viewModel)
        },
    ) { innerPadding ->
        Crossfade(
            targetState = SearchNavMenu.getSearchNavMenu(searchMenuId),
            label = OtherConstant.EMPTY_STRING
        ) { destination ->
            val innerModifier = Modifier.padding(innerPadding)
            when (destination) {
                SearchNavMenu.GAME -> {
                    SearchGame(
                        navigateToGameDetails = navigateToGameDetails,
                        modifier = innerModifier,
                        viewModel = viewModel,
                        upPress = upPress,
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
                        lazyListStateAnime = lazyListStateAnime,
                        lazyListStateManga = lazyListStateManga,
                        lazyListStateSeasonalAnime = lazyListStateSeasonalAnime,
                        searchAnimeQuery = searchAnimeQuery,
                        seasonalAnimeQuery = seasonalAnimeQuery,
                        searchAnimeList = searchAnimeList,
                        searchMangaList = searchMangaList,
                        seasonalAnimeList = seasonalAnimeList,
                        navigateToAnimeDetails = navigateToAnimeDetails,
                        upPress = upPress
                    )
                }
                SearchNavMenu.MOVIE -> {
                    Movie(
                        modifier = innerModifier,
                        viewModel = viewModel,
                        lazyListStateMovie = lazyListStateMovie,
                        lazyListStateTv = lazyListStateTv,
                        searchMovieQuery = searchMovieQuery,
                        searchMovieList = searchMovieList,
                        searchTvList = searchTvList,
                        navigateToMovieDetails = navigateToMovieDetails,
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
    BottomNavigation {
        menu.forEach { menu ->
            BottomNavigationItem(
                icon = { Icon(imageVector = menu.icon, contentDescription = null) },
                selected = menu == SearchNavMenu.getSearchNavMenu(menuId),
                onClick = { viewModel.selectBottomNavMenu(menu.ordinal) },
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
    ANIME(R.string.anime_manga, Icons.Filled.Tv),
    MOVIE(R.string.movie_tv_show, Icons.Filled.Theaters);

    companion object {
        fun getSearchNavMenu(menu: Int) : SearchNavMenu {
            return when (menu) {
                SearchMenu.GAME.ordinal -> GAME
                SearchMenu.ANIME.ordinal -> ANIME
                SearchMenu.MOVIE.ordinal -> MOVIE
                else -> GAME
            }
        }
    }
}