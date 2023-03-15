package com.dirzaaulia.gamewish.ui.common.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.dirzaaulia.gamewish.data.model.myanimelist.ParentNode
import com.dirzaaulia.gamewish.utils.visible
import com.dirzaaulia.gamewish.ui.common.CommonLoadingItem
import com.dirzaaulia.gamewish.ui.common.CommonPagingAppendError
import com.dirzaaulia.gamewish.ui.common.ErrorConnect
import com.dirzaaulia.gamewish.ui.common.item.CommonAnimeItem
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import timber.log.Timber

@Composable
fun <T : Any> AnimeVerticalList(
    data: LazyPagingItems<T>,
    lazyListState: LazyListState,
    emptyString: String,
    errorString: String,
    viewModel: HomeViewModel,
    content: @Composable (T) -> Unit
) {
    when {
        data.loadState.refresh is LoadState.Loading -> {
            //TODO() Updating Placeholder
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxSize(),
            ) {
                items(10) {
                    CommonAnimeItem(
                        parentNode = ParentNode(),
                        loadState = data.loadState,
                        type = "Anime"
                    )
                }
            }
        }

        data.loadState.refresh is LoadState.Error -> {
            val error = data.loadState.refresh as LoadState.Error
            val errorMessage = error.error.message
            if (errorMessage != null) {
                if (errorMessage.contains("HTTP 401", true)) {
                    viewModel.getMyAnimeListRefreshToken()
                } else {
                    ErrorConnect(text = errorString) {
                        data.retry()
                    }
                }
            }
        }

        data.itemCount != 0 && data.loadState.refresh is LoadState.NotLoading -> {
            AnimeListNotEmpty(
                lazyListState =  lazyListState,
                data = data,
                content = content,
                errorString = errorString
            )
        }

        data.itemCount == 0 && data.loadState.refresh is LoadState.NotLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emptyString, 
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }
    }
}

@Composable
private fun <T : Any> AnimeListNotEmpty(
    lazyListState: LazyListState,
    data: LazyPagingItems<T>,
    content: @Composable (T) -> Unit,
    errorString: String
) {
    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .visible(data.loadState.refresh !is LoadState.Loading),
    ) {

        items(data) { data ->
            data?.let {
                content.invoke(data)
            }
        }

        data.apply {
            when {
                loadState.append is LoadState.Loading -> {
                    item { CommonLoadingItem() }
                }

                loadState.refresh is LoadState.Error -> {
                    val error = data.loadState.refresh as LoadState.Error
                    item {
                        Timber.e("Refresh Error: ${error.error.localizedMessage}")
                    }
                }

                loadState.append is LoadState.Error -> {
                    item {
                        Timber.e("Append Error: Not Loading")
                        CommonPagingAppendError(
                            message = errorString,
                            retry = { retry() }
                        )
                    }
                }
            }
        }
    }
}