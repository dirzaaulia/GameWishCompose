package com.dirzaaulia.gamewish.ui.home.wishlist.game

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.primarySurface
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
    data: LazyPagingItems<Wishlist>,
    selectGame: (Long) -> Unit,
    lazyListState: LazyListState,
) {
    Scaffold(
        backgroundColor = MaterialTheme.colors.primarySurface,
    ) { innerPadding ->
        GameList(
            modifier = Modifier.padding(innerPadding),
            data = data,
            state = rememberSwipeRefreshState(
                data.loadState.refresh is LoadState.Loading
            ),
            lazyListState
        ) { wishlist ->
            GameListItem(
                wishlist = wishlist,
                selectGame = selectGame
            )
        }
    }
}