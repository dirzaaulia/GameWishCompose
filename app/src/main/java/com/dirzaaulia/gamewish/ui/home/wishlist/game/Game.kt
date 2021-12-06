package com.dirzaaulia.gamewish.ui.home.wishlist.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.wishlist.GameWishlist
import com.dirzaaulia.gamewish.extension.visible
import com.dirzaaulia.gamewish.ui.common.CommonVerticalList
import com.dirzaaulia.gamewish.ui.common.WishlistGameItem

@Composable
fun WishlistGame(
    data: LazyPagingItems<GameWishlist>,
    navigateToGameDetails: (Long) -> Unit,
    lazyListState: LazyListState,
    gameStatus: String,
) {
    var gameStatusFormatted = gameStatus

    if (gameStatusFormatted.isBlank()) {
        gameStatusFormatted = "All"
    }

    Column {
        Text(
            text = "Sort by : $gameStatusFormatted",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 8.dp)
                .align(Alignment.End)
                .visible(data.loadState.refresh is LoadState.NotLoading)
        )
        CommonVerticalList(
            data = data,
            lazyListState = lazyListState,
            emptyString = "Your Game Wishlist is still empty!",
            errorString = stringResource(id = R.string.game_list_error)
        ) { wishlist ->
            WishlistGameItem(
                gameWishlist = wishlist,
                navigateToGameDetails = navigateToGameDetails,
            )
        }
    }
}