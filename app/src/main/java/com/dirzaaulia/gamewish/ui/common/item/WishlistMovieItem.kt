package com.dirzaaulia.gamewish.ui.common.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
                    .height(150.dp)
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
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = movieWishlist.name.toString(),
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }
    }
}