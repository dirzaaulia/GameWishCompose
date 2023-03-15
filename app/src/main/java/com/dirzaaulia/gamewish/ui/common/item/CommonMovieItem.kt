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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.tmdb.Movie
import com.dirzaaulia.gamewish.utils.NetworkImage
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.TmdbConstant
import com.dirzaaulia.gamewish.utils.changeDateFormat

@Composable
fun CommonMovieItem(
    modifier: Modifier = Modifier,
    movie: Movie,
    navigateToDetails: (Long, String) -> Unit,
    type: String,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(vertical = 4.dp)
            .clickable(
                onClick = {
                    movie.id?.let {
                        if (type.equals("Movie", true)) {
                            navigateToDetails(it, "Movie")
                        } else {
                            navigateToDetails(it, "TV Show")
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
            movie.posterPath.let {
                val url = if (it.isNullOrBlank()) {
                    OtherConstant.NO_IMAGE_URL
                } else {
                    "${TmdbConstant.TMDB_BASE_IMAGE_URL}$it"
                }

                NetworkImage(
                    url = url,
                    contentDescription = null,
                    modifier = modifier
                        .width(100.dp)
                        .fillMaxHeight(),
                    contentScale = ContentScale.FillBounds
                )
            }
            Column(
                modifier = modifier
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .weight(1f)
            ) {
                movie.releaseDate?.let {
                    val releaseDate = if (it.isBlank()) {
                        stringResource(R.string.no_release_date)
                    } else {
                        it.changeDateFormat("yyyy-MM-dd")
                    }

                    Text(
                        text = "Release Date : $releaseDate",
                        style = MaterialTheme.typography.caption
                    )
                }
                movie.title?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                movie.name?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.subtitle1
                    )
                }
            }
        }
    }
}