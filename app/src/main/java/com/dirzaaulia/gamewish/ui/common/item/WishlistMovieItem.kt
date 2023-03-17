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
import com.dirzaaulia.gamewish.data.model.wishlist.MovieWishlist
import com.dirzaaulia.gamewish.utils.NetworkImage
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.TmdbConstant

@Composable
fun WishlistMovieItem(
    modifier: Modifier = Modifier,
    movieWishlist: MovieWishlist,
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
            NetworkImage(
                url = String.format(
                    OtherConstant.STRING_FORMAT_S_S,
                    TmdbConstant.TMDB_BASE_IMAGE_URL,
                    movieWishlist.image
                ),
                modifier = modifier
                    .width(100.dp)
                    .fillMaxHeight(),
                contentScale = ContentScale.FillBounds
            )
            Column(
                modifier = modifier
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = movieWishlist.status.toString(),
                    style = MaterialTheme.typography.caption
                )
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = movieWishlist.name.toString(),
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }
    }
}