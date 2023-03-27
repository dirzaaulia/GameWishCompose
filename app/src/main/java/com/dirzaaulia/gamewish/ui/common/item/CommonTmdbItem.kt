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
import com.dirzaaulia.gamewish.data.model.tmdb.Movie
import com.dirzaaulia.gamewish.utils.*

@Composable
fun CommonTmdbItem(
    modifier: Modifier = Modifier,
    movie: Movie,
    navigateToDetails: (Long, String) -> Unit,
    type: String,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 4.dp)
            .clickable(
                onClick = {
                    type.doBasedOnTmdbType(
                        doIfMovie = { navigateToDetails(movie.id, TmdbConstant.TMDB_TYPE_MOVIE) },
                        doIfTv = { navigateToDetails(movie.id, TmdbConstant.TMDB_TYPE_TVSHOW) }
                    )
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
                    movie.posterPath
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
                    text = String.format(
                        OtherConstant.STRING_FORMAT_S_SPACE_S_SPACE_S,
                        TmdbConstant.TMDB_RELEASE_DATE,
                        OtherConstant.COLON,
                        movie.releaseDate.formatTmdbReleaseDate()
                    ),
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = movie.title.replaceIfNull(),
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = movie.name.replaceIfNull(),
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }
    }
}