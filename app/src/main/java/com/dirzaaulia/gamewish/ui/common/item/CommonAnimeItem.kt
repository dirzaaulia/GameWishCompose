package com.dirzaaulia.gamewish.ui.common.item

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.dirzaaulia.gamewish.data.model.myanimelist.ParentNode
import com.dirzaaulia.gamewish.utils.NetworkImage
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.capitalizeWords
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder

@Composable
fun CommonAnimeItem(
    modifier: Modifier = Modifier,
    parentNode: ParentNode,
    type: String,
    loadState: CombinedLoadStates? = null,
    navigateToAnimeDetails: (Long, String) -> Unit = { _: Long, _: String -> },
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(vertical = 4.dp)
            .clickable(
                onClick = {
                    parentNode.node?.id?.let {
                        if (type.equals("Anime", true)) {
                            navigateToAnimeDetails(it, "Anime")
                        } else {
                            navigateToAnimeDetails(it, "Manga")
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
            val url = parentNode.node?.mainPicture?.large?.ifBlank {
                OtherConstant.NO_IMAGE_URL
            }

            NetworkImage(
                url = url.toString(),
                contentDescription = null,
                modifier = modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .placeholder(
                        visible = loadState?.refresh is LoadState.Loading,
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
                val isScorePlaceholderVisible = if (loadState?.refresh is LoadState.Loading) {
                    true
                } else loadState?.refresh is LoadState.NotLoading && parentNode.listStatus?.score != null

                AnimatedVisibility(
                    modifier = Modifier
                        .placeholder(
                            visible = loadState?.refresh is LoadState.Loading,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = MaterialTheme.colors.secondary,
                            shape = MaterialTheme.shapes.small
                        )
                        .align(Alignment.End),
                    visible = isScorePlaceholderVisible
                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null
                        )
                        Text(
                            text = parentNode.listStatus?.score.toString(),
                            style = MaterialTheme.typography.caption
                        )
                    }
                }

                var statusFormatted = parentNode.listStatus?.status
                    .toString()
                    .replace("_"," ")
                statusFormatted = statusFormatted.capitalizeWords()

                val isStatusPlaceholderVisible = if (loadState?.refresh is LoadState.Loading) {
                    true
                } else loadState?.refresh is LoadState.NotLoading && parentNode.listStatus?.status != null

                AnimatedVisibility(
                    modifier = Modifier
                        .placeholder(
                            visible = loadState?.refresh is LoadState.Loading,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = MaterialTheme.colors.secondary,
                            shape = MaterialTheme.shapes.small
                        ),
                    visible = isStatusPlaceholderVisible
                ) {
                    Text(
                        text = statusFormatted,
                        style = MaterialTheme.typography.body2
                    )
                }

                val isRelationTypePlaceholderVisible = if (loadState?.refresh is LoadState.Loading) {
                    true
                } else if (loadState?.refresh is LoadState.NotLoading && parentNode.relationType != null) {
                    true
                } else loadState == null && parentNode.relationType != null

                AnimatedVisibility(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .placeholder(
                            visible = loadState?.refresh is LoadState.Loading,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = MaterialTheme.colors.secondary,
                            shape = MaterialTheme.shapes.small
                        ),
                    visible = isRelationTypePlaceholderVisible
                ) {
                    Text(
                        text = parentNode.relationType.toString(),
                        style = MaterialTheme.typography.body2
                    )
                }

                Text(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .placeholder(
                            visible = loadState?.refresh is LoadState.Loading,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = MaterialTheme.colors.secondary,
                            shape = MaterialTheme.shapes.small
                        ),
                    text = parentNode.node?.title.toString(),
                    style = MaterialTheme.typography.subtitle1
                )

                if (type.equals("Anime", true)) {
                    if (!parentNode.listStatus?.status.equals("plan_to_watch", true)) {
                        parentNode.listStatus?.episodes?.let {
                            Text(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .placeholder(
                                        visible = loadState?.refresh is LoadState.Loading,
                                        highlight = PlaceholderHighlight.shimmer(),
                                        color = MaterialTheme.colors.secondary,
                                        shape = MaterialTheme.shapes.small
                                    ),
                                text = "$it Episodes Watched",
                                style = MaterialTheme.typography.caption
                            )
                        }
                    }
                } else {
                    if (!parentNode.listStatus?.status.equals("plan_to_read", true)) {
                        parentNode.listStatus?.chapters?.let {
                            Text(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .placeholder(
                                        visible = loadState?.refresh is LoadState.Loading,
                                        highlight = PlaceholderHighlight.shimmer(),
                                        color = MaterialTheme.colors.secondary,
                                        shape = MaterialTheme.shapes.small
                                    ),
                                text = "$it Chapters Watched",
                                style = MaterialTheme.typography.caption
                            )
                        }
                    }
                }
            }
        }
    }
}