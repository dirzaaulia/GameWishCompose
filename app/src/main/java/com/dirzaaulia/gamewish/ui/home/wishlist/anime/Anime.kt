package com.dirzaaulia.gamewish.ui.home.wishlist.anime

import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.dirzaaulia.gamewish.base.ResponseResult
import com.dirzaaulia.gamewish.extension.isError
import com.dirzaaulia.gamewish.extension.isSucceeded
import com.dirzaaulia.gamewish.ui.common.WebViewMyAnimeList
import com.dirzaaulia.gamewish.ui.home.HomeViewModel

@Composable
fun WishlistAnime(
    accessTokenResult: ResponseResult<String>?,
    accessToken: String?,
    viewModel: HomeViewModel
) {
    when {
        accessTokenResult.isSucceeded -> {
            Box(contentAlignment = Alignment.Center,) {
                Text(
                    text = "MyAnimeList Token Saved! $accessToken",
                    style = MaterialTheme.typography.h3
                )
            }
        }
        accessTokenResult.isError -> {
            WebViewMyAnimeList(viewModel = viewModel)
        }
    }
}