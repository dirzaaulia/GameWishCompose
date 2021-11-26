package com.dirzaaulia.gamewish.ui.common

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dirzaaulia.gamewish.data.model.myanimelist.MainPicture
import com.dirzaaulia.gamewish.data.model.rawg.Screenshots
import com.dirzaaulia.gamewish.data.model.tmdb.Backdrop
import com.dirzaaulia.gamewish.utils.NetworkImage
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
            modifier = it
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
            .width(100.dp)
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
    backdrops: List<Backdrop>
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
            .width(100.dp)
    ) { pageIndex ->
        val backdrop = backdrops[pageIndex]
        Card {
            backdrop.path?.let {
                NetworkImage(
                    url = it,
                    contentDescription = null
                )
            }
        }
    }
}