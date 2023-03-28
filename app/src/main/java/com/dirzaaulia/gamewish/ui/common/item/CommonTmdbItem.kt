package com.dirzaaulia.gamewish.ui.common.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dirzaaulia.gamewish.data.model.tmdb.Movie
import com.dirzaaulia.gamewish.utils.NetworkImage
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.TmdbConstant
import com.dirzaaulia.gamewish.utils.doBasedOnTmdbType
import com.dirzaaulia.gamewish.utils.formatTmdbReleaseDate
import com.dirzaaulia.gamewish.utils.replaceIfNull

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
                    movie.posterPath
                ),
            )
            Column(
                modifier = modifier
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Bottom
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
                when (type) {
                    TmdbConstant.TMDB_TYPE_MOVIE -> {
                        Text(
                            text = movie.title.replaceIfNull(),
                            style = MaterialTheme.typography.titleLarge,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    TmdbConstant.TMDB_TYPE_TVSHOW -> {
                        Text(
                            text = movie.name.replaceIfNull(),
                            style = MaterialTheme.typography.titleLarge,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}