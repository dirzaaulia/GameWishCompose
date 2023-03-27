package com.dirzaaulia.gamewish.ui.home.wishlist.game

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.wishlist.GameWishlist
import com.dirzaaulia.gamewish.ui.common.CommonVerticalList
import com.dirzaaulia.gamewish.ui.common.item.WishlistGameItem
import com.dirzaaulia.gamewish.utils.PlaceholderConstant

@Composable
fun WishlistGame(
    modifier: Modifier = Modifier,
    data: LazyPagingItems<GameWishlist>,
    navigateToGameDetails: (Long) -> Unit,
    lazyListState: LazyListState
) {
    CommonVerticalList(
        modifier = modifier,
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