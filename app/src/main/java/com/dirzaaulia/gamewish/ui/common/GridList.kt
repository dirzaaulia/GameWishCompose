package com.dirzaaulia.gamewish.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.cheapshark.Deals
import com.dirzaaulia.gamewish.extension.visible
import com.dirzaaulia.gamewish.utils.NetworkImage
import com.dirzaaulia.gamewish.utils.currencyFormatter
import com.dirzaaulia.gamewish.utils.openDeals
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.SwipeRefreshState
import timber.log.Timber
import java.util.*


@Composable
fun <T: Any> GridSwipeRefreshList(
    modifier: Modifier = Modifier,
    data: LazyPagingItems<T>,
    state: SwipeRefreshState,
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
            LazyVerticalGrid(
                cells = GridCells.Fixed(2),
                modifier = Modifier
                    .visible(data.loadState.refresh !is LoadState.Loading)
            ) {
                items(data.itemCount) { index ->
                    data[index]?.let { item -> content.invoke(item) }
                }
                data.apply {
                    when {
                        loadState.append is LoadState.Loading -> {
                            item { CommonLoadingGridItem() }
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
fun <T: Any> GridList(
    data: LazyPagingItems<T>,
    lazyListState: LazyListState,
    content: @Composable (T) -> Unit
) {
    if (data.itemCount != 0) {
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
                        item { CommonLoadingGridItem() }
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
            Text(text = "There no Deals found!", style = MaterialTheme.typography.subtitle1)
        }
    }

}

@Composable
fun DealsItem(
    deals: Deals,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(vertical = 4.dp)
            .clickable(
                onClick = { deals.dealID?.let { openDeals(context = context, it) } }
            ),
        shape = MaterialTheme.shapes.large,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            deals.thumb?.let {
                NetworkImage(
                    url = it,
                    contentDescription = null,
                    modifier = modifier
                        .width(100.dp)
                        .fillMaxHeight(),
                    contentScale = ContentScale.FillBounds
                )
            }
            Column(
                modifier = modifier
                    .padding(4.dp)
                    .weight(1f)
            ) {
                deals.savings?.let {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colors.primary,
                        modifier = modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            modifier = modifier.padding(2.dp),
                            textAlign = TextAlign.Center,
                            text = String.format("%.2f%% Off", it.toFloat()),
                            style = MaterialTheme.typography.caption
                        )
                    }
                }
                deals.title?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                deals.normalPrice?.let {
                    Text(
                        text = currencyFormatter(it.toDouble(), Locale.US),
                        style = MaterialTheme.typography.caption,
                        textDecoration = TextDecoration.LineThrough
                    )
                }
                deals.salePrice?.let {
                    Text(
                        text = currencyFormatter(it.toDouble(), Locale.US),
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }
    }
}