package com.dirzaaulia.gamewish.ui.common.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.dirzaaulia.gamewish.data.model.myanimelist.ParentNode
import com.dirzaaulia.gamewish.utils.*

@Composable
fun CommonMyAnimeListItem(
    modifier: Modifier = Modifier,
    parentNode: ParentNode,
    type: String,
    navigateToAnimeDetails: (Long, String) -> Unit = { _: Long, _: String -> },
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 4.dp)
            .clickable(
                onClick = {
                    parentNode.node?.id?.let { id ->
                        type.doBasedOnMyAnimeListType(
                            doIfAnime = {
                                navigateToAnimeDetails(
                                    id,
                                    MyAnimeListConstant.MYANIMELIST_TYPE_ANIME
                                )
                            },
                            doIfManga = {
                                navigateToAnimeDetails(
                                    id,
                                    MyAnimeListConstant.MYANIMELIST_TYPE_MANGA
                                )
                            }

                        )
                    }
                }
            ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NetworkImage(
                url = parentNode.node?.mainPicture?.large,
                modifier = modifier
                    .width(100.dp)
                    .height(150.dp)
                    .fillMaxHeight(),
                contentScale = ContentScale.FillBounds
            )
            Column(
                modifier = modifier
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                parentNode.listStatus?.score?.let { score ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.End),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = OtherConstant.EMPTY_STRING
                        )
                        Text(
                            text = score.toString(),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                val status = parentNode.listStatus?.status.myAnimeListStatusFormatted(
                    MyAnimeListConstant.MYANIMELIST_STATUS_ALL
                )
                Text(
                    modifier = Modifier.visible(status != MyAnimeListConstant.MYANIMELIST_STATUS_ALL),
                    text = status,
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = parentNode.node?.title.toString(),
                    style = MaterialTheme.typography.headlineLarge
                )

                if (type.equals(MyAnimeListConstant.MYANIMELIST_TYPE_ANIME, true)) {
                    Text(
                        modifier = Modifier
                            .visible(status ==
                                    MyAnimeListConstant.MYANIMELIST_STATUS_PLAN_TO_WATCH
                                        .myAnimeListStatusApiFormat()
                            ),
                        text = String.format(
                            OtherConstant.STRING_FORMAT_S_SPACE_S_SPACE_S,
                            parentNode.listStatus?.episodes.toString(),
                            MyAnimeListConstant.MYANIMELIST_EPISODES,
                            MyAnimeListConstant.MYANIMELIST_WATCHED
                        ),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                else
                    Text(
                        modifier = Modifier
                            .visible(status ==
                                    MyAnimeListConstant.MYANIMELIST_STATUS_PLAN_TO_READ
                                        .myAnimeListStatusApiFormat()
                            ),
                        text = String.format(
                            OtherConstant.STRING_FORMAT_S_SPACE_S_SPACE_S,
                            parentNode.listStatus?.chapters.toString(),
                            MyAnimeListConstant.MYANIMELIST_CHAPTERS,
                            MyAnimeListConstant.MYANIMELIST_READ
                        ),
                        style = MaterialTheme.typography.labelSmall
                    )
            }
        }
    }
}