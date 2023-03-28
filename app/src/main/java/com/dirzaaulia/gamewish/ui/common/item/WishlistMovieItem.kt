package com.dirzaaulia.gamewish.ui.common.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
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
                    navigateToMovieDetails(movieWishlist.id, movieWishlist.type)
                }
            ),
    ) {
        Row(
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NetworkImage(
                modifier = modifier
                    .width(100.dp)
                    .fillMaxHeight(),
                url = String.format(
                    OtherConstant.STRING_FORMAT_S_S,
                    TmdbConstant.TMDB_BASE_IMAGE_URL,
                    movieWishlist.image
                )
            )
            Column(
                modifier = modifier
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = movieWishlist.status,
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = movieWishlist.name,
                    style = MaterialTheme.typography.titleLarge,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}