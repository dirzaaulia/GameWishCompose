package com.dirzaaulia.gamewish.ui.details

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.myanimelist.Details
import com.dirzaaulia.gamewish.data.request.cheapshark.DealsRequest
import com.dirzaaulia.gamewish.extension.isError
import com.dirzaaulia.gamewish.extension.isSucceeded
import com.dirzaaulia.gamewish.extension.visible
import com.dirzaaulia.gamewish.ui.common.*
import com.dirzaaulia.gamewish.ui.theme.Grey700
import com.dirzaaulia.gamewish.ui.theme.Red700
import com.dirzaaulia.gamewish.ui.theme.White
import com.dirzaaulia.gamewish.utils.*
import com.google.accompanist.insets.navigationBarsWithImePadding
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
    navigateToAnimeDetails: (Long, String) -> Unit,
) {

    val menu  = AnimeDetailsTab.values()
    val menuId: Int by viewModel.selectedAnimeTab.collectAsState(0)
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val data by viewModel.animeDetails.collectAsState(null)
    val dataResult by viewModel.animeDetailsResult.collectAsState(null)
    val loading = viewModel.loading.value
    val updateMyAnimeListResult by viewModel.updateMyAnimeListResult.collectAsState()
    val deleteMyAnimeListResult by viewModel.deleteMyAnimeListResult.collectAsState()
    val errorMessage = if (type.equals("Anime", true)) {
        stringResource(id = R.string.anime_details_error)
    } else {
        stringResource(id = R.string.manga_details_error)
    }

    LaunchedEffect(viewModel) {
        if (type.equals("Anime", true)) {
            viewModel.getAnimeDetails(animeId)
        } else {
            viewModel.getMangaDetails(animeId)
        }
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
                        when {
                            updateMyAnimeListResult.isSucceeded -> {
                                scope.launch {
                                    val message = if (type.equals("Anime", true)) {
                                        if (data?.listStatus != null) {
                                            "This anime has been updated on your Anime list"
                                        } else {
                                            "This anime has been added on your Anime list"
                                        }
                                    } else {
                                        if (data?.listStatus != null) {
                                            "This manga has been updated on your Anime list"
                                        } else {
                                            "This manga has been added on your Anime list"
                                        }
                                    }
                                    scaffoldState.snackbarHostState.showSnackbar(message)
                                    viewModel.setUpdateMyAnimeListResult()
                                    data?.id?.let { id -> viewModel.getAnimeDetails(id) }
                                }
                            }
                            updateMyAnimeListResult.isError -> {
                                scope.launch {
                                    val message = if (type.equals("Anime", true)) {
                                        "Something went wrong when updating your Anime list. Please try again"
                                    } else {
                                        "Something went wrong when updating your Manga list. Please try again"
                                    }
                                    scaffoldState.snackbarHostState.showSnackbar(message)
                                    viewModel.setUpdateMyAnimeListResult()
                                }
                            }
                            deleteMyAnimeListResult.isSucceeded -> {
                                scope.launch {
                                    val message = if (type.equals("Anime", true)) {
                                        "This anime has been deleted from your Anime list"
                                    } else {
                                        "This manga has been deleted from your Manga list"
                                    }
                                    scaffoldState.snackbarHostState.showSnackbar(message)
                                    viewModel.setDeleteMyAnimeListResult()
                                    data?.id?.let { id -> viewModel.getAnimeDetails(id) }
                                }
                            }
                            deleteMyAnimeListResult.isError -> {
                                scope.launch {
                                    val message = if (type.equals("Anime", true)) {
                                        "Something went wrong when updating your Anime list. Please try again"
                                    } else {
                                        "Something went wrong when updating your Manga list. Please try again"
                                    }
                                    scaffoldState.snackbarHostState.showSnackbar(message)
                                    viewModel.setDeleteMyAnimeListResult()
                                }
                            }
                        }

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
                                    data?.let { value ->
                                        RelatedTab(
                                            data = value,
                                            type = type,
                                            navigateToAnimeDetails = navigateToAnimeDetails
                                        )
                                    }
                                }
                                AnimeDetailsTab.RECOMMENDATION -> {
                                    data?.let { value ->
                                        RecommendationTab(
                                            data = value,
                                            type = type,
                                            navigateToAnimeDetails = navigateToAnimeDetails
                                        )
                                    }
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
fun RecommendationTab(
    modifier: Modifier = Modifier,
    data: Details,
    type: String,
    navigateToAnimeDetails: (Long, String) -> Unit,
) {
    Column(modifier = modifier.padding(8.dp)) {
        data.title?.let {
            val text = if (type.equals("Anime", true)) {
                stringResource(R.string.anime_data_source)
            } else {
                stringResource(R.string.manga_data_source)
            }

            Text(
                text = text,
                style = MaterialTheme.typography.caption,
            )
        }
        if (type.equals("Anime", true)) {
            val recommendationList = data.recommendations

            if (recommendationList?.isEmpty() == true) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = stringResource(id = R.string.recommendation_anime_empty),
                        style = MaterialTheme.typography.h6
                    )
                }
            } else {
                LazyColumn {
                    items(recommendationList!!) { value ->
                        CommonAnimeItem(
                            parentNode = value,
                            navigateToAnimeDetails = navigateToAnimeDetails,
                            type = "Anime"
                        )
                    }
                }
            }
        } else {
            val recommendationList = data.recommendations

            if (recommendationList?.isEmpty() == true) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = stringResource(id = R.string.recommendation_manga_empty),
                        style = MaterialTheme.typography.h6
                    )
                }
            } else {
                LazyColumn {
                    items(recommendationList!!) { value ->
                        CommonAnimeItem(
                            parentNode = value,
                            navigateToAnimeDetails = navigateToAnimeDetails,
                            type = "Manga"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RelatedTab(
    modifier: Modifier = Modifier,
    data: Details,
    type: String,
    navigateToAnimeDetails: (Long, String) -> Unit,
) {
    Column(modifier = modifier.padding(8.dp)) {
        data.title?.let {
            val text = if (type.equals("Anime", true)) {
                stringResource(R.string.anime_data_source)
            } else {
                stringResource(R.string.manga_data_source)
            }

            if (type.equals("Anime", true)) {
                if (data.relatedAnime?.isNotEmpty() == true) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.caption,
                    )
                }
            } else {
                if (data.relatedManga?.isNotEmpty() == true) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.caption,
                    )
                }
            }
        }
        if (type.equals("Anime", true)) {
            val relatedList = data.relatedAnime

            if (relatedList?.isEmpty() == true) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = stringResource(id = R.string.related_anime_empty),
                        style = MaterialTheme.typography.h6
                    )
                }
            } else {
                LazyColumn {
                    items(relatedList!!) { value ->
                        CommonAnimeItem(
                            parentNode = value,
                            navigateToAnimeDetails = navigateToAnimeDetails,
                            type = "Anime"
                        )
                    }
                }
            }
        } else {
            val relatedList = data.relatedManga

            if (relatedList?.isEmpty() == true) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = stringResource(id = R.string.related_manga_empty),
                        style = MaterialTheme.typography.h6
                    )
                }
            } else {
                LazyColumn {
                    items(relatedList!!) { value ->
                        CommonAnimeItem(
                            parentNode = value,
                            navigateToAnimeDetails = navigateToAnimeDetails,
                            type = "Manga"
                        )
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

    var statusExpanded by remember { mutableStateOf(false) }
    var scoreExpanded by remember { mutableStateOf(false) }
    var scoreIndex by remember { mutableStateOf(0) }
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

    val score = if (data.listStatus?.score != null) {
        animeScoreFormat(data.listStatus?.score)
    } else {
        "(0) - Appaling"
    }

    val scoreList = listOf("(1) - Appaling",
        "(2) - Horrible",
        "(3) - Very Bad",
        "(4) - Bad",
        "(5) - Average",
        "(6) - Fine",
        "(7) - Good",
        "(8) - Very Good",
        "(9) - Great",
        "(10) - Masterpiece"
    )

    val numberWatched = if (type.equals("Anime", true)) {
        data.listStatus?.episodes ?: 0
    } else {
        data.listStatus?.chapters ?: 0
    }

    var statusText by remember { mutableStateOf(status) }
    var scoreText by remember { mutableStateOf(score) }
    var numberWatchedText by remember { mutableStateOf(numberWatched.toString()) }
    var textfieldSize by remember { mutableStateOf(Size.Zero) }
    val isRewatching = if (type.equals("Anime", true)) {
        data.listStatus?.isRewatching ?: false
    } else {
        data.listStatus?.isReReading ?: false
    }
    val isRewatchingState = remember { mutableStateOf(isRewatching) }
    val icon = when {
        statusExpanded -> {
            Icons.Filled.ArrowDropUp //it requires androidx.compose.material:material-icons-extended
        }
        scoreExpanded -> {
            Icons.Filled.ArrowDropUp //it requires androidx.compose.material:material-icons-extended
        }
        else -> {
            Icons.Filled.ArrowDropDown
        }
    }


    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .navigationBarsWithImePadding()
    ) {
        if (data.listStatus != null) {
            IconButton(
                onClick = {
                    data.id?.let {
                        if (type.equals("Anime", true)) {
                            viewModel.deleteMyAnimeListAnimeList(it)
                        } else {
                            viewModel.deleteMyAnimeListMangaList(it)
                        }

                    }
                    scope.launch {
                        scaffoldState.bottomSheetState.collapse()
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
                    modifier = Modifier.clickable { statusExpanded = !statusExpanded }
                )
            }
        )
        DropdownMenu(
            expanded = statusExpanded,
            onDismissRequest = { statusExpanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
        ) {
            statusList.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        statusText = item
                        statusExpanded = false
                    }
                ) {
                    Text(text = item)
                }
            }
        }
        val fieldEnabled = !(statusText.equals("Plan To Watch", true)
                || statusText.equals("Plan To Read", true))

        OutlinedTextField(
            enabled = fieldEnabled,
            readOnly = true,
            value = scoreText,
            onValueChange = { scoreText = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                },
            label = { Text("Score") },
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.clickable { scoreExpanded = !scoreExpanded }
                )
            }
        )
        DropdownMenu(
            expanded = scoreExpanded,
            onDismissRequest = { scoreExpanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
        ) {
            scoreList.forEachIndexed { index, item ->
                DropdownMenuItem(
                    onClick = {
                        scoreText = item
                        scoreExpanded = false
                        scoreIndex = index
                    }
                ) {
                    Text(text = item)
                }
            }
        }
        OutlinedTextField(
            enabled = fieldEnabled,
            value = numberWatchedText,
            onValueChange = { numberWatchedText = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                if (type.equals("Anime", true)) {
                    Text("Episodes Watched")
                } else {
                    Text("Chapters Read")
                }
            },
            placeholder = {
                Text(numberWatchedText)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 4.dp),
        ) {
            Text(
                text = if (type.equals("Anime", true)) {
                    "Is Rewatching"
                } else {
                    "Is Rereading"
                },
                style = MaterialTheme.typography.subtitle1
            )
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End
            ) {
                Switch(
                    checked = isRewatchingState.value,
                    onCheckedChange = {
                        isRewatchingState.value = it

                    },
                )
            }
        }
        OutlinedButton(
            modifier = Modifier
                .padding(top = 4.dp, bottom = 16.dp)
                .fillMaxWidth(),
            onClick = {
                data.id?.let {
                    if (type.equals("Anime", true)) {
                        viewModel.updateMyAnimeListAnimeList(
                            it,
                            statusText.lowerCaseWords().replace(" ","_"),
                            isRewatchingState.value,
                            scoreIndex + 1,
                            numberWatchedText.toInt()
                        )
                    } else {
                        viewModel.updateMyAnimeListMangaList(
                            it,
                            statusText.lowerCaseWords().replace(" ","_"),
                            isRewatchingState.value,
                            scoreIndex + 1,
                            numberWatchedText.toInt()
                        )
                    }
                }

                scope.launch { scaffoldState.bottomSheetState.collapse() }
            }
        ) {
            val text = if (data.listStatus != null) {
                if (type.equals("Anime", true)) {
                    "Update Anime In List"
                } else {
                    "Update Manga In List"
                }
            } else {
                if (type.equals("Anime", true)) {
                    "Add Anime To List"
                } else {
                    "Add Manga To List"
                }
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
    ScrollableTabRow(selectedTabIndex = menuId) {
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