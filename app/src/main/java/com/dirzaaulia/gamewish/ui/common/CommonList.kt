package com.dirzaaulia.gamewish.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
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
import com.dirzaaulia.gamewish.data.model.rawg.Genre
import com.dirzaaulia.gamewish.data.model.myanimelist.ParentNode
import com.dirzaaulia.gamewish.data.model.rawg.Games
import com.dirzaaulia.gamewish.data.model.rawg.Platform
import com.dirzaaulia.gamewish.data.model.rawg.Publisher
import com.dirzaaulia.gamewish.data.request.myanimelist.SearchGameRequest
import com.dirzaaulia.gamewish.extension.visible
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.ui.search.SearchViewModel
import com.dirzaaulia.gamewish.ui.theme.White
import com.dirzaaulia.gamewish.utils.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.SwipeRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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
    emptyString: String,
    errorString: String,
    content: @Composable (T) -> Unit
) {
    if (data.itemCount != 0) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
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
fun WishlistGameItem(
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
fun CommonAnimeItem(
    modifier: Modifier = Modifier,
    parentNode: ParentNode,
    navigateToAnimeDetails: (Long, String) -> Unit,
    type: String,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(vertical = 4.dp)
            .clickable(
                onClick = {
                    parentNode.node?.id?.let {
                        if (type.equals("Anime", true)) {
                            navigateToAnimeDetails(it, "Anime")
                        } else {
                            navigateToAnimeDetails(it, "Manga")
                        }
                    }
                }
            ),
        shape = MaterialTheme.shapes.large,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                parentNode.listStatus?.score?.let {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null
                        )
                        Text(
                            text = it.toString(),
                            style = MaterialTheme.typography.caption
                        )
                    }
                }
                parentNode.listStatus?.status?.let {
                    var statusFormatted = it.replace("_"," ")
                    statusFormatted = statusFormatted.capitalizeWords()

                    Text(
                        text = statusFormatted,
                        style = MaterialTheme.typography.body2
                    )
                }
                parentNode.relationType?.let {
                    Text(
                        text = it,
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
                    if (type.equals("Anime", true)) {
                        if (!status.equals("plan_to_watch", true)) {
                            parentNode.listStatus?.episodes?.let {
                                Text(
                                    text = "$it Episodes Watched",
                                    style = MaterialTheme.typography.caption
                                )
                            }
                        }
                    } else {
                        if (!status.equals("plan_to_read", true)) {
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
}

@Composable
fun SearchGamesItem(
    modifier: Modifier = Modifier,
    navigateToGameDetails: (Long) -> Unit = { },
    games: Games,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = { games.id?.let { navigateToGameDetails(it) } }
            ),
        elevation = 0.dp,
    ) {
        Column(modifier = modifier.padding(top = 4.dp)) {
            games.released?.let { released ->
                Text(
                    text = textDateFormatter2(released),
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                )
            }
            games.name?.let { name ->
                Text(
                    text = name,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                )
            }
            games.platforms?.let { platforms ->
                LazyRow(modifier = Modifier.padding(start = 8.dp)
                ) {
                    items(platforms) { data ->
                        Surface(
                            shape = CircleShape,
                            color = setPlatformsBackgroundColor(data, 0),
                            modifier = modifier.padding(top = 4.dp, end = 4.dp)
                        ) {
                            Text(
                                modifier = modifier.padding(4.dp),
                                textAlign = TextAlign.Center,
                                text = String.format("${data.platform?.name}"),
                                style = MaterialTheme.typography.caption,
                                color = White
                            )
                        }
                    }
                }
            }
            Divider(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun SearchGenreItem(
    modifier: Modifier = Modifier,
    genre: Genre,
    viewModel: SearchViewModel,
    scope: CoroutineScope,
    scaffoldState: BackdropScaffoldState
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    genre.id?.let {
                        viewModel.setSearchGameRequest(
                            SearchGameRequest("", it, null, null)
                        )
                    }
                    scope.launch {
                        scaffoldState.conceal()
                    }
                }
            ),
        elevation = 0.dp,
    ) {
        Column {
            genre.imageBackground?.let { imageUrl ->
                NetworkImage(
                    url = imageUrl,
                    contentDescription = null,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
            genre.name?.let { name ->
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
fun SearchPublisherItem(
    modifier: Modifier = Modifier,
    publisher: Publisher,
    viewModel: SearchViewModel,
    scope: CoroutineScope,
    scaffoldState: BackdropScaffoldState
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    publisher.id?.let {
                        viewModel.setSearchGameRequest(
                            SearchGameRequest("", null, it, null)
                        )
                    }
                    scope.launch {
                        scaffoldState.conceal()
                    }
                }
            ),
        elevation = 0.dp,
    ) {
        Column {
            publisher.imageBackground?.let { imageUrl ->
                NetworkImage(
                    url = imageUrl,
                    contentDescription = null,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
            publisher.name?.let { name ->
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
fun SearchPlatformItem(
    modifier: Modifier = Modifier,
    platform: Platform,
    viewModel: SearchViewModel,
    scope: CoroutineScope,
    scaffoldState: BackdropScaffoldState
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    platform.id?.let {
                        viewModel.setSearchGameRequest(
                            SearchGameRequest("", null, null, it)
                        )
                    }
                    scope.launch {
                        scaffoldState.conceal()
                    }
                }
            ),
        elevation = 0.dp,
    ) {
        Column {
            platform.imageBackground?.let { imageUrl ->
                NetworkImage(
                    url = imageUrl,
                    contentDescription = null,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
            platform.name?.let { name ->
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
