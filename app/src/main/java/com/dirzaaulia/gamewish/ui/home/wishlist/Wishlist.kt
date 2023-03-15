package com.dirzaaulia.gamewish.ui.home.wishlist

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.primarySurface
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
import com.dirzaaulia.gamewish.ui.home.wishlist.movie.WishlistMovie
import com.dirzaaulia.gamewish.utils.MyAnimeListConstant
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.TmdbConstant
import com.dirzaaulia.gamewish.utils.movieStatusFormatted
import com.dirzaaulia.gamewish.utils.myAnimeListStatusFormatted
import com.dirzaaulia.gamewish.utils.replaceIfNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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
    val menuId: Int by viewModel.selectedWishlistTab.collectAsState(initial = OtherConstant.ZERO)
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
            Crossfade(
                targetState = WishlistTab.getTabFromResource(menuId),
                label = OtherConstant.EMPTY_STRING
            ) { destination ->
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
                        searchQuery = movieQuery,
                        type = TmdbConstant.TMDB_TYPE_MOVIE
                    )

                    WishlistTab.TVSHOW -> MovieFilterDialog(
                        viewModel = viewModel,
                        movieStatus = tvShowStatus,
                        searchQuery = tvShowQuery,
                        type = TmdbConstant.TMDB_TYPE_TVSHOW
                    )
                }
            }

        },
        sheetPeekHeight = 0.dp,
        topBar = {
            Crossfade(
                targetState = WishlistTab.getTabFromResource(menuId),
                label = OtherConstant.EMPTY_STRING,
            ) { destination ->
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
            Crossfade(
                targetState = WishlistTab.getTabFromResource(menuId),
                label = OtherConstant.EMPTY_STRING
            ) { destination ->
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
                            animeType = MyAnimeListConstant.MYANIMELIST_TYPE_MANGA,
                            emptyString = stringResource(R.string.myanimelist_anime_list_empty),
                            errorString = stringResource(R.string.anime_list_error),
                            animeStatusFormatted = animeStatus.myAnimeListStatusFormatted(
                                MyAnimeListConstant.MYANIMELIST_STATUS_ALL
                            ),
                            navigateToAnimeDetails = navigateToAnimeDetails
                        )
                    }

                    WishlistTab.MANGA -> {
                        WishlistAnime(
                            accessTokenResult = accessTokenResult,
                            viewModel = viewModel,
                            lazyListState = lazyListStateManga,
                            data = lazyListManga,
                            animeType = MyAnimeListConstant.MYANIMELIST_TYPE_MANGA,
                            emptyString = stringResource(R.string.myanimelist_manga_list_empty),
                            errorString = stringResource(R.string.manga_list_error),
                            animeStatusFormatted = mangaStatus.myAnimeListStatusFormatted(
                                MyAnimeListConstant.MYANIMELIST_STATUS_ALL
                            ),
                            navigateToAnimeDetails = navigateToAnimeDetails
                        )
                    }

                    WishlistTab.MOVIE -> {
                        WishlistMovie(
                            data = lazyListMovie,
                            navigateToMovieDetail = navigateToMovieDetails,
                            lazyListState = lazyListStateMovie,
                            emptyString = stringResource(id = R.string.movie_list_empty),
                            errorString = stringResource(id = R.string.movie_list_error),
                            movieStatusFormatted = movieStatus.movieStatusFormatted()
                        )
                    }

                    WishlistTab.TVSHOW -> {
                        WishlistMovie(
                            data = lazyListTVShow,
                            navigateToMovieDetail = navigateToMovieDetails,
                            lazyListState = lazyListStateTVShow,
                            emptyString = stringResource(id = R.string.movie_list_empty),
                            errorString = stringResource(id = R.string.movie_list_error),
                            movieStatusFormatted = tvShowStatus.movieStatusFormatted()
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
                    contentDescription = OtherConstant.EMPTY_STRING,
                )
            }
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = { navigateToSearch(R.string.game) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = OtherConstant.EMPTY_STRING,
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
                    contentDescription = OtherConstant.EMPTY_STRING,
                )
            }
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = { navigateToSearch(R.string.anime_manga) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = OtherConstant.EMPTY_STRING,
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
                    contentDescription = OtherConstant.EMPTY_STRING,
                )
            }
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = { navigateToSearch(R.string.movie_tv_show) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = OtherConstant.EMPTY_STRING,
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
                OtherConstant.ZERO -> GAME
                OtherConstant.ONE -> ANIME
                OtherConstant.TWO -> MANGA
                OtherConstant.THREE -> MOVIE
                OtherConstant.FOUR -> TVSHOW
                else -> GAME
            }
        }
    }
}