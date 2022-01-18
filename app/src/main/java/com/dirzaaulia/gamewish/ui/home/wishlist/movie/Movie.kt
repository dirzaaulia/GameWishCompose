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
import com.dirzaaulia.gamewish.extension.visible
import com.dirzaaulia.gamewish.ui.common.CommonVerticalList
import com.dirzaaulia.gamewish.ui.common.WishlistMovieItem

@Composable
fun WishlistMovie(
    data: LazyPagingItems<MovieWishlist>,
    navigateToMovieDetail: (Long, String) -> Unit,
    lazyListState: LazyListState,
    movieStatus: String,
) {
    var movieStatusFormatted = movieStatus

    if (movieStatusFormatted.isBlank()) {
        movieStatusFormatted = "All"
    }

    Column {
        Text(
            text = "Sort by : $movieStatusFormatted",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 8.dp)
                .align(Alignment.End)
                .visible(data.loadState.refresh is LoadState.NotLoading)
        )
        CommonVerticalList(
            data = data,
            lazyListState = lazyListState,
            emptyString = "Your Movie watchlist is still empty!",
            errorString = stringResource(id = R.string.movie_list_error)
        ) { wishlist ->
            WishlistMovieItem(
                movieWishlist = wishlist,
                navigateToMovieDetails = navigateToMovieDetail,
            )
        }
    }
}
