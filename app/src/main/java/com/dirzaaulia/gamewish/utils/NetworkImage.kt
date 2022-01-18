package com.dirzaaulia.gamewish.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.extension.visible
import com.dirzaaulia.gamewish.ui.common.CommonLoading
import com.dirzaaulia.gamewish.theme.compositedOnSurface

/**
 * A wrapper around [Image] and [rememberImagePainter], setting a
 * default [contentScale] and showing content while loading.
 */
@OptIn(ExperimentalCoilApi::class)
@Composable
fun NetworkImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.FillBounds,
    placeholderColor: Color? = MaterialTheme.colors.compositedOnSurface(0.2f)
) {

    Box(modifier) {
        val painter = rememberImagePainter(
            data = url,
            builder = {
                placeholder(drawableResId = R.drawable.ic_gamewish)
            }
        )

        CommonLoading(visibility = painter.state is ImagePainter.State.Loading)
        Image(
            painter = painter,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = Modifier.fillMaxSize()
                .visible(painter.state is ImagePainter.State.Success)
        )

//        if (painter.state is ImagePainter.State.Loading && placeholderColor != null) {
//            Spacer(
//                modifier = Modifier
//                    .matchParentSize()
//                    .background(placeholderColor)
//            )
//        }
    }
}

///**
// * A Coil [Interceptor] which appends query params to Unsplash urls to request sized images.
// */
//@OptIn(ExperimentalCoilApi::class)
//object UnsplashSizingInterceptor : Interceptor {
//    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
//        val data = chain.request.data
//        val size = chain.size
//        if (data is String &&
//            data.startsWith("https://images.unsplash.com/photo-") &&
//            size is PixelSize
//        ) {
//            val url = data.toHttpUrl()
//                .newBuilder()
//                .addQueryParameter("w", size.width.toString())
//                .addQueryParameter("h", size.height.toString())
//                .build()
//            val request = chain.request.newBuilder().data(url).build()
//            return chain.proceed(request)
//        }
//        return chain.proceed(chain.request)
//    }
//}
