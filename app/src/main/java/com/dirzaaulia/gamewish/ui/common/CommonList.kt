package com.dirzaaulia.gamewish.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.cheapshark.Deals
import com.dirzaaulia.gamewish.data.model.myanimelist.ParentNode
import com.dirzaaulia.gamewish.data.model.rawg.Games
import com.dirzaaulia.gamewish.data.model.rawg.Genre
import com.dirzaaulia.gamewish.data.model.rawg.Platform
import com.dirzaaulia.gamewish.data.model.rawg.Publisher
import com.dirzaaulia.gamewish.data.model.tmdb.Movie
import com.dirzaaulia.gamewish.data.model.wishlist.GameWishlist
import com.dirzaaulia.gamewish.data.model.wishlist.MovieWishlist
import com.dirzaaulia.gamewish.data.request.myanimelist.SearchGameRequest
import com.dirzaaulia.gamewish.extension.visible
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.ui.search.SearchViewModel
import com.dirzaaulia.gamewish.utils.*
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder
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
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize(),
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
                        PlaceholderConstant.GAME_WISHLIST ->  {
                            WishlistGameItem(gameWishlist = GameWishlist(), loadState = data.loadState)
                        }
                        PlaceholderConstant.MOVIE_WISHLIST -> {
                            WishlistMovieItem(movieWishlist = MovieWishlist(), loadState = data.loadState )
                        }
                        PlaceholderConstant.DEALS -> {
                            DealsItem(deals = Deals(), loadStates = data.loadState)
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
                            WishlistGameItem(gameWishlist = GameWishlist(), loadState = data.loadState)
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

    when {
        data.loadState.refresh is LoadState.Loading -> {
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
    modifier: Modifier = Modifier,
    gameWishlist: GameWishlist,
    loadState: CombinedLoadStates,
    navigateToGameDetails: (Long) -> Unit = { },
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = { gameWishlist.id?.let { navigateToGameDetails(it) } }
            ),
        elevation = 0.dp,
    ) {
        Column (
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            NetworkImage(
                url = gameWishlist.image.toString(),
                contentDescription = null,
                modifier = modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .placeholder(
                        visible = loadState.refresh is LoadState.Loading,
                        highlight = PlaceholderHighlight.shimmer(),
                        color = MaterialTheme.colors.secondary,
                        shape = MaterialTheme.shapes.small
                    ),
                contentScale = ContentScale.FillBounds
            )
            Text(
                text = gameWishlist.status.toString(),
                style = MaterialTheme.typography.body2,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, start = 8.dp)
                    .placeholder(
                        visible = loadState.refresh is LoadState.Loading,
                        highlight = PlaceholderHighlight.shimmer(),
                        color = MaterialTheme.colors.secondary,
                        shape = MaterialTheme.shapes.small
                    )
            )
            Text(
                text = gameWishlist.name.toString(),
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
                    .placeholder(
                        visible = loadState.refresh is LoadState.Loading,
                        highlight = PlaceholderHighlight.shimmer(),
                        color = MaterialTheme.colors.secondary,
                        shape = MaterialTheme.shapes.small
                    )

            )
        }
    }
}

@Composable
fun WishlistMovieItem(
    modifier: Modifier = Modifier,
    movieWishlist: MovieWishlist,
    loadState: CombinedLoadStates,
    navigateToMovieDetails: (Long, String) -> Unit = { _: Long, _: String -> },

    ) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(vertical = 4.dp)
            .clickable(
                onClick = {
                    movieWishlist.id?.let { id ->
                        movieWishlist.type?.let { type ->
                            navigateToMovieDetails(id, type)
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
            val url = if (movieWishlist.image.isNullOrBlank()) {
                OtherConstant.NO_IMAGE_URL
            } else {
                "${TmdbConstant.TMDB_BASE_IMAGE_URL}${movieWishlist.image}"
            }

            NetworkImage(
                url = url,
                contentDescription = null,
                modifier = modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .placeholder(
                        visible = loadState.refresh is LoadState.Loading,
                        highlight = PlaceholderHighlight.shimmer(),
                        color = MaterialTheme.colors.secondary,
                        shape = MaterialTheme.shapes.small
                    ),
                contentScale = ContentScale.FillBounds
            )
            Column(
                modifier = modifier
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    modifier = Modifier
                        .placeholder(
                            visible = loadState.refresh is LoadState.Loading,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = MaterialTheme.colors.secondary,
                            shape = MaterialTheme.shapes.small
                        ),
                    text = movieWishlist.status.toString(),
                    style = MaterialTheme.typography.caption
                )
                Text(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .placeholder(
                            visible = loadState.refresh is LoadState.Loading,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = MaterialTheme.colors.secondary,
                            shape = MaterialTheme.shapes.small
                        ),
                    text = movieWishlist.name.toString(),
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }
    }
}

@Composable
fun DealsItem(
    modifier: Modifier = Modifier,
    deals: Deals,
    loadStates: CombinedLoadStates
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
            val url = deals.thumb?.ifBlank {
                OtherConstant.NO_IMAGE_URL
            }

            NetworkImage(
                url = url.toString(),
                contentDescription = null,
                modifier = modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .placeholder(
                        visible = loadStates.refresh is LoadState.Loading,
                        highlight = PlaceholderHighlight.shimmer(),
                        color = MaterialTheme.colors.secondary,
                        shape = MaterialTheme.shapes.small
                    ),
                contentScale = ContentScale.FillBounds
            )
            Column(
                modifier = modifier
                    .padding(4.dp)
                    .weight(1f)
            ) {
                Surface(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .placeholder(
                            visible = loadStates.refresh is LoadState.Loading,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = MaterialTheme.colors.secondary,
                            shape = MaterialTheme.shapes.small
                        ),
                    shape = CircleShape,
                    color = MaterialTheme.colors.primary,
                ) {
                    Text(
                        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                        textAlign = TextAlign.Center,
                        text = String.format("%.2f%% Off", deals.savings?.toFloat()),
                        style = MaterialTheme.typography.caption
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .placeholder(
                            visible = loadStates.refresh is LoadState.Loading,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = MaterialTheme.colors.secondary,
                            shape = MaterialTheme.shapes.small
                        ),
                    text = deals.title.toString(),
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .placeholder(
                            visible = loadStates.refresh is LoadState.Loading,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = MaterialTheme.colors.secondary,
                            shape = MaterialTheme.shapes.small
                        ),
                    text = deals.normalPrice?.toDouble().toCurrencyFormat(),
                    style = MaterialTheme.typography.caption,
                    textDecoration = TextDecoration.LineThrough
                )
                Text(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .placeholder(
                            visible = loadStates.refresh is LoadState.Loading,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = MaterialTheme.colors.secondary,
                            shape = MaterialTheme.shapes.small
                        ),
                    text = deals.salePrice?.toDouble().toCurrencyFormat(),
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }
}

@Composable
fun CommonMovieItem(
    modifier: Modifier = Modifier,
    movie: Movie,
    navigateToDetails: (Long, String) -> Unit,
    type: String,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(vertical = 4.dp)
            .clickable(
                onClick = {
                    movie.id?.let {
                        if (type.equals("Movie", true)) {
                            navigateToDetails(it, "Movie")
                        } else {
                            navigateToDetails(it, "TV Show")
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
            movie.posterPath.let {
                val url = if (it.isNullOrBlank()) {
                    OtherConstant.NO_IMAGE_URL
                } else {
                    "${TmdbConstant.TMDB_BASE_IMAGE_URL}$it"
                }

                NetworkImage(
                    url = url,
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
                movie.releaseDate?.let {
                    val releaseDate = if (it.isBlank()) {
                        stringResource(R.string.no_release_date)
                    } else {
                        it.changeDateFormat("yyyy-MM-dd")
                    }

                    Text(
                        text = "Release Date : $releaseDate",
                        style = MaterialTheme.typography.caption
                    )
                }
                movie.title?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                movie.name?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.subtitle1
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
    type: String,
    loadState: CombinedLoadStates? = null,
    navigateToAnimeDetails: (Long, String) -> Unit = { _: Long, _: String -> },
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
            val url = parentNode.node?.mainPicture?.large?.ifBlank {
                OtherConstant.NO_IMAGE_URL
            }

            NetworkImage(
                url = url.toString(),
                contentDescription = null,
                modifier = modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .placeholder(
                        visible = loadState?.refresh is LoadState.Loading,
                        highlight = PlaceholderHighlight.shimmer(),
                        color = MaterialTheme.colors.secondary,
                        shape = MaterialTheme.shapes.small
                    ),
                contentScale = ContentScale.FillBounds
            )
            Column(
                modifier = modifier
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .weight(1f)
            ) {
                val isScorePlaceholderVisible = if (loadState?.refresh is LoadState.Loading) {
                    true
                } else loadState?.refresh is LoadState.NotLoading && parentNode.listStatus?.score != null

                AnimatedVisibility(
                    modifier = Modifier
                        .placeholder(
                            visible = loadState?.refresh is LoadState.Loading,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = MaterialTheme.colors.secondary,
                            shape = MaterialTheme.shapes.small
                        )
                        .align(Alignment.End),
                    visible = isScorePlaceholderVisible
                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null
                        )
                        Text(
                            text = parentNode.listStatus?.score.toString(),
                            style = MaterialTheme.typography.caption
                        )
                    }
                }

                var statusFormatted = parentNode.listStatus?.status
                    .toString()
                    .replace("_"," ")
                statusFormatted = statusFormatted.capitalizeWords()

                val isStatusPlaceholderVisible = if (loadState?.refresh is LoadState.Loading) {
                    true
                } else loadState?.refresh is LoadState.NotLoading && parentNode.listStatus?.status != null

                AnimatedVisibility(
                    modifier = Modifier
                        .placeholder(
                            visible = loadState?.refresh is LoadState.Loading,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = MaterialTheme.colors.secondary,
                            shape = MaterialTheme.shapes.small
                        ),
                    visible = isStatusPlaceholderVisible
                ) {
                    Text(
                        text = statusFormatted,
                        style = MaterialTheme.typography.body2
                    )
                }

                val isRelationTypePlaceholderVisible = if (loadState?.refresh is LoadState.Loading) {
                    true
                } else if (loadState?.refresh is LoadState.NotLoading && parentNode.relationType != null) {
                    true
                } else loadState == null && parentNode.relationType != null

                AnimatedVisibility(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .placeholder(
                            visible = loadState?.refresh is LoadState.Loading,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = MaterialTheme.colors.secondary,
                            shape = MaterialTheme.shapes.small
                        ),
                    visible = isRelationTypePlaceholderVisible
                ) {
                    Text(
                        text = parentNode.relationType.toString(),
                        style = MaterialTheme.typography.body2
                    )
                }

                Text(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .placeholder(
                            visible = loadState?.refresh is LoadState.Loading,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = MaterialTheme.colors.secondary,
                            shape = MaterialTheme.shapes.small
                        ),
                    text = parentNode.node?.title.toString(),
                    style = MaterialTheme.typography.subtitle1
                )

                if (type.equals("Anime", true)) {
                    if (!parentNode.listStatus?.status.equals("plan_to_watch", true)) {
                        parentNode.listStatus?.episodes?.let {
                            Text(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .placeholder(
                                        visible = loadState?.refresh is LoadState.Loading,
                                        highlight = PlaceholderHighlight.shimmer(),
                                        color = MaterialTheme.colors.secondary,
                                        shape = MaterialTheme.shapes.small
                                    ),
                                text = "$it Episodes Watched",
                                style = MaterialTheme.typography.caption
                            )
                        }
                    }
                } else {
                    if (!parentNode.listStatus?.status.equals("plan_to_read", true)) {
                        parentNode.listStatus?.chapters?.let {
                            Text(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .placeholder(
                                        visible = loadState?.refresh is LoadState.Loading,
                                        highlight = PlaceholderHighlight.shimmer(),
                                        color = MaterialTheme.colors.secondary,
                                        shape = MaterialTheme.shapes.small
                                    ),
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

@Composable
fun SearchGamesItem(
    modifier: Modifier = Modifier,
    navigateToGameDetails: (Long) -> Unit = { },
    games: Games,
    loadStates: CombinedLoadStates,
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
            Text(
                text = "Release Date : ${games.released?.changeDateFormat("yyyy-MM-dd")}",
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
                    .placeholder(
                        visible = loadStates.refresh is LoadState.Loading,
                        highlight = PlaceholderHighlight.shimmer(),
                        color = MaterialTheme.colors.secondary,
                        shape = MaterialTheme.shapes.small
                    )
            )
            Text(
                text = games.name.toString(),
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding(start = 8.dp, end = 4.dp, top = 4.dp)
                    .fillMaxWidth()
                    .placeholder(
                        visible = loadStates.refresh is LoadState.Loading,
                        highlight = PlaceholderHighlight.shimmer(),
                        color = MaterialTheme.colors.secondary,
                        shape = MaterialTheme.shapes.small
                    )
            )
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
    viewModel: SearchViewModel? = null,
    loadStates: CombinedLoadStates,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    genre.id?.let {
                        viewModel?.apply {
                            selectSearchGameTab(0)
                            setSearchGameRequest(
                                SearchGameRequest("", it, null, null)
                            )
                        }
                    }
                }
            ),
        elevation = 0.dp,
    ) {
        Column {
            val url = genre.imageBackground?.ifBlank {
                OtherConstant.NO_IMAGE_URL
            }

            NetworkImage(
                url = url.toString(),
                contentDescription = null,
                modifier = modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .placeholder(
                        visible = loadStates.refresh is LoadState.Loading,
                        highlight = PlaceholderHighlight.shimmer(),
                        color = MaterialTheme.colors.secondary,
                        shape = MaterialTheme.shapes.small
                    ),
                contentScale = ContentScale.FillBounds
            )
            Text(
                text = genre.name.toString(),
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
                    .placeholder(
                        visible = loadStates.refresh is LoadState.Loading,
                        highlight = PlaceholderHighlight.shimmer(),
                        color = MaterialTheme.colors.secondary,
                        shape = MaterialTheme.shapes.small
                    )
            )
        }
    }
}

@Composable
fun SearchPublisherItem(
    modifier: Modifier = Modifier,
    publisher: Publisher,
    viewModel: SearchViewModel,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    publisher.id?.let {
                        viewModel.apply {
                            selectSearchGameTab(0)
                            setSearchGameRequest(
                                SearchGameRequest("", null, it, null)
                            )
                        }
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
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    platform.id?.let {
                        viewModel.apply {
                            selectSearchGameTab(0)
                            setSearchGameRequest(
                                SearchGameRequest("", null, null, it)
                            )
                        }
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
