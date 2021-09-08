package com.dirzaaulia.gamewish.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.Wishlist
import com.dirzaaulia.gamewish.extension.visible
import com.dirzaaulia.gamewish.ui.theme.GameWishTheme
import com.dirzaaulia.gamewish.utils.NetworkImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.SwipeRefreshState
import timber.log.Timber

@Composable
fun <T : Any> GameList(
    modifier: Modifier = Modifier,
    data: LazyPagingItems<T>,
    state: SwipeRefreshState,
    lazyListState: LazyListState,
    content: @Composable (T) -> Unit
) {
    if (data.itemCount != 0) {
        SwipeRefresh(
            state = state,
            onRefresh = { data.refresh() },
            indicator = { indicatorState, refreshTrigger ->
                SwipeRefreshIndicator(
                    state = indicatorState,
                    refreshTriggerDistance = refreshTrigger,
                    contentColor = MaterialTheme.colors.primary
                )
            },
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .visible(data.loadState.refresh !is LoadState.Loading)
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
                            val error = data.loadState.refresh as LoadState.Error
                            item {
                                Timber.e("Append Error: ${error.error.localizedMessage}")
                            }
                        }
                    }
                }
            }
        }
    }

    if (data.itemCount == 0 && data.loadState.refresh is LoadState.Error) {
        ErrorConnect(text = stringResource(id = R.string.deals_error)) {
            data.retry()
        }
    }

    if (data.itemCount == 0 && (data.loadState.append is LoadState.NotLoading
                || data.loadState.refresh is LoadState.NotLoading )) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = "Your game wishlist still empty!",
                style = MaterialTheme.typography.subtitle1)
        }
    }

    CommonLoading(data.loadState.refresh is LoadState.Loading)
}

@Composable
fun GameListItem(
    wishlist: Wishlist,
    modifier: Modifier = Modifier,
    selectGame: (Long) -> Unit = { }
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = { wishlist.id?.let { selectGame(it) } }
            ),
        elevation = 0.dp,
    ) {
        Column {
            wishlist.image?.let { imageUrl ->
                NetworkImage(
                    url = imageUrl,
                    contentDescription = null,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
            wishlist.status?.let { status ->
                Text(
                    text = status,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, start = 8.dp)
                )
            }
            wishlist.name?.let { name ->
                Text(
                    text = name,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, bottom = 4.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewGameListItemLight() {
    GameWishTheme(
        darkTheme = false
    ) {
        GameListItem(wishlist = Wishlist.mock())
    }
}

@Preview
@Composable
fun PreviewGameListItemDark() {
    GameWishTheme(
        darkTheme = true
    ) {
        GameListItem(wishlist = Wishlist.mock())
    }
}