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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.Wishlist
import com.dirzaaulia.gamewish.data.model.myanimelist.ParentNode
import com.dirzaaulia.gamewish.ui.common.AnimeFilterDialog
import com.dirzaaulia.gamewish.ui.common.GameFilterDialog
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.ui.home.wishlist.anime.WishlistAnime
import com.dirzaaulia.gamewish.ui.home.wishlist.game.WishlistGame
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Wishlist(
    navigateToGameDetails: (Long) -> Unit,
    modifier: Modifier,
    viewModel: HomeViewModel,
    navigateToMyAnimeListLogin: () -> Unit
) {
    val menu = WishlistTab.values()
    val menuId: Int by viewModel.selectedWishlistTab.collectAsState(initial = 0)
    val searchQuery by viewModel.query.collectAsState()
    val gameStatus by viewModel.gameStatus.collectAsState()
    val animeStatus by viewModel.animeStatus.collectAsState()
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val lazyListStateGame = rememberLazyListState()
    val lazyListStateAnime = rememberLazyListState()
    val lazyListWishlist: LazyPagingItems<Wishlist> =
        viewModel.listWishlist.collectAsLazyPagingItems()
    val accessTokenResult by viewModel.tokenResult.collectAsState()
    val lazyListAnime: LazyPagingItems<ParentNode> =
        viewModel.animeList.collectAsLazyPagingItems()

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
                        searchQuery = searchQuery
                    )
                    WishlistTab.ANIME -> AnimeFilterDialog(
                        viewModel = viewModel,
                        animeStatus = animeStatus
                    )
                    WishlistTab.MANGA -> AnimeFilterDialog(
                        viewModel = viewModel,
                        animeStatus = animeStatus
                    )
                }
            }

        },
        sheetPeekHeight = 0.dp,
        topBar = {
            Crossfade(targetState = WishlistTab.getTabFromResource(menuId)) { destination ->
                when (destination) {
                    WishlistTab.GAME -> GameAppBar(scope, scaffoldState)
                    WishlistTab.ANIME -> AnimeAppBar(scope, scaffoldState)
                    WishlistTab.MANGA -> AnimeAppBar(scope, scaffoldState)
                }
            }
        },
    ) {
        Scaffold(
            topBar = { WishlistTabMenu(menu = menu, menuId = menuId, viewModel = viewModel) }
        ) {
            Crossfade(targetState = WishlistTab.getTabFromResource(menuId)) { destination ->
                when (destination) {
                    //TODO Need to update with Anime & Manga layout
                    WishlistTab.GAME -> {
                        WishlistGame(
                            data = lazyListWishlist,
                            navigateToGameDetails = navigateToGameDetails,
                            lazyListState = lazyListStateGame,
                            viewModel = viewModel,
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
                            navigateToMyAnimeListLogin = navigateToMyAnimeListLogin
                        )
                    }
                    WishlistTab.MANGA -> {

                    }
                }
                scope.launch {
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
) {
    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier
            .height(80.dp)
            .statusBarsPadding()
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
                onClick = { /* todo */ }
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
) {
    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier
            .height(80.dp)
            .statusBarsPadding()
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
        }
    }
}

@Composable
fun WishlistTabMenu(
    menu: Array<WishlistTab>,
    menuId: Int,
    viewModel: HomeViewModel
) {
    TabRow(selectedTabIndex = menuId) {
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
    MANGA(R.string.manga);

    companion object {
        fun getTabFromResource(index: Int): WishlistTab {
            return when (index) {
                0 -> GAME
                1 -> ANIME
                2 -> MANGA
                else -> GAME
            }
        }
    }
}