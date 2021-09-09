package com.dirzaaulia.gamewish.ui.home.wishlist.game

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import com.dirzaaulia.gamewish.data.model.Wishlist
import com.dirzaaulia.gamewish.ui.common.CommonVerticalList
import com.dirzaaulia.gamewish.ui.common.GameListItem

@Composable
fun WishlistGame(
    data: LazyPagingItems<Wishlist>,
    selectGame: (Long) -> Unit,
    lazyListState: LazyListState,
) {
    CommonVerticalList(
        data = data,
        lazyListState = lazyListState,
        emptyString = "Your Game Wishlist is still empty!"
    ) { wishlist ->
        GameListItem(
            wishlist = wishlist,
            selectGame = selectGame
        )
    }
}