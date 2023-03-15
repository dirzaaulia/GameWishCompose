package com.dirzaaulia.gamewish.ui.search.tab.movie

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.tmdb.Movie
import com.dirzaaulia.gamewish.theme.White
import com.dirzaaulia.gamewish.ui.common.CommonVerticalList
import com.dirzaaulia.gamewish.ui.common.item.CommonMovieItem
import com.dirzaaulia.gamewish.ui.search.SearchViewModel
import com.dirzaaulia.gamewish.utils.PlaceholderConstant

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Movie(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel,
    lazyListStateMovie: LazyListState,
    lazyListStateTv: LazyListState,
    searchMovieQuery: String,
    searchMovieList: LazyPagingItems<Movie>,
    searchTvList: LazyPagingItems<Movie>,
    navigateToMovieDetails: (Long, String) -> Unit,
    upPress: () -> Unit,
) {
    val menu = SearchMovieTab.values()
    val menuId: Int by viewModel.selectedSearchMovieTab.collectAsState(initial = 0)

    Scaffold(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.primarySurface,
        topBar = {
            SearchMovieAppBar(
                searchQuery = searchMovieQuery,
                viewModel = viewModel,
                upPress = upPress
            )
        },
    ) {
        Scaffold(
            topBar = {
                SearchMovieTabMenu(menu = menu, menuId = menuId, viewModel = viewModel)
            }
        ) {
            Crossfade(
                targetState = SearchMovieTab.getTabFromResource(menuId),
                label = ""
            ) { destination ->
                when (destination) {
                    SearchMovieTab.MOVIE -> {
                        SearchMovieList(
                            data = searchMovieList,
                            lazyListState = lazyListStateMovie,
                            searchMovieQuery = searchMovieQuery,
                            navigateToMovieDetails = navigateToMovieDetails
                        )
                    }
                    SearchMovieTab.TVSHOW -> {
                        SearchTvList(
                            data = searchTvList,
                            lazyListState = lazyListStateTv,
                            searchMovieQuery = searchMovieQuery,
                            navigateToTvDetails = navigateToMovieDetails
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchTvList(
    data: LazyPagingItems<Movie>,
    lazyListState: LazyListState,
    searchMovieQuery: String,
    navigateToTvDetails: (Long, String) -> Unit
) {
    Column (
        modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp)
    ){

        val emptyString = if (searchMovieQuery.isBlank()) {
            stringResource(id = R.string.search_tv_empty_query)
        } else {
            stringResource(id = R.string.search_tv_empty)
        }

        if (data.itemCount != 0 && data.loadState.refresh is LoadState.NotLoading) {
            Text(
                text = stringResource(id = R.string.tv_show_data_source),
                style = MaterialTheme.typography.caption,
            )
        }
        CommonVerticalList(
            data = data,
            lazyListState = lazyListState,
            placeholderType = PlaceholderConstant.MOVIE_WISHLIST,
            emptyString = emptyString,
            errorString = stringResource(id = R.string.search_tv_error),
        ) { movie ->
            CommonMovieItem(
                movie = movie,
                type = "TV Show",
                navigateToDetails = navigateToTvDetails
            )
        }
    }
}


@Composable
fun SearchMovieList(
    data: LazyPagingItems<Movie>,
    lazyListState: LazyListState,
    searchMovieQuery: String,
    navigateToMovieDetails: (Long, String) -> Unit
) {
    Column (
        modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp)
    ){

        val emptyString = if (searchMovieQuery.isBlank()) {
            stringResource(id = R.string.search_movie_empty_query)
        } else {
            stringResource(id = R.string.search_movie_empty)
        }

        if (data.itemCount != 0 && data.loadState.refresh is LoadState.NotLoading) {
            Text(
                text = stringResource(id = R.string.movie_data_source),
                style = MaterialTheme.typography.caption,
            )
        }
        CommonVerticalList(
            data = data,
            lazyListState = lazyListState,
            placeholderType = PlaceholderConstant.MOVIE_WISHLIST,
            emptyString = emptyString,
            errorString = stringResource(id = R.string.search_movie_error),
        ) { movie ->
            CommonMovieItem(
                movie = movie,
                type = "Movie",
                navigateToDetails = navigateToMovieDetails
            )
        }
    }
}

@Composable
fun SearchMovieAppBar(
    searchQuery: String,
    viewModel: SearchViewModel,
    upPress: () -> Unit
) {
    var query by rememberSaveable { mutableStateOf(searchQuery) }
    val localFocusManager = LocalFocusManager.current

    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier
            .wrapContentHeight()
            .statusBarsPadding()
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxSize()
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = { upPress() }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                    tint = White
                )
            }
            TextField(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                value = query,
                onValueChange = {
                    query = it
                    viewModel.setSearchMovieQuery(query)
                },
                shape = RectangleShape,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search_movie),
                        color = White
                    )
                },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.primarySurface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = White,
                    textColor = White,
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        localFocusManager.clearFocus()
                        viewModel.setSearchMovieQuery(query)
                    }
                )
            )
        }
    }
}

@Composable
fun SearchMovieTabMenu(
    menu: Array<SearchMovieTab>,
    menuId: Int,
    viewModel: SearchViewModel
) {
    TabRow(selectedTabIndex = menuId) {
        menu.forEachIndexed { index, searchMovieTab ->
            Tab(
                selected = menuId == index,
                text = { Text(stringResource(id = searchMovieTab.title)) },
                onClick = { viewModel.selectSearchMovieTab(index) }
            )
        }
    }
}

enum class SearchMovieTab (@StringRes val title: Int) {
    MOVIE(R.string.movie),
    TVSHOW(R.string.tv_show);

    companion object {
        fun getTabFromResource(index: Int): SearchMovieTab {
            return when (index) {
                0 -> MOVIE
                1 -> TVSHOW
                else -> MOVIE
            }
        }
    }
}