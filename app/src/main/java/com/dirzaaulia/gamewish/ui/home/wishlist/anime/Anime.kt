package com.dirzaaulia.gamewish.ui.home.wishlist.anime

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.base.ResponseResult
import com.dirzaaulia.gamewish.data.model.myanimelist.ParentNode
import com.dirzaaulia.gamewish.utils.isError
import com.dirzaaulia.gamewish.utils.isSucceeded
import com.dirzaaulia.gamewish.utils.visible
import com.dirzaaulia.gamewish.ui.common.MyAnimeListWebViewClient
import com.dirzaaulia.gamewish.ui.common.item.CommonAnimeItem
import com.dirzaaulia.gamewish.ui.common.list.AnimeVerticalList
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.utils.MyAnimeListConstant
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder

@Composable
fun WishlistAnime(
    accessTokenResult: ResponseResult<String>?,
    viewModel: HomeViewModel,
    lazyListState: LazyListState,
    data: LazyPagingItems<ParentNode>,
    animeType: String,
    emptyString: String,
    errorString: String,
    animeStatusFormatted: String,
    navigateToAnimeDetails: (Long, String) -> Unit,
) {
    when {
        accessTokenResult.isSucceeded -> {
            MyAnimeListLoggedIn(
                animeStatusFormatted,
                data,
                lazyListState,
                emptyString,
                errorString,
                viewModel,
                navigateToAnimeDetails,
                animeType
            )
        }
        accessTokenResult.isError -> {
            MyAnimeListWebViewClient(
                from = MyAnimeListConstant.MYANIMELIST_WEBVIEW_WISHLIST,
                viewModel = viewModel,
            )
        }
    }
}

@Composable
private fun MyAnimeListLoggedIn(
    animeStatusFormatted: String,
    data: LazyPagingItems<ParentNode>,
    lazyListState: LazyListState,
    emptyString: String,
    errorString: String,
    viewModel: HomeViewModel,
    navigateToAnimeDetails: (Long, String) -> Unit,
    animeType: String
) {
    Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
        Text(
            text = stringResource(R.string.sort_by, animeStatusFormatted),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 8.dp)
                .align(Alignment.End)
                .placeholder(
                    visible = data.loadState.refresh is LoadState.Loading,
                    highlight = PlaceholderHighlight.shimmer(),
                    color = MaterialTheme.colors.secondary,
                    shape = MaterialTheme.shapes.small
                )
                .visible(data.loadState.refresh is LoadState.NotLoading)
        )
        AnimeVerticalList(
            data = data,
            lazyListState = lazyListState,
            emptyString = emptyString,
            errorString = errorString,
            viewModel = viewModel
        ) { parentNode ->
            CommonAnimeItem(
                parentNode = parentNode,
                navigateToAnimeDetails = navigateToAnimeDetails,
                type = animeType,
                loadState = data.loadState
            )
        }
    }
}