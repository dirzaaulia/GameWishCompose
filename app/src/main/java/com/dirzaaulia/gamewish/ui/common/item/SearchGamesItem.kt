package com.dirzaaulia.gamewish.ui.common.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.dirzaaulia.gamewish.data.model.rawg.Games
import com.dirzaaulia.gamewish.utils.changeDateFormat
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder

@Composable
fun SearchGamesItem(
    modifier: Modifier = Modifier,
    navigateToGameDetails: (Long) -> Unit = { },
    games: Games,
    loadStates: CombinedLoadStates,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = { games.id?.let { navigateToGameDetails(it) } }
            ),
        elevation = 0.dp,
    ) {
        Column(modifier = modifier.padding(top = 4.dp)) {
            Text(
                text = "Release Date : ${games.released?.changeDateFormat("yyyy-MM-dd")}",
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
                    .placeholder(
                        visible = loadStates.refresh is LoadState.Loading,
                        highlight = PlaceholderHighlight.shimmer(),
                        color = MaterialTheme.colors.secondary,
                        shape = MaterialTheme.shapes.small
                    )
            )
            Text(
                text = games.name.toString(),
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding(start = 8.dp, end = 4.dp, top = 4.dp)
                    .fillMaxWidth()
                    .placeholder(
                        visible = loadStates.refresh is LoadState.Loading,
                        highlight = PlaceholderHighlight.shimmer(),
                        color = MaterialTheme.colors.secondary,
                        shape = MaterialTheme.shapes.small
                    )
            )
            Divider(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            )
        }
    }
}