package com.dirzaaulia.gamewish.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import com.dirzaaulia.gamewish.data.model.Wishlist
import com.dirzaaulia.gamewish.data.model.cheapshark.Deals
import com.dirzaaulia.gamewish.data.model.myanimelist.ParentNode
import com.dirzaaulia.gamewish.extension.visible
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.utils.NetworkImage
import com.dirzaaulia.gamewish.utils.capitalizeWords
import com.dirzaaulia.gamewish.utils.currencyFormatter
import com.dirzaaulia.gamewish.utils.openDeals
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.SwipeRefreshState
import timber.log.Timber
import java.util.*

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
    emptyString: String,
    errorString: String,
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
//                    loadState.append is LoadState.Error -> {
//                        val error = data.loadState.refresh as LoadState.Error
//                        item {
//                            Timber.e("Append Error: ${error.error.localizedMessage}")
//                        }
//                    }
                }
            }
        }
    }

    when {
        data.loadState.refresh is LoadState.Loading -> {
            CommonLoading(visibility = data.loadState.refresh is LoadState.Loading)
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
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emptyString, style = MaterialTheme.typography.subtitle1)
            }
        }
    }
}

@Composable
fun <T : Any> AnimeVerticalList(
    data: LazyPagingItems<T>,
    lazyListState: LazyListState,
    emptyString: String,
    errorString: String,
    viewModel: HomeViewModel,
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

    when {
        data.loadState.refresh is LoadState.Loading -> {
            CommonLoading(visibility = data.loadState.refresh is LoadState.Loading)
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
fun GameListItem(
    wishlist: Wishlist,
    modifier: Modifier = Modifier,
    navigateToGameDetails: (Long) -> Unit = { }
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = { wishlist.id?.let { navigateToGameDetails(it) } }
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

@Composable
fun AnimeItem(
    parentNode: ParentNode,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(vertical = 4.dp)
            .clickable(
                onClick = { /* TODO */ }
            ),
        shape = MaterialTheme.shapes.large,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            parentNode.node?.mainPicture?.large?.let {
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
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .weight(1f)
            ) {
                parentNode.listStatus?.status?.let {
                    var statusFormatted = it.replace("_"," ")
                    statusFormatted = statusFormatted.capitalizeWords()

                    Text(
                        text = statusFormatted,
                        style = MaterialTheme.typography.body2
                    )
                }
                parentNode.node?.title?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                parentNode.listStatus?.status?.let { status ->
                    if (!status.equals("plan_to_watch", true)) {
                        parentNode.listStatus?.episodes?.let {
                            Text(
                                text = "$it Episodes Watched",
                                style = MaterialTheme.typography.caption
                            )
                        }
                    } else if (!status.equals("plan_to_read", true)) {
                        parentNode.listStatus?.chapters?.let {
                            Text(
                                text = "$it Chapters Watched",
                                style = MaterialTheme.typography.caption
                            )
                        }
                    }
                }
            }
        }
    }
}