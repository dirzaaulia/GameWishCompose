package com.dirzaaulia.gamewish.ui.home.wishlist

import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.Wishlist
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.ui.home.wishlist.tab.WishlistGame

@Composable
fun Wishlist(
    viewModel: HomeViewModel,
    navigateToGameDetails: (Long) -> Unit
) {
    val menu = WishlistTab.values()
    val menuId: Int by viewModel.selectedWishlistTab.collectAsState(initial = 0)
    val lazyListWishlist: LazyPagingItems<Wishlist> =
        viewModel.listWishlist.collectAsLazyPagingItems()

    Scaffold(
        backgroundColor = MaterialTheme.colors.primarySurface,
        topBar = { WishlistTabMenu(menu = menu, menuId = menuId, viewModel = viewModel) }
    ) {
        Crossfade(targetState = WishlistTab.getTabFromResource(menuId)) { destination ->
            when (destination) {
                //TODO Need to update with Anime & Manga layout
                WishlistTab.GAME -> WishlistGame(
                    data = lazyListWishlist,
                    navigateToDetailsWishlist = navigateToGameDetails
                )
                WishlistTab.ANIME -> WishlistGame(
                    data = lazyListWishlist,
                    navigateToDetailsWishlist = navigateToGameDetails
                )
                WishlistTab.MANGA -> WishlistGame(
                    data = lazyListWishlist,
                    navigateToDetailsWishlist = navigateToGameDetails
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

enum class WishlistTab(
    @StringRes val title: Int
) {
    GAME(R.string.game),
    ANIME(R.string.anime),
    MANGA(R.string.manga);

    companion object {
        fun getTabFromResource(@StringRes resource: Int): WishlistTab {
            return when (resource) {
                R.string.game -> GAME
                R.string.anime -> ANIME
                R.string.manga -> MANGA
                else -> GAME
            }
        }
    }
}