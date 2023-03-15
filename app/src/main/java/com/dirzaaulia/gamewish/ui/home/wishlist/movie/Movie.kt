package com.dirzaaulia.gamewish.ui.home.wishlist.movie

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
import com.dirzaaulia.gamewish.data.model.wishlist.MovieWishlist
import com.dirzaaulia.gamewish.utils.visible
import com.dirzaaulia.gamewish.ui.common.CommonVerticalList
import com.dirzaaulia.gamewish.ui.common.item.WishlistMovieItem
import com.dirzaaulia.gamewish.utils.PlaceholderConstant
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder

@Composable
fun WishlistMovie(
    data: LazyPagingItems<MovieWishlist>,
    navigateToMovieDetail: (Long, String) -> Unit,
    lazyListState: LazyListState,
    emptyString: String,
    errorString: String,
    movieStatusFormatted: String,
) {

    Column {
        Text(
            text = stringResource(R.string.sort_by, movieStatusFormatted),
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
            placeholderType = PlaceholderConstant.MOVIE_WISHLIST,
            emptyString = emptyString,
            errorString = errorString
        ) { wishlist ->
            WishlistMovieItem(
                movieWishlist = wishlist,
                navigateToMovieDetails = navigateToMovieDetail,
                loadState = data.loadState,
            )
        }
    }
}
