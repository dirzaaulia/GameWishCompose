package com.dirzaaulia.gamewish.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.ui.common.placeholder.CommonItemPlaceholder
import com.dirzaaulia.gamewish.utils.MyAnimeListConstant
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.PlaceholderConstant
import com.dirzaaulia.gamewish.utils.replaceIfNull

@Composable
fun <T : Any> CommonVerticalList(
    modifier: Modifier = Modifier,
    data: LazyPagingItems<T>,
    lazyListState: LazyListState,
    placeholderType: Int = PlaceholderConstant.DEFAULT,
    emptyString: String,
    errorString: String,
    doWhenMyAnimeListError: () -> Unit = { },
    content: @Composable (T) -> Unit
) {
    when {
        data.loadState.refresh is LoadState.Loading -> {
            CommonVerticalListLoading(
                lazyListState = lazyListState,
                placeholderType = placeholderType
            )
        }

        data.loadState.refresh is LoadState.Error -> {
            val error = data.loadState.refresh as LoadState.Error
            val errorMessage = error.error.message.replaceIfNull(errorString)
            if (errorMessage.contains(
                    other = MyAnimeListConstant.MYANIMELIST_HTTP_401_ERROR,
                    ignoreCase = true
                )
            ) doWhenMyAnimeListError.invoke()
            else ErrorConnect(text = errorMessage) { data.retry() }
        }

        data.itemCount != OtherConstant.ZERO
                && data.loadState.refresh is LoadState.NotLoading -> {
            CommonVerticalListNotEmpty(
                modifier = modifier,
                data = data,
                errorString = errorString,
                content = content,
            )
        }

        data.itemCount == OtherConstant.ZERO
                && data.loadState.refresh is LoadState.NotLoading -> {
            CommonVerticalListEmpty(emptyString = emptyString)
        }
    }
}

@Composable
private fun CommonVerticalListLoading(
    lazyListState: LazyListState,
    placeholderType: Int,
) {
    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize(),
    ) {
        items(OtherConstant.FIFTY) {
            when (placeholderType) {
                PlaceholderConstant.GAME_WISHLIST -> {
                    CommonItemPlaceholder(
                        height = 240.dp,
                        shape = MaterialTheme.shapes.small
                    )
                }
                PlaceholderConstant.MOVIE_WISHLIST -> {
                    CommonItemPlaceholder(
                        height = 240.dp,
                        shape = MaterialTheme.shapes.medium
                    )
                }
                PlaceholderConstant.DEALS -> {
                    CommonItemPlaceholder(
                        height = 150.dp,
                        shape = MaterialTheme.shapes.medium
                    )
                }
                PlaceholderConstant.SEARCH_GAME_TAB -> {
                    CommonItemPlaceholder(
                        height = 240.dp,
                        shape = MaterialTheme.shapes.large
                    )
                }
                PlaceholderConstant.SEARCH_GAME -> {
                    CommonItemPlaceholder(
                        height = 120.dp,
                        shape = MaterialTheme.shapes.large
                    )
                }
                PlaceholderConstant.ANIME -> {
                    CommonItemPlaceholder(
                        height = 150.dp,
                        shape = MaterialTheme.shapes.medium
                    )
                }
                else -> {
                    CommonItemPlaceholder(
                        height = 240.dp,
                        shape = MaterialTheme.shapes.medium
                    )
                }
            }
        }
    }
}

@Composable
private fun CommonVerticalListEmpty(emptyString: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emptyString,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun <T : Any> CommonVerticalListNotEmpty(
    modifier: Modifier = Modifier,
    data: LazyPagingItems<T>,
    errorString: String,
    content: @Composable (T) -> Unit,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(data) { data ->
            data?.let {
                content.invoke(data)
            }
        }
        data.apply {
            when {
                loadState.append is LoadState.Loading -> item { CommonLoadingItem() }

                loadState.refresh is LoadState.Error -> item {
                    ErrorConnect(text = errorString) { retry() }
                }

                loadState.append is LoadState.Error -> item {
                    CommonPagingAppendError(
                        message = errorString,
                        retry = { retry() }
                    )
                }
            }
        }
    }
}

@Composable
fun CommonPagingAppendError(
    message: String,
    retry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = { retry() }
        ) {
            Text(text = stringResource(R.string.try_again))
        }
    }
}
