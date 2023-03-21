package com.dirzaaulia.gamewish.ui.search.tab.game

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
import com.dirzaaulia.gamewish.data.model.rawg.Games
import com.dirzaaulia.gamewish.data.model.rawg.SearchTab
import com.dirzaaulia.gamewish.data.model.rawg.SearchTabType
import com.dirzaaulia.gamewish.data.model.rawg.SearchTabType.Companion.setRawgDataSource
import com.dirzaaulia.gamewish.data.request.myanimelist.SearchGameRequest
import com.dirzaaulia.gamewish.theme.White
import com.dirzaaulia.gamewish.ui.common.*
import com.dirzaaulia.gamewish.ui.common.item.SearchGameTabItem
import com.dirzaaulia.gamewish.ui.common.item.SearchGamesItem
import com.dirzaaulia.gamewish.ui.search.SearchViewModel
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.PlaceholderConstant
import com.dirzaaulia.gamewish.utils.visible

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchGame(
    navigateToGameDetails: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel,
    upPress: () -> Unit,
    scaffoldState: ScaffoldState,
    lazyListStateSearchGames: LazyListState,
    lazyListStateGenre: LazyListState,
    lazyListStatePublisher: LazyListState,
    lazyListStatePlatform: LazyListState,
    searchGameList: LazyPagingItems<Games>,
    genre: LazyPagingItems<SearchTab>,
    publisher: LazyPagingItems<SearchTab>,
    platform: LazyPagingItems<SearchTab>,
    searchGameRequest: SearchGameRequest
) {

    val menu = SearchGameTab.values()
    val menuId: Int by viewModel.selectedSearchGameTab.collectAsState(initial = 0)

    Scaffold(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.primarySurface,
        scaffoldState = scaffoldState,
        content = {
            Scaffold(
                topBar = { SearchGameTabMenu(menu = menu, menuId = menuId, viewModel = viewModel) }
            ) {
                Crossfade(
                    targetState = SearchGameTab.getTabFromResource(menuId),
                    label = OtherConstant.EMPTY_STRING
                ) { destination ->
                    when (destination) {
                        SearchGameTab.LIST -> {
                            SearchGameList(
                                navigateToGameDetails = navigateToGameDetails,
                                data = searchGameList,
                                lazyListStateSearchGames = lazyListStateSearchGames,
                            )
                        }
                        SearchGameTab.GENRES -> {
                            SearchGameTabList(
                                data = genre,
                                type = SearchTabType.GENRE,
                                lazyListState = lazyListStateGenre,
                                viewModel = viewModel,
                            )
                        }
                        SearchGameTab.PUBLISHER -> {
                            SearchGameTabList(
                                data = publisher,
                                type = SearchTabType.PUBLISHER,
                                lazyListState = lazyListStatePublisher,
                                viewModel = viewModel,
                            )
                        }
                        SearchGameTab.PLATFORMS -> {
                            SearchGameTabList(
                                data = platform,
                                type = SearchTabType.PLATFORM,
                                lazyListState = lazyListStatePlatform,
                                viewModel = viewModel,
                            )
                        }
                    }
                }
            }
        },
        topBar =  {
            SearchGameAppBar(
                searchQuery = searchGameRequest.searchQuery,
                viewModel = viewModel,
                upPress = upPress
            )
        }
    )
}

@Composable
fun SearchGameList(
    navigateToGameDetails: (Long) -> Unit,
    data: LazyPagingItems<Games>,
    lazyListStateSearchGames: LazyListState
) {
    Column {
        Text(
            modifier = Modifier
                .padding(4.dp)
                .visible(data.loadState.refresh is LoadState.NotLoading),
            text = stringResource(id = R.string.games_data_source),
            style = MaterialTheme.typography.caption,
        )
        CommonVerticalList(
            data = data,
            lazyListState = lazyListStateSearchGames,
            placeholderType = PlaceholderConstant.SEARCH_GAME,
            emptyString = stringResource(id = R.string.search_games_empty),
            errorString = stringResource(id = R.string.search_games_error),
        ) { games ->
            SearchGamesItem(
                navigateToGameDetails = navigateToGameDetails,
                games = games,
            )
        }
    }
}

@Composable
fun SearchGameTabList(
    data: LazyPagingItems<SearchTab>,
    type: SearchTabType,
    lazyListState: LazyListState,
    viewModel: SearchViewModel,
) {
    Column {
        Text(
            modifier = Modifier
                .padding(4.dp)
                .visible(data.loadState.refresh is LoadState.NotLoading),
            text = type.setRawgDataSource(),
            style = MaterialTheme.typography.caption,
        )
        CommonVerticalList(
            data = data,
            lazyListState = lazyListState,
            placeholderType = PlaceholderConstant.SEARCH_GAME_TAB,
            emptyString = stringResource(id = R.string.search_genre_empty),
            errorString = stringResource(id = R.string.search_genre_error),
        ) { searchTab ->
            SearchGameTabItem(
                searchTab = searchTab,
                viewModel = viewModel,
            )
        }
    }
}

@Composable
fun SearchGameAppBar(
    searchQuery: String,
    viewModel: SearchViewModel,
    upPress: () -> Unit
) {
    var query by rememberSaveable { mutableStateOf(searchQuery) }
    val localFocusManager = LocalFocusManager.current

    TopAppBar(
        elevation = 0.dp,
        contentColor = White,
        modifier = Modifier
            .wrapContentHeight()
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
                },
                shape = RectangleShape,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search_game),
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
                        viewModel.apply {
                            selectSearchGameTab(0)
                            setSearchGameRequest(
                                SearchGameRequest(query, null, null, null)
                            )
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
    ScrollableTabRow(selectedTabIndex = menuId) {
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
    LIST(R.string.search),
    GENRES(R.string.genres),
    PUBLISHER(R.string.publishers),
    PLATFORMS(R.string.platforms);

    companion object {
        fun getTabFromResource(index: Int): SearchGameTab {
            return when (index) {
                0 -> LIST
                1 -> GENRES
                2 -> PUBLISHER
                3 -> PLATFORMS
                else -> LIST
            }
        }
    }
}