package com.dirzaaulia.gamewish.ui.search.tab.anime

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.utils.ResponseResult
import com.dirzaaulia.gamewish.data.model.myanimelist.ParentNode
import com.dirzaaulia.gamewish.data.request.myanimelist.SearchGameRequest
import com.dirzaaulia.gamewish.utils.isError
import com.dirzaaulia.gamewish.utils.isSucceeded
import com.dirzaaulia.gamewish.theme.White
import com.dirzaaulia.gamewish.ui.common.CommonVerticalList
import com.dirzaaulia.gamewish.ui.common.MyAnimeListWebViewClient
import com.dirzaaulia.gamewish.ui.common.item.CommonAnimeItem
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.ui.search.SearchViewModel
import com.dirzaaulia.gamewish.utils.MyAnimeListConstant
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.PlaceholderConstant
import com.dirzaaulia.gamewish.utils.capitalizeWords
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchAnime(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel,
    homeViewModel: HomeViewModel,
    scope: CoroutineScope,
    accessTokenResult: ResponseResult<String>?,
    lazyListStateAnime: LazyListState,
    lazyListStateManga: LazyListState,
    lazyListStateSeasonalAnime: LazyListState,
    searchAnimeQuery: String,
    seasonalAnimeQuery: String,
    searchAnimeList: LazyPagingItems<ParentNode>,
    searchMangaList: LazyPagingItems<ParentNode>,
    seasonalAnimeList: LazyPagingItems<ParentNode>,
    navigateToAnimeDetails: (Long, String) -> Unit,
    upPress: () -> Unit,
) {
    val menu = SearchAnimeTab.values()
    val menuId: Int by viewModel.selectedSearchAnimeTab.collectAsState(initial = 0)

    val scaffoldState = rememberBottomSheetScaffoldState()

    when {
        accessTokenResult.isSucceeded -> {
            BottomSheetScaffold(
                scaffoldState = scaffoldState,
                modifier = modifier,
                backgroundColor = MaterialTheme.colors.primarySurface,
                topBar = {
                    SearchAnimeAppBar(
                        menuId = menuId,
                        searchQuery = searchAnimeQuery,
                        viewModel = viewModel,
                        upPress = upPress
                    )
                },
                sheetContent = {
                    SeasonalAnimeSheet(
                        seasonalAnimeQuery = seasonalAnimeQuery,
                        viewModel = viewModel
                    )
                },
                sheetPeekHeight = 0.dp,
                sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
            ) {
                Scaffold(
                    topBar = {
                        SearchAnimeTabMenu(menu = menu, menuId = menuId, viewModel = viewModel)
                    }
                ) {
                    Crossfade(
                        targetState = SearchAnimeTab.getTabFromResource(menuId),
                        label = ""
                    ) { destination ->
                        when (destination) {
                            SearchAnimeTab.SEASONAL -> {
                                SeasonalAnime(
                                    seasonalAnimeQuery = seasonalAnimeQuery,
                                    data = seasonalAnimeList,
                                    lazyListState = lazyListStateSeasonalAnime,
                                    scope = scope,
                                    scaffoldState = scaffoldState,
                                    navigateToAnimeDetails = navigateToAnimeDetails
                                )
                            }

                            SearchAnimeTab.ANIME -> {
                                SearchAnimeList(
                                    data = searchAnimeList,
                                    lazyListState = lazyListStateAnime,
                                    navigateToAnimeDetails = navigateToAnimeDetails,
                                    searchAnimeQuery = searchAnimeQuery
                                )
                            }

                            SearchAnimeTab.MANGA -> {
                                SearchMangaList(
                                    data = searchMangaList,
                                    lazyListState = lazyListStateManga,
                                    navigateToAnimeDetails = navigateToAnimeDetails,
                                    searchAnimeQuery = searchAnimeQuery
                                )
                            }
                        }
                    }
                }

            }
        }

        accessTokenResult.isError -> {
            MyAnimeListWebViewClient(
                from = MyAnimeListConstant.MYANIMELIST_WEBVIEW_OTHER,
                viewModel = homeViewModel
            )
        }
    }
}

@Composable
fun SearchMangaList(
    data: LazyPagingItems<ParentNode>,
    lazyListState: LazyListState,
    navigateToAnimeDetails: (Long, String) -> Unit,
    searchAnimeQuery: String
) {
    Column(
        modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp)
    ) {
        val emptyString = if (searchAnimeQuery.isBlank()) {
            stringResource(id = R.string.search_manga_empty_query)
        } else {
            stringResource(id = R.string.search_manga_empty)
        }

        if (data.itemCount != OtherConstant.ZERO
            && data.loadState.refresh is LoadState.NotLoading
        ) Text(
            text = stringResource(id = R.string.manga_data_source),
            style = MaterialTheme.typography.caption,
        )
        CommonVerticalList(
            data = data,
            lazyListState = lazyListState,
            placeholderType = PlaceholderConstant.ANIME,
            emptyString = emptyString,
            errorString = stringResource(id = R.string.search_manga_error),
        ) { parentNode ->
            CommonAnimeItem(
                parentNode = parentNode,
                navigateToAnimeDetails = navigateToAnimeDetails,
                type = MyAnimeListConstant.MYANIMELIST_TYPE_MANGA,
            )
        }
    }
}

@Composable
fun SearchAnimeList(
    data: LazyPagingItems<ParentNode>,
    lazyListState: LazyListState,
    navigateToAnimeDetails: (Long, String) -> Unit,
    searchAnimeQuery: String
) {
    Column(
        modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp)
    ) {
        val emptyString = if (searchAnimeQuery.isBlank()) {
            stringResource(id = R.string.search_anime_empty_query)
        } else {
            stringResource(id = R.string.search_anime_empty)
        }

        if (data.itemCount != 0 && data.loadState.refresh is LoadState.NotLoading) {
            Text(
                text = stringResource(id = R.string.anime_data_source),
                style = MaterialTheme.typography.caption,
            )
        }
        CommonVerticalList(
            data = data,
            lazyListState = lazyListState,
            placeholderType = PlaceholderConstant.ANIME,
            emptyString = emptyString,
            errorString = stringResource(id = R.string.search_anime_error),
        ) { parentNode ->
            CommonAnimeItem(
                parentNode = parentNode,
                navigateToAnimeDetails = navigateToAnimeDetails,
                type = "Anime",
            )
        }
    }
}

@Composable
fun SeasonalAnime(
    seasonalAnimeQuery: String,
    data: LazyPagingItems<ParentNode>,
    lazyListState: LazyListState,
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState,
    navigateToAnimeDetails: (Long, String) -> Unit,
) {

    val seasonal = seasonalAnimeQuery.split(" ")
    val season = seasonal[0]
    val year = seasonal[1]

    Column(
        modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp)
    ) {
        Row {
            Text(
                text = "${season.capitalizeWords()} $year",
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .placeholder(
                        visible = data.loadState.refresh is LoadState.Loading,
                        highlight = PlaceholderHighlight.shimmer(),
                        color = MaterialTheme.colors.secondary,
                        shape = MaterialTheme.shapes.small
                    )
            )
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = {
                    scope.launch {
                        if (scaffoldState.bottomSheetState.isCollapsed) {
                            scaffoldState.bottomSheetState.expand()
                        } else {
                            scaffoldState.bottomSheetState.collapse()
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null
                )
            }
        }
        Text(
            modifier = Modifier
                .placeholder(
                    visible = data.loadState.refresh is LoadState.Loading,
                    highlight = PlaceholderHighlight.shimmer(),
                    color = MaterialTheme.colors.secondary,
                    shape = MaterialTheme.shapes.small
                ),
            text = stringResource(id = R.string.seasonal_data_source),
            style = MaterialTheme.typography.caption,
        )
        CommonVerticalList(
            data = data,
            lazyListState = lazyListState,
            placeholderType = PlaceholderConstant.ANIME,
            emptyString = stringResource(id = R.string.search_seasonal_empty),
            errorString = stringResource(id = R.string.search_seasonal_error),
        ) { parentNode ->
            CommonAnimeItem(
                parentNode = parentNode,
                navigateToAnimeDetails = navigateToAnimeDetails,
                type = "Anime",
            )
        }
    }
}

@Composable
fun SeasonalAnimeSheet(
    seasonalAnimeQuery: String,
    viewModel: SearchViewModel
) {
    val season = seasonalAnimeQuery.split(" ")

    var seasonExpanded by remember { mutableStateOf(false) }
    val statusList = listOf("Winter", "Spring", "Summer", "Fall")
    var seasonText by rememberSaveable { mutableStateOf(season[0].capitalizeWords()) }
    var textfieldSize by remember { mutableStateOf(Size.Zero) }
    val seasonIcon = if (seasonExpanded)
        Icons.Filled.ArrowDropUp //it requires androidx.compose.material:material-icons-extended
    else
        Icons.Filled.ArrowDropDown

    var year by rememberSaveable { mutableStateOf(season[1]) }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .navigationBarsPadding()
            .imePadding()
    ) {
        OutlinedTextField(
            readOnly = true,
            value = seasonText,
            onValueChange = { seasonText = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                },
            label = { Text("Season") },
            trailingIcon = {
                Icon(
                    imageVector = seasonIcon,
                    contentDescription = null,
                    modifier = Modifier.clickable { seasonExpanded = !seasonExpanded }
                )
            }
        )
        DropdownMenu(
            expanded = seasonExpanded,
            onDismissRequest = { seasonExpanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
        ) {
            statusList.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        seasonText = item
                        seasonExpanded = false

                        viewModel.setSearchSeasonalQuery("${seasonText.lowercase()} $year")
                    }
                ) {
                    Text(text = item)
                }
            }
        }
        OutlinedTextField(
            value = year,
            onValueChange = {
                year = it
                viewModel.setSearchSeasonalQuery("${seasonText.lowercase()} $year")
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Year") },
        )
    }
}

@Composable
fun SearchAnimeAppBar(
    menuId: Int,
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
                    .align(Alignment.CenterVertically)
                    .weight(1f)
                    .fillMaxHeight(),
                value = query,
                onValueChange = {
                    query = it

                    if (menuId == 0) {
                        viewModel.selectSearchAnimeTab(1)
                    }
                    viewModel.setSearchAnimeQuery(query)
                },
                shape = RectangleShape,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search_anime),
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
                        viewModel.setSearchGameRequest(
                            SearchGameRequest(query, null, null, null)
                        )
                    }
                )
            )
        }
    }
}

@Composable
fun SearchAnimeTabMenu(
    menu: Array<SearchAnimeTab>,
    menuId: Int,
    viewModel: SearchViewModel
) {
    ScrollableTabRow(selectedTabIndex = menuId) {
        menu.forEachIndexed { index, searchAnimeTab ->
            Tab(
                selected = menuId == index,
                text = { Text(stringResource(id = searchAnimeTab.title)) },
                onClick = { viewModel.selectSearchAnimeTab(index) }
            )
        }
    }
}

enum class SearchAnimeTab(@StringRes val title: Int) {
    SEASONAL(R.string.seasonal_anime),
    ANIME(R.string.anime),
    MANGA(R.string.manga);

    companion object {
        fun getTabFromResource(index: Int): SearchAnimeTab {
            return when (index) {
                0 -> SEASONAL
                1 -> ANIME
                2 -> MANGA
                else -> SEASONAL
            }
        }
    }
}