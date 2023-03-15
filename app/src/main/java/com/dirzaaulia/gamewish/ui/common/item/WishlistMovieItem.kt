package com.dirzaaulia.gamewish.ui.common.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.dirzaaulia.gamewish.data.model.wishlist.MovieWishlist
import com.dirzaaulia.gamewish.utils.NetworkImage
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.TmdbConstant
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder

@Composable
fun WishlistMovieItem(
    modifier: Modifier = Modifier,
    movieWishlist: MovieWishlist,
    loadState: CombinedLoadStates,
    navigateToMovieDetails: (Long, String) -> Unit = { _: Long, _: String -> },

    ) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(vertical = 4.dp)
            .clickable(
                onClick = {
                    movieWishlist.id?.let { id ->
                        movieWishlist.type?.let { type ->
                            navigateToMovieDetails(id, type)
                        }
                    }
                }
            ),
        shape = MaterialTheme.shapes.large,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val url = if (movieWishlist.image.isNullOrBlank()) {
                OtherConstant.NO_IMAGE_URL
            } else {
                "${TmdbConstant.TMDB_BASE_IMAGE_URL}${movieWishlist.image}"
            }

            NetworkImage(
                url = url,
                contentDescription = null,
                modifier = modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .placeholder(
                        visible = loadState.refresh is LoadState.Loading,
                        highlight = PlaceholderHighlight.shimmer(),
                        color = MaterialTheme.colors.secondary,
                        shape = MaterialTheme.shapes.small
                    ),
                contentScale = ContentScale.FillBounds
            )
            Column(
                modifier = modifier
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    modifier = Modifier
                        .placeholder(
                            visible = loadState.refresh is LoadState.Loading,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = MaterialTheme.colors.secondary,
                            shape = MaterialTheme.shapes.small
                        ),
                    text = movieWishlist.status.toString(),
                    style = MaterialTheme.typography.caption
                )
                Text(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .placeholder(
                            visible = loadState.refresh is LoadState.Loading,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = MaterialTheme.colors.secondary,
                            shape = MaterialTheme.shapes.small
                        ),
                    text = movieWishlist.name.toString(),
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }
    }
}