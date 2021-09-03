package com.dirzaaulia.gamewish.ui.home.wishlist.tab

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.dirzaaulia.gamewish.data.model.Wishlist
import com.dirzaaulia.gamewish.ui.common.GameList
import com.dirzaaulia.gamewish.ui.common.GameListItem
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun WishlistGame(
    modifier: Modifier = Modifier,
    data: LazyPagingItems<Wishlist>,
    navigateToDetailsWishlist: (Long) -> Unit
) {
    GameList(
        modifier = modifier,
        data = data,
        state = rememberSwipeRefreshState(data.loadState.refresh is LoadState.Loading)
    ) { wishlist ->
        GameListItem(
            wishlist = wishlist,
            navigateToDetailsWishlist = navigateToDetailsWishlist
        )
    }
}
