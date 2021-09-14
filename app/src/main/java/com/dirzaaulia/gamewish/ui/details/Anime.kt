package com.dirzaaulia.gamewish.ui.details

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.Wishlist
import com.dirzaaulia.gamewish.data.model.myanimelist.Details
import com.dirzaaulia.gamewish.data.model.rawg.GameDetails
import com.dirzaaulia.gamewish.extension.isError
import com.dirzaaulia.gamewish.extension.isSucceeded
import com.dirzaaulia.gamewish.extension.visible
import com.dirzaaulia.gamewish.ui.common.CommonAnimeCarousel
import com.dirzaaulia.gamewish.ui.common.CommonLoading
import com.dirzaaulia.gamewish.ui.common.ErrorConnect
import com.dirzaaulia.gamewish.ui.search.SearchViewModel
import com.dirzaaulia.gamewish.ui.search.tab.game.SearchGameTab
import com.dirzaaulia.gamewish.ui.theme.Grey700
import com.dirzaaulia.gamewish.ui.theme.Red700
import com.dirzaaulia.gamewish.ui.theme.White
import com.dirzaaulia.gamewish.utils.*
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AnimeDetails(
    viewModel: DetailsViewModel = hiltViewModel(),
    upPress: () -> Unit,
    type: String,
    animeId: Long,
) {

    val menu  = AnimeDetailsTab.values()
    val menuId: Int by viewModel.selectedAnimeTab.collectAsState(0)
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val data by viewModel.animeDetails.collectAsState(null)
    val dataResult by viewModel.animeDetailsResult.collectAsState(null)
    val loading = viewModel.loading.value
    val errorMessage = if (type.equals("Anime", true)) {
        stringResource(id = R.string.anime_details_error)
    } else {
        stringResource(id = R.string.manga_details_error)
    }

    LaunchedEffect(viewModel) {
        viewModel.getAnimeDetails(animeId)
    }

    CommonLoading(visibility = loading)
    AnimatedVisibility(visible = !loading) {
        when {
            dataResult.isError -> {
                viewModel.setLoading(false)
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    ErrorConnect(text = errorMessage) {
                        viewModel.getAnimeDetails(animeId)
                    }
                }
            }
            dataResult.isSucceeded -> {
                Scaffold(
                    topBar = {
                        data?.title?.let {
                            AnimeDetailsTopBar(
                                title = it,
                                upPress = upPress
                            )
                        }
                    }
                ) {
                    BottomSheetScaffold(
                        scaffoldState = scaffoldState,
                        topBar = {
                            AnimeDetailsTabMenu(
                                menu = menu,
                                menuId = menuId,
                                viewModel = viewModel
                            )
                        },
                        sheetContent = {
                            data?.let { value ->
                                AnimeDetailsSheetContent(
                                    type = type,
                                    data = value,
                                    viewModel = viewModel,
                                    scope = scope,
                                    scaffoldState = scaffoldState
                                )
                            }
                        },
                        sheetPeekHeight = 0.dp,
                    ) { innerPadding ->
                        val innerModifier = Modifier.padding(innerPadding)
                        Crossfade(
                            targetState = AnimeDetailsTab.getTabFromResource(menuId)
                        ) { destination ->
                            when (destination) {
                                AnimeDetailsTab.DESCRIPTION -> {
                                    data?.let { value ->
                                        DescriptionTab(
                                            modifier = innerModifier,
                                            type = type,
                                            loading = loading,
                                            data = value,
                                            scope = scope,
                                            scaffoldState = scaffoldState
                                        )
                                    }
                                }
                                AnimeDetailsTab.RELATED -> {

                                }
                                AnimeDetailsTab.RECOMMENDATION -> {

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DescriptionTab(
    modifier: Modifier = Modifier,
    type: String,
    loading: Boolean,
    data: Details,
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState
) {
    LazyColumn(modifier = modifier.padding(8.dp)) {
        item {
            DescriptionHeader(
                type = type,
                loading = loading,
                data = data
            )
        }
        item {
            DescriptionFooter(
                data = data,
                scope = scope,
                scaffoldState = scaffoldState
            )
        }
    }
}

@Composable
fun DescriptionHeader(
    type: String,
    loading: Boolean,
    data: Details
) {
    Row(modifier = Modifier.height(300.dp)) {
        Card(
            modifier = Modifier
                .fillMaxHeight()
                .padding(end = 4.dp)
                .weight(1f),
            shape = MaterialTheme.shapes.small
        ) {
            if (!data.pictures.isNullOrEmpty()) {
                val pagerState = rememberPagerState(
                    pageCount = data.pictures.size,
                    initialOffscreenLimit = 2,
                )

                CommonAnimeCarousel(
                    pagerState = pagerState,
                    screenshots = data.pictures
                )
            } else if (data.pictures?.isEmpty() == true) {
                NetworkImage(
                    modifier = Modifier.visible(!loading),
                    url = OtherConstant.NO_IMAGE_URL,
                    contentDescription = null
                )
            }
        }
        Card(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 8.dp)
                .weight(1f),
            shape = MaterialTheme.shapes.small
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Card(
                    backgroundColor = Grey700,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = type,
                        color = White,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(4.dp),
                        textAlign = TextAlign.Center
                    )
                }
                Card(
                    backgroundColor = Grey700,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                ) {
                    data.status?.let { status ->
                        Text(
                            text = status.replace("_"," ").capitalizeWords(),
                            color = White,
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier.padding(4.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Text(
                    text = animeSourceFormat(data.mediaType),
                    style = MaterialTheme.typography.subtitle2,
                )

                val episodes = if (type.equals("anime", true)) {
                    "${data.episodes} Episodes"
                } else {
                    "${data.chapters} Chapters"
                }

                Text(
                    text = episodes,
                    color = Color.Gray,
                    style = MaterialTheme.typography.caption,
                )
                Text(
                    text = "Score",
                    style = MaterialTheme.typography.subtitle2,
                )
                Text(
                    text = "${data.mean}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.caption,
                )
                Text(
                    text = "Rank",
                    style = MaterialTheme.typography.subtitle2,
                )
                Text(
                    text = "#${data.rank}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.caption,
                )
                Text(
                    text = "Popularity",
                    style = MaterialTheme.typography.subtitle2,
                )
                Text(
                    text = "#${data.popularity}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.caption,
                )
                Text(
                    text = "Members",
                    style = MaterialTheme.typography.subtitle2,
                )
                Text(
                    text = numberFormatter(data.members?.toDouble()),
                    color = Color.Gray,
                    style = MaterialTheme.typography.caption,
                )
            }
        }
    }
}

@Composable
fun DescriptionFooter(
    data: Details,
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState
) {
    Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
        data.title?.let {
            Text(
                text = stringResource(id = R.string.anime_data_source),
                style = MaterialTheme.typography.caption,
            )
        }
        Row(
          verticalAlignment = Alignment.CenterVertically,
        ) {
            data.title?.let {
                Text(
                    modifier = Modifier.weight(1f),
                    text = it,
                    style = MaterialTheme.typography.h4,
                )
            }
            OutlinedButton(
                modifier = Modifier.size(50.dp),
                onClick = {
                    scope.launch {
                        if (scaffoldState.bottomSheetState.isCollapsed) {
                            scaffoldState.bottomSheetState.expand()
                        } else {
                            scaffoldState.bottomSheetState.collapse()
                        }
                    }
                },
                shape = CircleShape,
                border = BorderStroke(1.dp, MaterialTheme.colors.onSurface),
                contentPadding = PaddingValues(0.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSurface
                )
            }
        }
        data.alternativeTitles?.ja?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.h5,
            )
        }
        data.startDate?.let {
            Text(
                text = animeDateFormat(data.startDate, data.endDate),
                style = MaterialTheme.typography.body2,
            )
        }
        Row {
            data.source?.let {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Source : ${it.replace("_", " ").capitalizeWords()}",
                    style = MaterialTheme.typography.body2,
                )
            }
            data.rating?.let {
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    text = animeRatingFormat(it),
                    style = MaterialTheme.typography.body2,
                )
            }
        }
        data.genres?.let {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                textAlign = TextAlign.Center,
                text = animeGenreFormat(it),
                style = MaterialTheme.typography.body2,
            )
        }
        data.synopsis?.let {
            Text(
                text = "Synopsis",
                style = MaterialTheme.typography.h6,
            )
            Text(
                textAlign = TextAlign.Justify,
                text = htmlToTextFormatter(it).toString(),
                style = MaterialTheme.typography.body1,
            )
        }
        data.background?.let{
            if (it.isNotBlank()) {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = "Background",
                    style = MaterialTheme.typography.h6,
                )
                Text(
                    textAlign = TextAlign.Justify,
                    text = htmlToTextFormatter(it).toString(),
                    style = MaterialTheme.typography.body1,
                )
            }
        }
    }
}

@Composable
fun AnimeDetailsTopBar(
    title: String,
    upPress: () -> Unit
) {
    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier
            .height(80.dp)
            .statusBarsPadding()
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
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
            Text(
                maxLines = 2,
                softWrap = true,
                modifier = Modifier.align(Alignment.CenterVertically),
                text = title,
                style = MaterialTheme.typography.h6
            )
        }
    }
}

@Composable
fun AnimeDetailsSheetContent(
    type: String,
    data: Details,
    viewModel: DetailsViewModel,
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState
) {

    var expanded by remember { mutableStateOf(false) }
    val status = if (type.equals("Anime", true)) {
        data.listStatus?.status?.replace("_", " ")?.capitalizeWords()
            ?: "Plan To Watch"
    } else {
        data.listStatus?.status?.replace("_", " ")?.capitalizeWords()
            ?: "Plan To Read"
    }

    val statusList = if (type.equals("Anime", true)) {
        listOf("Watching", "Completed", "On-Hold", "Dropped", "Plan To Watch")
    } else {
        listOf("Reading", "Completed", "On-Hold", "Dropped", "Plan To Read")
    }

    var statusText by remember { mutableStateOf(status) }
    var textfieldSize by remember { mutableStateOf(Size.Zero) }
    val icon = if (expanded)
        Icons.Filled.ArrowDropUp //it requires androidx.compose.material:material-icons-extended
    else
        Icons.Filled.ArrowDropDown

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        if (data.listStatus != null) {
            IconButton(
                onClick = {
                    data.listStatus.let {

                    }

                    scope.launch {
                        scaffoldState.bottomSheetState.collapse()
                        scaffoldState.snackbarHostState
                            .showSnackbar("This game has been deleted from your Wishlist.")
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null,
                    tint = Red700
                )
            }
        }
        data.title?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.h6
            )
        }
        OutlinedTextField(
            readOnly = true,
            value = statusText,
            onValueChange = { statusText = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                },
            label = { Text("Status") },
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
        ) {
            statusList.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        statusText = item
                        expanded = false
                    }
                ) {
                    Text(text = item)
                }
            }
        }
        OutlinedButton(
            modifier = Modifier
                .padding(top = 4.dp, bottom = 16.dp)
                .fillMaxWidth(),
            onClick = {
//                val newData = data.let {
//
//                }
//                viewModel.addToWishlist(data)
//                gameDetails.id?.let { viewModel.checkIfGameWishlisted(it) }

                scope.launch {
                    scaffoldState.bottomSheetState.collapse()

                    val text = if (data.listStatus != null) {
                        "This game has been updated on your Wishlist."
                    } else {
                        "This game has been added to your Wishlist."
                    }

                    scaffoldState.snackbarHostState.showSnackbar(text)
                }
            }
        ) {
            val text = if (data.listStatus != null) {
                "Update Wishlist"
            } else {
                "Add To Wishlist"
            }

            Text(text = text)
        }
    }
}

@Composable
fun AnimeDetailsTabMenu(
    menu: Array<AnimeDetailsTab>,
    menuId: Int,
    viewModel: DetailsViewModel
) {
    TabRow(selectedTabIndex = menuId) {
        menu.forEachIndexed { index, searchGameTab ->
            Tab(
                selected = menuId == index,
                text = { Text(stringResource(id = searchGameTab.title)) },
                onClick = { viewModel.selectAnimeDetailsTab(index) }
            )
        }
    }
}

enum class AnimeDetailsTab(@StringRes val title: Int) {
    DESCRIPTION(R.string.description),
    RELATED(R.string.related),
    RECOMMENDATION(R.string.recommendation);

    companion object {
        fun getTabFromResource(index: Int): AnimeDetailsTab {
            return when (index) {
                0 -> DESCRIPTION
                1 -> RELATED
                2 -> RECOMMENDATION
                else -> DESCRIPTION
            }
        }
    }
}