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
import com.dirzaaulia.gamewish.extension.isError
import com.dirzaaulia.gamewish.extension.isSucceeded
import com.dirzaaulia.gamewish.extension.visible
import com.dirzaaulia.gamewish.ui.common.AnimeVerticalList
import com.dirzaaulia.gamewish.ui.common.CommonAnimeItem
import com.dirzaaulia.gamewish.ui.common.MyAnimeListWebViewClient
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.utils.capitalizeWords
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder

@Composable
fun WishlistAnime(
    accessTokenResult: ResponseResult<String>?,
    viewModel: HomeViewModel,
    lazyListState: LazyListState,
    data: LazyPagingItems<ParentNode>,
    animeStatus: String,
    navigateToAnimeDetails: (Long, String) -> Unit,
) {
    var animeStatusFormatted = animeStatus
    animeStatusFormatted = animeStatusFormatted.replace("_", " ")
    animeStatusFormatted = animeStatusFormatted.capitalizeWords()

    if (animeStatusFormatted.isBlank()) {
        animeStatusFormatted = "All"
    }

    Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
        Text(
            text = "Sort by : $animeStatusFormatted",
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
            emptyString = "Your Anime list is still empty!",
            errorString = stringResource(id = R.string.anime_list_error),
            viewModel = viewModel
        ) { parentNode ->
            CommonAnimeItem(
                parentNode = parentNode,
                navigateToAnimeDetails = navigateToAnimeDetails,
                type = "Anime",
                loadState = data.loadState
            )
        }
    }

    when {
        accessTokenResult.isSucceeded -> {

        }
        accessTokenResult.isError -> {
            MyAnimeListWebViewClient(
                from = 0,
                viewModel = viewModel
            )
        }
    }
}