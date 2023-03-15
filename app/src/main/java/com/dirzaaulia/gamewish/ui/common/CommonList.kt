package com.dirzaaulia.gamewish.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import com.dirzaaulia.gamewish.data.model.myanimelist.ParentNode
import com.dirzaaulia.gamewish.data.model.rawg.Games
import com.dirzaaulia.gamewish.data.model.rawg.Genre
import com.dirzaaulia.gamewish.data.model.wishlist.GameWishlist
import com.dirzaaulia.gamewish.data.model.wishlist.MovieWishlist
import com.dirzaaulia.gamewish.utils.visible
import com.dirzaaulia.gamewish.ui.common.item.CommonAnimeItem
import com.dirzaaulia.gamewish.ui.common.item.SearchGamesItem
import com.dirzaaulia.gamewish.ui.common.item.SearchGenreItem
import com.dirzaaulia.gamewish.ui.common.item.WishlistGameItem
import com.dirzaaulia.gamewish.ui.common.item.WishlistMovieItem
import com.dirzaaulia.gamewish.ui.common.placeholder.CommonItemPlaceholder
import com.dirzaaulia.gamewish.utils.PlaceholderConstant
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.SwipeRefreshState
import timber.log.Timber

@Composable
fun <T : Any> CommonVerticalSwipeList(
    data: LazyPagingItems<T>,
    state: SwipeRefreshState,
    lazyListState: LazyListState,
    emptyString: String,
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
        ) {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
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

    when {
        data.loadState.refresh is LoadState.Loading -> {
            CommonLoading(visibility = data.loadState.refresh is LoadState.Loading)
        }

        data.loadState.refresh is LoadState.Error -> {
            ErrorConnect(text = stringResource(id = R.string.deals_error)) {
                data.retry()
            }
        }

        data.itemCount == 0 && data.loadState.refresh is LoadState.NotLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emptyString, style = MaterialTheme.typography.subtitle1)
            }
        }
    }
}

@Composable
fun <T : Any> CommonVerticalList(
    data: LazyPagingItems<T>,
    lazyListState: LazyListState,
    placeholderType: Int = PlaceholderConstant.DEFAULT,
    emptyString: String,
    errorString: String,
    content: @Composable (T) -> Unit
) {
    if (data.itemCount != 0) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
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
                            ErrorConnect(text = errorString) {
                                retry()
                            }
                        }
                    }

                    loadState.append is LoadState.Error -> {
                        val error = data.loadState.append as LoadState.Error
                        item {
                            Timber.e("Append Error: ${error.error.localizedMessage}")
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

    when {
        data.loadState.refresh is LoadState.Loading -> {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxSize(),
            ) {
                items(25) {
                    when (placeholderType) {
                        PlaceholderConstant.GAME_WISHLIST -> {
                            WishlistGameItem(
                                gameWishlist = GameWishlist(),
                                loadState = data.loadState
                            )
                        }

                        PlaceholderConstant.MOVIE_WISHLIST -> {
                            WishlistMovieItem(
                                movieWishlist = MovieWishlist(),
                                loadState = data.loadState
                            )
                        }

                        PlaceholderConstant.DEALS -> {
                            CommonItemPlaceholder(
                                height = 150.dp,
                                shape = MaterialTheme.shapes.large
                            )
                        }

                        PlaceholderConstant.SEARCH_GAME_TAB -> {
                            SearchGenreItem(genre = Genre(), loadStates = data.loadState)
                        }

                        PlaceholderConstant.SEARCH_GAME -> {
                            SearchGamesItem(games = Games(), loadStates = data.loadState)
                        }

                        PlaceholderConstant.ANIME -> {
                            CommonAnimeItem(
                                parentNode = ParentNode(),
                                type = "",
                                loadState = data.loadState
                            )
                        }

                        else -> {
                            WishlistGameItem(
                                gameWishlist = GameWishlist(),
                                loadState = data.loadState
                            )
                        }
                    }
                }
            }
        }

        data.loadState.refresh is LoadState.Error -> {
            val error = data.loadState.refresh as LoadState.Error
            Timber.e("Refresh Error : ${error.error.localizedMessage}")
            ErrorConnect(text = errorString) {
                data.retry()
            }
        }

        data.itemCount == 0 && data.loadState.refresh is LoadState.NotLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emptyString,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.onBackground
                )
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
            style = MaterialTheme.typography.subtitle2,
            textAlign = TextAlign.Center
        )
        Button(onClick = { retry() }) {
            Text(text = "Try Again")
        }
    }
}
