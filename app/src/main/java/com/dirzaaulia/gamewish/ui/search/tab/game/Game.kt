package com.dirzaaulia.gamewish.ui.search.tab.game

import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.paging.compose.LazyPagingItems
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.rawg.Games
import com.dirzaaulia.gamewish.data.model.rawg.Genre
import com.dirzaaulia.gamewish.data.model.rawg.Platform
import com.dirzaaulia.gamewish.data.model.rawg.Publisher
import com.dirzaaulia.gamewish.data.request.myanimelist.SearchGameRequest
import com.dirzaaulia.gamewish.ui.common.*
import com.dirzaaulia.gamewish.ui.search.SearchViewModel
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SearchGame(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel,
    upPress: () -> Unit,
    scope: CoroutineScope,
    scaffoldState: BackdropScaffoldState,
    lazyListStateSearchGames: LazyListState,
    lazyListStateGenre: LazyListState,
    lazyListStatePublisher: LazyListState,
    lazyListStatePlatform: LazyListState,
    searchGameList: LazyPagingItems<Games>,
    genre: LazyPagingItems<Genre>,
    publisher: LazyPagingItems<Publisher>,
    platform: LazyPagingItems<Platform>,
    searchGameRequest: SearchGameRequest
) {

    val menu = SearchGameTab.values()
    val menuId: Int by viewModel.selectedSearchGameTab.collectAsState(initial = 0)

    BackdropScaffold(
        modifier = modifier,
        backLayerBackgroundColor = MaterialTheme.colors.primarySurface,
        scaffoldState = scaffoldState,
        frontLayerShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        frontLayerContent = {
            SearchGameSheetContent(
                data = searchGameList,
                lazyListStateSearchGames = lazyListStateSearchGames,
                viewModel = viewModel,
            )
        },
        headerHeight = 0.dp,
        peekHeight = 80.dp,
        stickyFrontLayer = false,
        backLayerContent = {
            Scaffold(
                topBar = { SearchGameTabMenu(menu = menu, menuId = menuId, viewModel = viewModel) }
            ) {
                Crossfade(targetState = SearchGameTab.getTabFromResource(menuId)) { destination ->
                    when (destination) {
                        SearchGameTab.GENRE -> {
                            GenreList(
                                data = genre,
                                lazyListState = lazyListStateGenre,
                                viewModel = viewModel,
                                scope = scope,
                                scaffoldState = scaffoldState
                            )
                        }
                        SearchGameTab.PUBLISHER -> {
                            PublisherList(
                                data = publisher,
                                lazyListState = lazyListStatePublisher,
                                viewModel = viewModel,
                                scope = scope,
                                scaffoldState = scaffoldState
                            )
                        }
                        SearchGameTab.PLATFORMS -> {
                            PlatformList(
                                data = platform,
                                lazyListState = lazyListStatePlatform,
                                viewModel = viewModel,
                                scope = scope,
                                scaffoldState = scaffoldState
                            )
                        }
                    }
                }
            }
        },
        appBar =  {
            SearchGameAppBar(
                searchQuery = searchGameRequest.searchQuery,
                scope = scope,
                scaffoldState = scaffoldState,
                viewModel = viewModel,
                upPress = upPress
            )
        }
    )
}

@Composable
fun SearchGameSheetContent(
    data: LazyPagingItems<Games>,
    lazyListStateSearchGames: LazyListState,
    viewModel: SearchViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .width(64.dp)
                .height(4.dp),
            backgroundColor = MaterialTheme.colors.onSurface,
            shape = MaterialTheme.shapes.small,
            content = {}
        )
        Spacer(modifier = Modifier.height(4.dp))
        CommonVerticalList(
            data = data,
            lazyListState = lazyListStateSearchGames,
            emptyString = stringResource(id = R.string.search_games_empty),
            errorString = stringResource(id = R.string.search_games_error),
        ) { games ->
            SearchGamesItem(games = games, viewModel = viewModel)
        }
    }
}

@Composable
fun GenreList(
    data: LazyPagingItems<Genre>,
    lazyListState: LazyListState,
    viewModel: SearchViewModel,
    scope: CoroutineScope,
    scaffoldState: BackdropScaffoldState
) {
    CommonVerticalList(
        data = data,
        lazyListState = lazyListState,
        emptyString = stringResource(id = R.string.search_genre_empty),
        errorString = stringResource(id = R.string.search_genre_error),
    ) { genre ->
        SearchGenreItem(
            genre = genre,
            viewModel = viewModel,
            scope = scope,
            scaffoldState = scaffoldState
        )
    }
}

@Composable
fun PublisherList(
    data: LazyPagingItems<Publisher>,
    lazyListState: LazyListState,
    viewModel: SearchViewModel,
    scope: CoroutineScope,
    scaffoldState: BackdropScaffoldState
) {
    CommonVerticalList(
        data = data,
        lazyListState = lazyListState,
        emptyString = stringResource(id = R.string.search_publisher_empty),
        errorString = stringResource(id = R.string.search_publisher_error),
    ) { publisher ->
        SearchPublisherItem(
            publisher = publisher,
            viewModel = viewModel,
            scope = scope,
            scaffoldState = scaffoldState
        )
    }
}

@Composable
fun PlatformList(
    data: LazyPagingItems<Platform>,
    lazyListState: LazyListState,
    viewModel: SearchViewModel,
    scope: CoroutineScope,
    scaffoldState: BackdropScaffoldState
) {
    CommonVerticalList(
        data = data,
        lazyListState = lazyListState,
        emptyString = stringResource(id = R.string.search_platform_empty),
        errorString = stringResource(id = R.string.search_platform_error),
    ) { platform ->
        SearchPlatformItem(
            platform = platform,
            viewModel = viewModel,
            scope = scope,
            scaffoldState = scaffoldState
        )
    }
}

@Composable
fun SearchGameAppBar(
    searchQuery: String,
    scope: CoroutineScope,
    scaffoldState: BackdropScaffoldState,
    viewModel: SearchViewModel,
    upPress: () -> Unit
) {
    var query by rememberSaveable { mutableStateOf(searchQuery) }
    val localFocusManager = LocalFocusManager.current

    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier
            .height(80.dp)
            .statusBarsPadding()
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = { upPress() }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                )
            }
            TextField(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
                    .fillMaxHeight(),
                value = query,
                onValueChange = {
                    query = it
                },
                shape = RectangleShape,
                placeholder = {
                    Text(text = stringResource(id = R.string.search_game))
                },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.primarySurface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        localFocusManager.clearFocus()
                        viewModel.setSearchGameRequest(
                            SearchGameRequest(query, null, null, null)
                        )
                        scope.launch {
                            scaffoldState.conceal()
                        }
                    }
                )
            )
        }
    }
}

@Composable
fun SearchGameTabMenu(
    menu: Array<SearchGameTab>,
    menuId: Int,
    viewModel: SearchViewModel
) {
    TabRow(selectedTabIndex = menuId) {
        menu.forEachIndexed { index, searchGameTab ->
            Tab(
                selected = menuId == index,
                text = { Text(stringResource(id = searchGameTab.title)) },
                onClick = { viewModel.selectSearchGameTab(index) }
            )
        }
    }
}

enum class SearchGameTab (@StringRes val title: Int) {
    GENRE(R.string.genres),
    PUBLISHER(R.string.publishers),
    PLATFORMS(R.string.platforms);

    companion object {
        fun getTabFromResource(index: Int): SearchGameTab {
            return when (index) {
                0 -> GENRE
                1 -> PUBLISHER
                2 -> PLATFORMS
                else -> GENRE
            }
        }
    }
}
