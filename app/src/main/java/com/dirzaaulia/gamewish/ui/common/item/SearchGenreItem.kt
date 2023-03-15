package com.dirzaaulia.gamewish.ui.common.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.dirzaaulia.gamewish.data.model.rawg.Genre
import com.dirzaaulia.gamewish.data.request.myanimelist.SearchGameRequest
import com.dirzaaulia.gamewish.ui.search.SearchViewModel
import com.dirzaaulia.gamewish.utils.NetworkImage
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder

@Composable
fun SearchGenreItem(
    modifier: Modifier = Modifier,
    genre: Genre,
    viewModel: SearchViewModel? = null,
    loadStates: CombinedLoadStates,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    genre.id?.let {
                        viewModel?.apply {
                            selectSearchGameTab(0)
                            setSearchGameRequest(
                                SearchGameRequest("", it, null, null)
                            )
                        }
                    }
                }
            ),
        elevation = 0.dp,
    ) {
        Column {
            val url = genre.imageBackground?.ifBlank {
                OtherConstant.NO_IMAGE_URL
            }

            NetworkImage(
                url = url.toString(),
                contentDescription = null,
                modifier = modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .placeholder(
                        visible = loadStates.refresh is LoadState.Loading,
                        highlight = PlaceholderHighlight.shimmer(),
                        color = MaterialTheme.colors.secondary,
                        shape = MaterialTheme.shapes.small
                    ),
                contentScale = ContentScale.FillBounds
            )
            Text(
                text = genre.name.toString(),
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
                    .placeholder(
                        visible = loadStates.refresh is LoadState.Loading,
                        highlight = PlaceholderHighlight.shimmer(),
                        color = MaterialTheme.colors.secondary,
                        shape = MaterialTheme.shapes.small
                    )
            )
        }
    }
}