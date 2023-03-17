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
import com.dirzaaulia.gamewish.utils.visible
import com.dirzaaulia.gamewish.ui.common.CommonVerticalList
import com.dirzaaulia.gamewish.ui.common.item.WishlistGameItem
import com.dirzaaulia.gamewish.utils.PlaceholderConstant
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder

@Composable
fun WishlistGame(
    data: LazyPagingItems<GameWishlist>,
    navigateToGameDetails: (Long) -> Unit,
    lazyListState: LazyListState,
    gameStatus: String,
) {
    Column {
        Text(
            text = stringResource(R.string.sort_by, gameStatus),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 8.dp)
                .align(Alignment.End)
                .placeholder(
                    visible = data.loadState.refresh is LoadState.Loading,
                    highlight = PlaceholderHighlight.shimmer(),
                    color = MaterialTheme.colors.secondary,
                    shape = MaterialTheme.shapes.small
                )
                .visible(data.loadState.refresh is LoadState.NotLoading)
        )
        CommonVerticalList(
            data = data,
            lazyListState = lazyListState,
            placeholderType = PlaceholderConstant.GAME_WISHLIST,
            emptyString = stringResource(R.string.wishlist_game_empty),
            errorString = stringResource(id = R.string.game_list_error)
        ) { wishlist ->
            WishlistGameItem(
                gameWishlist = wishlist,
                navigateToGameDetails = navigateToGameDetails,
            )
        }
    }
}