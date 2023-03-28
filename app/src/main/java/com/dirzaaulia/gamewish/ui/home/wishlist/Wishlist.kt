package com.dirzaaulia.gamewish.ui.home.wishlist

import WishlistTab
import WishlistTabMenu
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.myanimelist.ParentNode
import com.dirzaaulia.gamewish.data.model.myanimelist.User
import com.dirzaaulia.gamewish.data.model.wishlist.FilterDialogType
import com.dirzaaulia.gamewish.data.model.wishlist.GameWishlist
import com.dirzaaulia.gamewish.data.model.wishlist.MovieWishlist
import com.dirzaaulia.gamewish.data.model.wishlist.SearchMenu
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.ui.home.wishlist.game.WishlistGame
import com.dirzaaulia.gamewish.ui.home.wishlist.myanimelist.WishlistMyAnimeList
import com.dirzaaulia.gamewish.ui.home.wishlist.tmdb.WishlistTmdb
import com.dirzaaulia.gamewish.utils.MyAnimeListConstant
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.myAnimeListStatusFormatted
import com.dirzaaulia.gamewish.utils.tmdbStatusFormatted

@Composable
fun Wishlist(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    myAnimeListUser: User,
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
        scaffoldState = scaffoldState,
        topBar = {
            Crossfade(
                targetState = WishlistTab.getTabFromResource(menuId),
                label = OtherConstant.EMPTY_STRING,
            ) { destination ->
                val searchMenu = when (destination) {
                    WishlistTab.GAME -> SearchMenu.GAME
                    WishlistTab.ANIME, WishlistTab.MANGA -> SearchMenu.ANIME
                    WishlistTab.MOVIE, WishlistTab.TVSHOW -> SearchMenu.MOVIE
                }
                val sortStatus = when (destination) {
                    WishlistTab.GAME -> gameStatus.ifBlank { OtherConstant.ALL }
                    WishlistTab.ANIME -> animeStatus.myAnimeListStatusFormatted(
                        MyAnimeListConstant.MYANIMELIST_STATUS_ALL
                    )
                    WishlistTab.MANGA -> mangaStatus.myAnimeListStatusFormatted(
                        MyAnimeListConstant.MYANIMELIST_STATUS_ALL
                    )
                    WishlistTab.MOVIE -> movieStatus.tmdbStatusFormatted()
                    WishlistTab.TVSHOW -> tvShowStatus.tmdbStatusFormatted()
                }
                WishlistAppBar(
                    scope = scope,
                    scaffoldState = scaffoldState,
                    searchMenu = searchMenu,
                    sortStatus = sortStatus,
                    myAnimeListUser = myAnimeListUser,
                    navigateToSearch = navigateToSearch
                )
            }
        },
        sheetContent = {
            Crossfade(
                targetState = WishlistTab.getTabFromResource(menuId),
                label = OtherConstant.EMPTY_STRING
            ) { destination ->
                when (destination) {
                    WishlistTab.GAME -> FilterDialog(
                        viewModel = viewModel,
                        filterStatus = gameStatus,
                        searchQuery = gameQuery,
                        type = FilterDialogType.GAME
                    )
                    WishlistTab.ANIME -> FilterDialog(
                        viewModel = viewModel,
                        filterStatus = animeStatus,
                        type = FilterDialogType.ANIME
                    )
                    WishlistTab.MANGA -> FilterDialog(
                        viewModel = viewModel,
                        filterStatus = mangaStatus,
                        type = FilterDialogType.MANGA
                    )
                    WishlistTab.MOVIE -> FilterDialog(
                        viewModel = viewModel,
                        filterStatus = movieStatus,
                        searchQuery = movieQuery,
                        type = FilterDialogType.MOVIE
                    )
                    WishlistTab.TVSHOW -> FilterDialog(
                        viewModel = viewModel,
                        filterStatus = tvShowStatus,
                        searchQuery = tvShowQuery,
                        type = FilterDialogType.TV
                    )
                }
            }
        },
        sheetPeekHeight = 0.dp,
    ) { innerPadding ->
        Column (modifier = Modifier.padding(innerPadding)) {
            WishlistTabMenu(
                menu = menu,
                menuId = menuId,
                viewModel = viewModel
            )
            Crossfade(
                targetState = WishlistTab.getTabFromResource(menuId),
                label = OtherConstant.EMPTY_STRING
            ) { destination ->
                when (destination) {
                    WishlistTab.GAME -> {
                        WishlistGame(
                            data = lazyListGameWishlist,
                            navigateToGameDetails = navigateToGameDetails,
                            lazyListState = lazyListStateGame
                        )
                    }
                    WishlistTab.ANIME -> {
                        WishlistMyAnimeList(
                            accessTokenResult = accessTokenResult,
                            viewModel = viewModel,
                            lazyListState = lazyListStateAnime,
                            data = lazyListAnime,
                            animeType = MyAnimeListConstant.MYANIMELIST_TYPE_ANIME,
                            emptyString = stringResource(R.string.myanimelist_anime_list_empty),
                            errorString = stringResource(R.string.anime_list_error),
                            navigateToAnimeDetails = navigateToAnimeDetails
                        )
                    }
                    WishlistTab.MANGA -> {
                        WishlistMyAnimeList(
                            accessTokenResult = accessTokenResult,
                            viewModel = viewModel,
                            lazyListState = lazyListStateManga,
                            data = lazyListManga,
                            animeType = MyAnimeListConstant.MYANIMELIST_TYPE_MANGA,
                            emptyString = stringResource(R.string.myanimelist_manga_list_empty),
                            errorString = stringResource(R.string.manga_list_error),
                            navigateToAnimeDetails = navigateToAnimeDetails
                        )
                    }
                    WishlistTab.MOVIE -> {
                        WishlistTmdb(
                            data = lazyListMovie,
                            navigateToMovieDetail = navigateToMovieDetails,
                            lazyListState = lazyListStateMovie,
                            emptyString = stringResource(id = R.string.movie_list_empty),
                            errorString = stringResource(id = R.string.movie_list_error)
                        )
                    }

                    WishlistTab.TVSHOW -> {
                        WishlistTmdb(
                            data = lazyListTVShow,
                            navigateToMovieDetail = navigateToMovieDetails,
                            lazyListState = lazyListStateTVShow,
                            emptyString = stringResource(id = R.string.movie_list_empty),
                            errorString = stringResource(id = R.string.movie_list_error)
                        )
                    }
                }
            }
        }
    }
}