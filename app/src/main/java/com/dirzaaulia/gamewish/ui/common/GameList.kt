package com.dirzaaulia.gamewish.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.dirzaaulia.gamewish.data.model.Wishlist
import com.dirzaaulia.gamewish.extension.visible
import com.dirzaaulia.gamewish.ui.theme.GameWishTheme
import com.dirzaaulia.gamewish.utils.NetworkImage
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.SwipeRefreshState
import timber.log.Timber

@Composable
fun<T : Any> GameList(
    modifier: Modifier = Modifier,
    data: LazyPagingItems<T>,
    state: SwipeRefreshState,
    content: @Composable (T) -> Unit
) {
    if (data.itemCount != 0) {
        SwipeRefresh(
            state = state,
            onRefresh = { data.refresh() },
            indicator ={ indicatorState, refreshTrigger ->
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
                            item { LoadingItem() }
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
    CommonLoading(data.loadState.refresh is LoadState.Loading)
}

@Composable
fun GameListItem(
    wishlist: Wishlist,
    modifier: Modifier = Modifier,
    navigateToDetailsWishlist: (Long) -> Unit = { }
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable(
                onClick = { wishlist.id?.let { navigateToDetailsWishlist(it) } }
            ),
        elevation = 8.dp,
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

@Composable
fun LoadingItem() {
    CircularProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
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