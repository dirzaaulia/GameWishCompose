package com.dirzaaulia.gamewish.ui.home.wishlist.tmdb

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import com.dirzaaulia.gamewish.data.model.wishlist.MovieWishlist
import com.dirzaaulia.gamewish.ui.common.CommonVerticalList
import com.dirzaaulia.gamewish.ui.common.item.WishlistMovieItem
import com.dirzaaulia.gamewish.utils.PlaceholderConstant

@Composable
fun WishlistTmdb(
    data: LazyPagingItems<MovieWishlist>,
    navigateToMovieDetail: (Long, String) -> Unit,
    lazyListState: LazyListState,
    emptyString: String,
    errorString: String,
) {
    CommonVerticalList(
        data = data,
        lazyListState = lazyListState,
        placeholderType = PlaceholderConstant.MOVIE_WISHLIST,
        emptyString = emptyString,
        errorString = errorString
    ) { wishlist ->
        WishlistMovieItem(
            movieWishlist = wishlist,
            navigateToMovieDetails = navigateToMovieDetail,
        )
    }
}
