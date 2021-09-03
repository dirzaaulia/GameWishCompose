package com.dirzaaulia.gamewish.ui.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.rawg.GameDetails
import com.dirzaaulia.gamewish.data.model.rawg.Screenshots
import com.dirzaaulia.gamewish.extension.visible
import com.dirzaaulia.gamewish.ui.common.CommonGameCarousel
import com.dirzaaulia.gamewish.ui.common.CommonLoading
import com.dirzaaulia.gamewish.ui.common.GameDetailsPlatformList
import com.dirzaaulia.gamewish.ui.common.GameDetailsStoresList
import com.dirzaaulia.gamewish.ui.theme.White
import com.dirzaaulia.gamewish.utils.*
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.rememberPagerState


@Composable
fun GameDetails(
    viewModel: DetailsViewModel,
    upPress: () -> Unit
) {
    val gameDetails = viewModel.gameDetails.value
    val screenshots = viewModel.gameDetailsScreenshots.value
    val loading = viewModel.loading.value

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            GameDetailsHeader(
                screenshots = screenshots,
                loading = loading,
                upPress = upPress
            )
        }

        item {
            gameDetails?.let { GameDetailsMiddleContent(it) }
        }
    }
    CommonLoading(visibility = loading)
}

@Composable
fun GameDetailsHeader(
    screenshots: List<Screenshots>,
    loading: Boolean,
    upPress: () -> Unit
) {
    Box {
        if (!screenshots.isNullOrEmpty()) {
            val pagerState = rememberPagerState(
                pageCount = screenshots.size,
                initialOffscreenLimit = 2,
            )

            CommonGameCarousel(
                pagerState = pagerState,
                height = 300.dp,
                screenshots = screenshots
            )
        } else if (screenshots.isEmpty()) {
            NetworkImage(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .visible(!loading),
                url = OtherConstant.NO_IMAGE_URL,
                contentDescription = null
            )
        }
        TopAppBar(
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
            contentColor = White,
            modifier = Modifier
                .visible(!loading)
                .statusBarsPadding()
        ) {
            IconButton(onClick = upPress) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.label_back)
                )
            }
        }
    }
}

@Composable
fun GameDetailsMiddleContent(data: GameDetails) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.data_by_rawg),
            style = MaterialTheme.typography.caption
        )
        data.name?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.h4
            )
        }
        Row {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                data.released?.let {
                    Text(
                        text = stringResource(id = R.string.release_date),
                        style = MaterialTheme.typography.h6

                    )
                    Text(
                        text = textDateFormatter2(it),
                        style = MaterialTheme.typography.body2
                    )
                }
                data.developers?.let {
                    Text(
                        text = stringResource(id = R.string.developer),
                        style = MaterialTheme.typography.h6
                    )
                    Text(
                        text = gameDeveloperFormatter(it),
                        style = MaterialTheme.typography.body2
                    )
                }
                data.publishers?.let {
                    Text(
                        text = stringResource(id = R.string.publishers),
                        style = MaterialTheme.typography.h6
                    )
                    Text(
                        text = gamePublishersFormatter(it),
                        style = MaterialTheme.typography.body2
                    )
                }
                data.website?.let { url ->
                    Text(
                        text = stringResource(id = R.string.link),
                        style = MaterialTheme.typography.h6
                    )
                    ClickableText(
                        text = AnnotatedString(
                            "Homepage",
                            SpanStyle(
                                color = MaterialTheme.colors.onSurface,
                                textDecoration = TextDecoration.Underline
                            )
                        ),
                        style = MaterialTheme.typography.body2,
                        onClick = { openLink(context, url) }
                    )
                }
                data.redditUrl?.let { url ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_reddit_logo),
                            contentDescription = null
                        )
                        ClickableText(
                            text = AnnotatedString(
                                getSubReddit(url),
                                SpanStyle(
                                    color = MaterialTheme.colors.onSurface,
                                    textDecoration = TextDecoration.Underline
                                )
                            ),
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier.weight(1f),
                            onClick = { openLink(context, url) }
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                data.esrbRating?.let {
                    Text(
                        text = stringResource(id = R.string.esrb_rating),
                        style = MaterialTheme.typography.h6
                    )
                    Image(
                        painter = painterResource(id = esrbRatingFormatter(it)),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp, 70.dp)
                    )
                }
            }
        }
        data.platforms?.let {
            Text(
                text = stringResource(id = R.string.platforms),
                style = MaterialTheme.typography.h6
            )
            GameDetailsPlatformList(data = it, code = 0)
        }
        data.stores?.let {
            Text(
                text = stringResource(id = R.string.stores),
                style = MaterialTheme.typography.h6
            )
            GameDetailsStoresList(data = it, code = 1)
        }
        data.description?.let {
            Text(
                text = stringResource(id = R.string.description),
                style = MaterialTheme.typography.h6
            )
            Text(
                text = AnnotatedString(htmlToTextFormatter(it).toString()),
                style = MaterialTheme.typography.body1
            )
        }
    }
}