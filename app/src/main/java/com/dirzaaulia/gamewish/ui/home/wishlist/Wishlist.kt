package com.dirzaaulia.gamewish.ui.home.wishlist

import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
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
import com.dirzaaulia.gamewish.data.model.Wishlist
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.ui.home.wishlist.anime.WishlistAnime
import com.dirzaaulia.gamewish.ui.home.wishlist.game.WishlistGame
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Wishlist(
    navigateToGameDetails: (Long) -> Unit,
    modifier: Modifier,
    viewModel: HomeViewModel,
) {
    val menu = WishlistTab.values()
    val menuId: Int by viewModel.selectedWishlistTab.collectAsState(initial = 0)
    val searchQuery: String by viewModel.query.collectAsState()
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val lazyListStateGame = rememberLazyListState()
    val lazyListWishlist: LazyPagingItems<Wishlist> =
        viewModel.listWishlist.collectAsLazyPagingItems()
    val accessTokenResult by viewModel.tokenResult.collectAsState()
    val accessToken by viewModel.token.collectAsState()

    BottomSheetScaffold(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.primarySurface,
        scaffoldState = scaffoldState,
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        sheetContent = {
            GameFilterDialog(
                viewModel = viewModel,
                searchQuery = searchQuery
            )
        },
        sheetPeekHeight = 0.dp,
        topBar = {
            Crossfade(targetState = WishlistTab.getTabFromResource(menuId)) { destination ->
                when (destination) {
                    WishlistTab.GAME -> GameAppBar(scope, scaffoldState)
                    WishlistTab.ANIME-> AnimeAppBar()
                    WishlistTab.MANGA -> AnimeAppBar()
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
                        scope.launch {
                            scaffoldState.bottomSheetState.collapse()
                        }
                        WishlistGame(
                            data = lazyListWishlist,
                            selectGame = navigateToGameDetails,
                            lazyListStateGame
                        )
                    }
                    WishlistTab.ANIME -> {
                        WishlistAnime(
                            accessTokenResult = accessTokenResult,
                            accessToken = accessToken,
                            viewModel = viewModel)
                    }
                    WishlistTab.MANGA -> {

                    }
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
                    imageVector = Icons.Filled.FilterList,
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
fun AnimeAppBar() {
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
                onClick = { /* todo */ }
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