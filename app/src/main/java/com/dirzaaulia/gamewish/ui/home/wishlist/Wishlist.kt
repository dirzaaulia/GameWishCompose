package com.dirzaaulia.gamewish.ui.home.wishlist

import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.myanimelist.ParentNode
import com.dirzaaulia.gamewish.data.model.wishlist.GameWishlist
import com.dirzaaulia.gamewish.data.model.wishlist.MovieWishlist
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.ui.home.wishlist.anime.WishlistAnime
import com.dirzaaulia.gamewish.ui.home.wishlist.game.WishlistGame
import com.dirzaaulia.gamewish.ui.home.wishlist.manga.WishlistManga
import com.dirzaaulia.gamewish.ui.home.wishlist.movie.WishlistMovie
import com.dirzaaulia.gamewish.ui.home.wishlist.tv_show.WishlistTVShow
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Wishlist(
    modifier: Modifier,
    viewModel: HomeViewModel,
    navigateToGameDetails: (Long) -> Unit,
    navigateToAnimeDetails: (Long, String) -> Unit,
    navigateToMovieDetails: (Long, String) -> Unit,
    navigateToSearch: (Int) -> Unit
) {
    val menu = WishlistTab.values()
    val menuId: Int by viewModel.selectedWishlistTab.collectAsState(initial = 0)
    val gameQuery by viewModel.gameQuery.collectAsState()
    val movieQuery by viewModel.movieQuery.collectAsState()
    val tvShowQuery by viewModel.tvShowQuery.collectAsState()
    val gameStatus by viewModel.gameStatus.collectAsState()
    val animeStatus by viewModel.animeStatus.collectAsState()
    val mangaStatus by viewModel.mangaStatus.collectAsState()
    val movieStatus by viewModel.movieStatus.collectAsState()
    val tvShowStatus by viewModel.tvShowStatus.collectAsState()
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val lazyListStateGame = rememberLazyListState()
    val lazyListStateAnime = rememberLazyListState()
    val lazyListStateManga = rememberLazyListState()
    val lazyListStateMovie = rememberLazyListState()
    val lazyListStateTVShow = rememberLazyListState()
    val lazyListGameWishlist: LazyPagingItems<GameWishlist> =
        viewModel.listWishlist.collectAsLazyPagingItems()
    val accessTokenResult by viewModel.tokenResult.collectAsState()
    val lazyListAnime: LazyPagingItems<ParentNode> =
        viewModel.animeList.collectAsLazyPagingItems()
    val lazyListManga: LazyPagingItems<ParentNode> =
        viewModel.mangaList.collectAsLazyPagingItems()
    val lazyListMovie: LazyPagingItems<MovieWishlist> =
        viewModel.movieWishlist.collectAsLazyPagingItems()
    val lazyListTVShow: LazyPagingItems<MovieWishlist> =
        viewModel.tvShowWishlist.collectAsLazyPagingItems()

    BottomSheetScaffold(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.primarySurface,
        scaffoldState = scaffoldState,
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        sheetContent = {
            Crossfade(targetState = WishlistTab.getTabFromResource(menuId)) { destination ->
                when (destination) {
                    WishlistTab.GAME -> GameFilterDialog(
                        viewModel = viewModel,
                        gameStatus = gameStatus,
                        searchQuery = gameQuery
                    )
                    WishlistTab.ANIME -> AnimeFilterDialog(
                        viewModel = viewModel,
                        animeStatus = animeStatus
                    )
                    WishlistTab.MANGA -> MangaFilterDialog(
                        viewModel = viewModel,
                        mangaStatus = mangaStatus
                    )
                    WishlistTab.MOVIE -> MovieFilterDialog(
                        viewModel = viewModel,
                        movieStatus = movieStatus,
                        searchQuery =  movieQuery,
                        type = "Movie"
                    )
                    WishlistTab.TVSHOW -> MovieFilterDialog(
                        viewModel = viewModel,
                        movieStatus = tvShowStatus,
                        searchQuery =  tvShowQuery,
                        type = "TV Show"
                    )
                }
            }

        },
        sheetPeekHeight = 0.dp,
        topBar = {
            Crossfade(targetState = WishlistTab.getTabFromResource(menuId)) { destination ->
                when (destination) {
                    WishlistTab.GAME -> GameAppBar(
                        scope = scope,
                        scaffoldState = scaffoldState,
                        navigateToSearch = navigateToSearch
                    )
                    WishlistTab.ANIME -> AnimeAppBar(
                        scope = scope,
                        scaffoldState = scaffoldState,
                        navigateToSearch = navigateToSearch
                    )
                    WishlistTab.MANGA -> AnimeAppBar(
                        scope = scope,
                        scaffoldState = scaffoldState,
                        navigateToSearch = navigateToSearch
                    )
                    WishlistTab.MOVIE -> MovieAppBar(
                        scope = scope,
                        scaffoldState = scaffoldState,
                        navigateToSearch = navigateToSearch
                    )
                    WishlistTab.TVSHOW -> MovieAppBar(
                        scope = scope,
                        scaffoldState = scaffoldState,
                        navigateToSearch = navigateToSearch
                    )
                }
            }
        },
    ) {
        Scaffold(
            topBar = { WishlistTabMenu(menu = menu, menuId = menuId, viewModel = viewModel) }
        ) {
            Crossfade(targetState = WishlistTab.getTabFromResource(menuId)) { destination ->
                when (destination) {
                    WishlistTab.GAME -> {
                        WishlistGame(
                            data = lazyListGameWishlist,
                            navigateToGameDetails = navigateToGameDetails,
                            lazyListState = lazyListStateGame,
                            gameStatus = gameStatus
                        )
                    }
                    WishlistTab.ANIME -> {
                        WishlistAnime(
                            accessTokenResult = accessTokenResult,
                            viewModel = viewModel,
                            lazyListState = lazyListStateAnime,
                            data = lazyListAnime,
                            animeStatus = animeStatus,
                            navigateToAnimeDetails = navigateToAnimeDetails
                           )
                    }
                    WishlistTab.MANGA -> {
                        WishlistManga(
                            accessTokenResult = accessTokenResult,
                            viewModel = viewModel,
                            lazyListState = lazyListStateManga,
                            data = lazyListManga,
                            animeStatus = mangaStatus,
                            navigateToAnimeDetails = navigateToAnimeDetails
                        )
                    }
                    WishlistTab.MOVIE -> {
                        WishlistMovie(
                            data = lazyListMovie,
                            navigateToMovieDetail = navigateToMovieDetails,
                            lazyListState = lazyListStateMovie,
                            movieStatus = movieStatus
                        )
                    }
                    WishlistTab.TVSHOW -> {
                        WishlistTVShow(
                            data = lazyListTVShow,
                            navigateToMovieDetail = navigateToMovieDetails,
                            lazyListState = lazyListStateTVShow,
                            tvShowStatus = tvShowStatus
                        )
                    }
                }
                LaunchedEffect(WishlistTab.getTabFromResource(menuId)) {
                    scaffoldState.bottomSheetState.collapse()
                }
            }
        }
    }
}

@Composable
fun GameAppBar(
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState,
    navigateToSearch: (Int) -> Unit = { },
) {
    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier
            .statusBarsPadding()
            .wrapContentHeight()
    ) {
        Image(
            modifier = Modifier
                .padding(0.dp, 16.dp, 0.dp, 16.dp)
                .size(100.dp, 0.dp)
                .align(Alignment.CenterVertically)
                .aspectRatio(1.0f),
            painter = painterResource(id = R.drawable.ic_gamewish_dark),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = {
                    scope.launch {
                        if (scaffoldState.bottomSheetState.isCollapsed) {
                            scaffoldState.bottomSheetState.expand()
                        } else {
                            scaffoldState.bottomSheetState.collapse()
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Sort,
                    contentDescription = null,
                )
            }
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = { navigateToSearch(R.string.game) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
fun AnimeAppBar(
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState,
    navigateToSearch: (Int) -> Unit = { }
) {
    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier
            .statusBarsPadding()
            .wrapContentHeight()
    ) {
        Image(
            modifier = Modifier
                .padding(0.dp, 16.dp, 0.dp, 16.dp)
                .size(100.dp, 0.dp)
                .align(Alignment.CenterVertically)
                .aspectRatio(1.0f),
            painter = painterResource(id = R.drawable.ic_gamewish_dark),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = {
                    scope.launch {
                        if (scaffoldState.bottomSheetState.isCollapsed) {
                            scaffoldState.bottomSheetState.expand()
                        } else {
                            scaffoldState.bottomSheetState.collapse()
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Sort,
                    contentDescription = null,
                )
            }
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = { navigateToSearch(R.string.anime_manga) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
fun MovieAppBar(
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState,
    navigateToSearch: (Int) -> Unit = { },
) {
    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier
            .statusBarsPadding()
            .wrapContentHeight()
    ) {
        Image(
            modifier = Modifier
                .padding(0.dp, 16.dp, 0.dp, 16.dp)
                .size(100.dp, 0.dp)
                .align(Alignment.CenterVertically)
                .aspectRatio(1.0f),
            painter = painterResource(id = R.drawable.ic_gamewish_dark),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = {
                    scope.launch {
                        if (scaffoldState.bottomSheetState.isCollapsed) {
                            scaffoldState.bottomSheetState.expand()
                        } else {
                            scaffoldState.bottomSheetState.collapse()
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Sort,
                    contentDescription = null,
                )
            }
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = { navigateToSearch(R.string.movie_tv_show) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                )
            }
        }
    }
}


@Composable
fun WishlistTabMenu(
    menu: Array<WishlistTab>,
    menuId: Int,
    viewModel: HomeViewModel
) {
    ScrollableTabRow(selectedTabIndex = menuId) {
        menu.forEachIndexed { index, wishlistTab ->
            Tab(
                selected = menuId == index,
                text = { Text(stringResource(id = wishlistTab.title)) },
                onClick = { viewModel.selectWishlistTab(index) }
            )
        }
    }
}

enum class WishlistTab(@StringRes val title: Int) {
    GAME(R.string.game),
    ANIME(R.string.anime),
    MANGA(R.string.manga),
    MOVIE(R.string.movie),
    TVSHOW(R.string.tv_show);

    companion object {
        fun getTabFromResource(index: Int): WishlistTab {
            return when (index) {
                0 -> GAME
                1 -> ANIME
                2 -> MANGA
                3 -> MOVIE
                4 -> TVSHOW
                else -> GAME
            }
        }
    }
}