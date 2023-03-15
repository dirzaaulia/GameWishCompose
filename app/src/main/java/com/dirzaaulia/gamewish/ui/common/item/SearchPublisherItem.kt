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
import com.dirzaaulia.gamewish.data.model.rawg.Publisher
import com.dirzaaulia.gamewish.data.request.myanimelist.SearchGameRequest
import com.dirzaaulia.gamewish.ui.search.SearchViewModel
import com.dirzaaulia.gamewish.utils.NetworkImage

@Composable
fun SearchPublisherItem(
    modifier: Modifier = Modifier,
    publisher: Publisher,
    viewModel: SearchViewModel,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    publisher.id?.let {
                        viewModel.apply {
                            selectSearchGameTab(0)
                            setSearchGameRequest(
                                SearchGameRequest("", null, it, null)
                            )
                        }
                    }
                }
            ),
        elevation = 0.dp,
    ) {
        Column {
            publisher.imageBackground?.let { imageUrl ->
                NetworkImage(
                    url = imageUrl,
                    contentDescription = null,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
            publisher.name?.let { name ->
                Text(
                    text = name,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, bottom = 4.dp)
                )
            }
        }
    }
}