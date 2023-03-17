package com.dirzaaulia.gamewish.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.compose.rememberImagePainter
import com.dirzaaulia.gamewish.ui.common.CommonLoading

/**
 * A wrapper around [Image] and [rememberImagePainter], setting a
 * default [contentScale] and showing content while loading.
 */
@Composable
fun NetworkImage(
    modifier: Modifier = Modifier,
    url: String?,
    contentDescription: String = OtherConstant.EMPTY_STRING,
    contentScale: ContentScale = ContentScale.FillBounds
) {

    SubcomposeAsyncImage(
        modifier = modifier,
        model = url
            .replaceIfNull(OtherConstant.NO_IMAGE_URL)
            .ifBlank {
                OtherConstant.NO_IMAGE_URL
            },
        contentDescription = contentDescription,
        contentScale = contentScale,
    ) {
        when (painter.state) {
            is AsyncImagePainter.State.Loading -> {
                CommonLoading(visibility = true)
            }

            is AsyncImagePainter.State.Success -> {
                SubcomposeAsyncImageContent()
            }

            is AsyncImagePainter.State.Error -> {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        imageVector = Icons.Filled.BrokenImage,
                        contentDescription = OtherConstant.EMPTY_STRING
                    )
                }
            }

            AsyncImagePainter.State.Empty -> Unit
        }
    }
}