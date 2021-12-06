package com.dirzaaulia.gamewish.ui.common

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dirzaaulia.gamewish.data.model.myanimelist.MainPicture
import com.dirzaaulia.gamewish.data.model.rawg.Screenshots
import com.dirzaaulia.gamewish.data.model.tmdb.Image
import com.dirzaaulia.gamewish.utils.NetworkImage
import com.dirzaaulia.gamewish.utils.TmdbConstant
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield

@Composable
fun CommonGameCarousel(
    pagerState: PagerState,
    height: Dp?,
    screenshots: List<Screenshots>
) {
    LaunchedEffect(Unit) {
        while (true) {
            yield()
            delay(2000)
            pagerState.animateScrollToPage(
                page = (pagerState.currentPage + 1) % (pagerState.pageCount),
                animationSpec = tween(600)
            )
        }
    }

    height?.let {
        Modifier
            .fillMaxWidth()
            .height(it)
    }?.let {
        HorizontalPager(
            state = pagerState,
            modifier = it,
            count = screenshots.size
        ) { pageIndex ->
            val screenshot = screenshots[pageIndex]
            Card {
                screenshot.image?.let { url ->
                    NetworkImage(
                        url = url,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun CommonAnimeCarousel(
    pagerState: PagerState,
    screenshots: List<MainPicture>
) {
    LaunchedEffect(Unit) {
        while (true) {
            yield()
            delay(2000)
            pagerState.animateScrollToPage(
                page = (pagerState.currentPage + 1) % (pagerState.pageCount),
                animationSpec = tween(600)
            )
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier =  Modifier
            .width(100.dp),
        count = screenshots.size
    ) { pageIndex ->
        val screenshot = screenshots[pageIndex]
        Card {
            screenshot.large?.let {
                NetworkImage(
                    url = it,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun CommonMovieCarousel(
    pagerState: PagerState,
    imageList: List<Image>
) {
    LaunchedEffect(Unit) {
        while (true) {
            yield()
            delay(2000)
            tween<Float>(600)
            pagerState.animateScrollToPage(
                page = (pagerState.currentPage + 1) % (pagerState.pageCount)
            )
        }
    }

    HorizontalPager(
        state = pagerState,
        count = imageList.size
    ) { pageIndex ->
        val image = imageList[pageIndex]
        Card {
            image.filePath?.let {
                NetworkImage(
                    url = "${TmdbConstant.TMDB_BASE_IMAGE_URL}$it",
                    contentDescription = null
                )
            }
        }
    }
}