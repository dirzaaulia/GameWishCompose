package com.dirzaaulia.gamewish.ui.common

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dirzaaulia.gamewish.data.model.myanimelist.MainPicture
import com.dirzaaulia.gamewish.data.model.rawg.Screenshots
import com.dirzaaulia.gamewish.data.model.tmdb.Image
import com.dirzaaulia.gamewish.utils.NetworkImage
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.TmdbConstant
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield

@Composable
fun CommonGameCarousel(
    pagerState: PagerState,
    height: Dp,
    screenshots: List<Screenshots>
) {
    LaunchedEffect(Unit) {
        while (true) {
            yield()
            delay(OtherConstant.TWO_THOUSAND_LONG)
            tween<Float>(OtherConstant.SIX_HUNDRED)
            if (pagerState.pageCount != OtherConstant.ZERO) {
                pagerState.animateScrollToPage(
                    page = (pagerState.currentPage + OtherConstant.ONE) % (pagerState.pageCount)
                )
            }
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.height(height),
        count = screenshots.size
    ) { pageIndex ->
        val screenshot = screenshots[pageIndex]
        Card(shape = MaterialTheme.shapes.large) {
            NetworkImage(
                url = screenshot.image,
                contentDescription = OtherConstant.EMPTY_STRING
            )
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
            delay(OtherConstant.TWO_THOUSAND_LONG)
            tween<Float>(OtherConstant.SIX_HUNDRED)
            pagerState.animateScrollToPage(
                page = (pagerState.currentPage + OtherConstant.ONE) % (pagerState.pageCount)
            )
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .width(100.dp),
        count = screenshots.size
    ) { pageIndex ->
        val screenshot = screenshots[pageIndex]
        Card(modifier = Modifier.fillMaxSize()) {
            screenshot.large?.let { image ->
                NetworkImage(
                    modifier = Modifier.fillMaxSize(),
                    url = image,
                    contentDescription = OtherConstant.EMPTY_STRING
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
            delay(OtherConstant.TWO_THOUSAND_LONG)
            tween<Float>(OtherConstant.SIX_HUNDRED)
            pagerState.animateScrollToPage(
                page = (pagerState.currentPage + OtherConstant.ONE) % (pagerState.pageCount)
            )
        }
    }

    HorizontalPager(
        state = pagerState,
        count = imageList.size
    ) { pageIndex ->
        val image = imageList[pageIndex]
        Card(shape = MaterialTheme.shapes.large) {
            image.filePath?.let { filePath ->
                NetworkImage(
                    url = String.format(
                        OtherConstant.STRING_FORMAT_S_S,
                        TmdbConstant.TMDB_BASE_IMAGE_URL,
                        filePath
                    ),
                )
            }
        }
    }
}