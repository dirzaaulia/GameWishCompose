package com.dirzaaulia.gamewish.ui.details.anime

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.dirzaaulia.gamewish.theme.Grey700
import com.dirzaaulia.gamewish.theme.Red700
import com.dirzaaulia.gamewish.theme.White
import com.dirzaaulia.gamewish.ui.common.CommonAnimeCarousel
import com.dirzaaulia.gamewish.ui.common.CommonLoading
import com.dirzaaulia.gamewish.ui.common.ErrorConnect
import com.dirzaaulia.gamewish.ui.common.item.CommonMyAnimeListItem
import com.dirzaaulia.gamewish.ui.details.DetailsViewModel
import com.dirzaaulia.gamewish.utils.*
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AnimeDetails(
    viewModel: DetailsViewModel = hiltViewModel(),
    upPress: () -> Unit,
    type: String,
    animeId: Long,
    navigateToAnimeDetails: (Long, String) -> Unit,
) {

    val menu = AnimeDetailsTab.values()
    val menuId: Int by viewModel.selectedAnimeTab.collectAsState(0)
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val data by viewModel.animeDetails.collectAsState(null)
    val dataResult by viewModel.animeDetailsResult.collectAsState(null)
    val updateMyAnimeListResult by viewModel.updateMyAnimeListResult.collectAsState()
    val deleteMyAnimeListResult by viewModel.deleteMyAnimeListResult.collectAsState()
    val errorMessage = type.setStringBasedOnMyAnimeListType(
        setIfAnime = stringResource(id = R.string.anime_details_error),
        setIfManga = stringResource(id = R.string.manga_details_error)
    )

    LaunchedEffect(viewModel) {
        type.doBasedOnMyAnimeListType(
            doIfAnime = { viewModel.getAnimeDetails(animeId) },
            doIfManga = { viewModel.getMangaDetails(animeId) }

        )
    }

    when {
        dataResult.isLoading -> CommonLoading(visibility = true)
        dataResult.isSucceeded -> {
            Scaffold(
                modifier = Modifier.navigationBarsPadding(),
                backgroundColor = MaterialTheme.colors.primarySurface,
                topBar = {
                    data?.title?.let { title: String ->
                        AnimeDetailsTopBar(
                            title = title,
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
                            LaunchedEffect(updateMyAnimeListResult.isSucceeded) {
                                scope.launch {
                                    val message = type.setStringBasedOnMyAnimeListType(
                                        setIfAnime = if (data?.listStatus != null) {
                                            MyAnimeListConstant.MYANIMELIST_ANIME_LIST_UPDATE
                                        } else {
                                            MyAnimeListConstant.MYANIMELIST_ANIME_LIST_ADD
                                        },
                                        setIfManga = if (data?.listStatus != null) {
                                            MyAnimeListConstant.MYANIMELIST_MANGA_LIST_UPDATE
                                        } else {
                                            MyAnimeListConstant.MYANIMELIST_MANGA_LIST_ADD
                                        }
                                    )
                                    scaffoldState.snackbarHostState.showSnackbar(message)
                                    viewModel.setUpdateMyAnimeListResult()
                                    data?.id?.let { id ->
                                        type.doBasedOnMyAnimeListType(
                                            doIfAnime = { viewModel.getAnimeDetails(id) },
                                            doIfManga = { viewModel.getMangaDetails(id) }

                                        )
                                    }
                                }
                            }
                        }

                        updateMyAnimeListResult.isError -> {
                            LaunchedEffect(updateMyAnimeListResult.isError) {
                                scope.launch {
                                    val message = type.setStringBasedOnMyAnimeListType(
                                        setIfAnime = MyAnimeListConstant.MYANIMELIST_ANIME_LIST_UPDATE_FAILED,
                                        setIfManga = MyAnimeListConstant.MYANIMELIST_MANGA_LIST_UPDATE_FAILED
                                    )
                                    scaffoldState.snackbarHostState.showSnackbar(message)
                                    viewModel.setUpdateMyAnimeListResult()
                                }
                            }
                        }

                        deleteMyAnimeListResult.isSucceeded -> {
                            LaunchedEffect(deleteMyAnimeListResult.isSucceeded) {
                                scope.launch {
                                    val message = type.setStringBasedOnMyAnimeListType(
                                        setIfAnime = MyAnimeListConstant.MYANIMELIST_ANIME_LIST_DELETE,
                                        setIfManga = MyAnimeListConstant.MYANIMELIST_MANGA_LIST_DELETE
                                    )
                                    scaffoldState.snackbarHostState.showSnackbar(message)
                                    viewModel.setDeleteMyAnimeListResult()
                                    data?.id?.let { id ->
                                        type.doBasedOnMyAnimeListType(
                                            doIfAnime = { viewModel.getAnimeDetails(id) },
                                            doIfManga = { viewModel.getMangaDetails(id) }
                                        )
                                    }
                                }
                            }
                        }

                        deleteMyAnimeListResult.isError -> {
                            LaunchedEffect(deleteMyAnimeListResult.isError) {
                                scope.launch {
                                    val message =
                                        type.setStringBasedOnMyAnimeListType(
                                            setIfAnime = MyAnimeListConstant.MYANIMELIST_ANIME_LIST_UPDATE_FAILED,
                                            setIfManga = MyAnimeListConstant.MYANIMELIST_MANGA_LIST_UPDATE_FAILED
                                        )
                                    scaffoldState.snackbarHostState.showSnackbar(message)
                                    viewModel.setDeleteMyAnimeListResult()
                                }
                            }
                        }
                    }

                    val innerModifier = Modifier.padding(innerPadding)
                    Crossfade(
                        targetState = AnimeDetailsTab.getTabFromResource(menuId),
                        label = OtherConstant.EMPTY_STRING
                    ) { destination ->
                        when (destination) {
                            AnimeDetailsTab.DESCRIPTION -> {
                                data?.let { value ->
                                    DescriptionTab(
                                        modifier = innerModifier,
                                        type = type,
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
        dataResult.isError -> {
            val errorScaffoldState = rememberScaffoldState()

            viewModel.setLoading(false)

            Scaffold(
                modifier = Modifier.navigationBarsPadding(),
                scaffoldState = errorScaffoldState
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    ErrorConnect(text = errorMessage) {
                        viewModel.getAnimeDetails(animeId)
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
            val text = type.setStringBasedOnMyAnimeListType(
                setIfAnime = stringResource(R.string.anime_data_source),
                setIfManga = stringResource(R.string.manga_data_source)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.caption,
            )
        }
        if (type.equals(MyAnimeListConstant.MYANIMELIST_TYPE_ANIME, true)) {
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
                        CommonMyAnimeListItem(
                            parentNode = value,
                            navigateToAnimeDetails = navigateToAnimeDetails,
                            type = MyAnimeListConstant.MYANIMELIST_TYPE_ANIME,
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
                        CommonMyAnimeListItem(
                            parentNode = value,
                            navigateToAnimeDetails = navigateToAnimeDetails,
                            type = MyAnimeListConstant.MYANIMELIST_TYPE_MANGA
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
            val text = type.setStringBasedOnMyAnimeListType(
                setIfAnime = stringResource(R.string.anime_data_source),
                setIfManga = stringResource(R.string.manga_data_source)
            )
            if (type.equals(MyAnimeListConstant.MYANIMELIST_TYPE_ANIME, true)) {
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
        if (type.equals(MyAnimeListConstant.MYANIMELIST_TYPE_ANIME, true)) {
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
                        CommonMyAnimeListItem(
                            parentNode = value,
                            navigateToAnimeDetails = navigateToAnimeDetails,
                            type = MyAnimeListConstant.MYANIMELIST_TYPE_ANIME
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
                        CommonMyAnimeListItem(
                            parentNode = value,
                            navigateToAnimeDetails = navigateToAnimeDetails,
                            type = MyAnimeListConstant.MYANIMELIST_TYPE_MANGA
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
    data: Details,
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState
) {
    LazyColumn(modifier = modifier.padding(8.dp)) {
        item {
            AnimeDescriptionHeader(
                type = type,
                data = data
            )
        }
        item {
            AnimeDescriptionFooter(
                data = data,
                scope = scope,
                scaffoldState = scaffoldState
            )
        }
    }
}

@Composable
fun AnimeDescriptionHeader(
    type: String,
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
                val pagerState = rememberPagerState()

                CommonAnimeCarousel(
                    pagerState = pagerState,
                    screenshots = data.pictures
                )
            } else if (data.pictures?.isEmpty() == true) {
                NetworkImage(
                    url = OtherConstant.NO_IMAGE_URL,
                    contentDescription = OtherConstant.EMPTY_STRING,
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
                            text = status.myAnimeListStatusFormatted(
                                OtherConstant.EMPTY_STRING
                            ),
                            color = White,
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier.padding(4.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Text(
                    text = data.mediaType.animeSourceFormat(),
                    style = MaterialTheme.typography.subtitle2,
                )

                val episodes = type.setStringBasedOnMyAnimeListType(
                    setIfAnime = String.format(
                        OtherConstant.STRING_FORMAT_S_SPACE_S,
                        data.episodes,
                        MyAnimeListConstant.MYANIMELIST_EPISODES
                    ),
                    setIfManga = String.format(
                        OtherConstant.STRING_FORMAT_S_SPACE_S,
                        data.chapters,
                        MyAnimeListConstant.MYANIMELIST_CHAPTERS
                    )
                )
                Text(
                    text = episodes,
                    color = Color.Gray,
                    style = MaterialTheme.typography.caption,
                )
                Text(
                    text = MyAnimeListConstant.MYANIMELIST_SCORE,
                    style = MaterialTheme.typography.subtitle2,
                )
                Text(
                    text = data.mean.toString(),
                    color = Color.Gray,
                    style = MaterialTheme.typography.caption,
                )
                Text(
                    text = MyAnimeListConstant.MYANIMELIST_RANK,
                    style = MaterialTheme.typography.subtitle2,
                )
                Text(
                    text = String.format(
                        OtherConstant.STRING_FORMAT_S_SPACE_S,
                        OtherConstant.HASHTAG,
                        data.rank.toString()
                    ),
                    color = Color.Gray,
                    style = MaterialTheme.typography.caption,
                )
                Text(
                    text = MyAnimeListConstant.MYANIMELIST_POPULARITY,
                    style = MaterialTheme.typography.subtitle2,
                )
                Text(
                    text = String.format(
                        OtherConstant.STRING_FORMAT_S_SPACE_S,
                        OtherConstant.HASHTAG,
                        data.popularity.toString()
                    ),
                    color = Color.Gray,
                    style = MaterialTheme.typography.caption,
                )
                Text(
                    text = MyAnimeListConstant.MYANIMELIST_MEMBERS,
                    style = MaterialTheme.typography.subtitle2,
                )
                Text(
                    text = data.members?.toDouble().toNumberFormat(),
                    color = Color.Gray,
                    style = MaterialTheme.typography.caption,
                )
            }
        }
    }
}

@Composable
fun AnimeDescriptionFooter(
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
            data.title?.let { title ->
                Text(
                    modifier = Modifier.weight(1f),
                    text = title,
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
                    contentDescription = OtherConstant.EMPTY_STRING,
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
                text = animeDateFormat(data.startDate, data.endDate.replaceIfNull()),
                style = MaterialTheme.typography.body2,
            )
        }
        Row {
            data.source?.let { source ->
                Text(
                    modifier = Modifier.weight(1f),
                    text = source.toMyAnimeListSource(),
                    style = MaterialTheme.typography.body2,
                )
            }
            data.rating?.let {
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    text = it.animeRatingFormat(),
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
                text = it.toAnimeGenre(),
                style = MaterialTheme.typography.body2,
            )
        }
        data.synopsis?.let {
            Text(
                text = MyAnimeListConstant.MYANIMELIST_SYNOPSIS,
                style = MaterialTheme.typography.h6,
            )
            Text(
                textAlign = TextAlign.Justify,
                text = it.fromHtml(),
                style = MaterialTheme.typography.body1,
            )
        }
        data.background?.let { background ->
            if (background.isNotBlank()) {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = MyAnimeListConstant.MYANIMELIST_BACKGROUND,
                    style = MaterialTheme.typography.h6,
                )
                Text(
                    textAlign = TextAlign.Justify,
                    text = background.fromHtml(),
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
        contentColor = White,
        modifier = Modifier
            .wrapContentHeight()
            .statusBarsPadding()
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = { upPress() }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = OtherConstant.EMPTY_STRING,
                    tint = White
                )
            }
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = title,
                softWrap = true,
                style = MaterialTheme.typography.h6,
                color = White
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
    val status = type.setStringBasedOnMyAnimeListType(
        setIfAnime = data.listStatus?.status.myAnimeListStatusFormatted(
            MyAnimeListConstant.MYANIMELIST_STATUS_PLAN_TO_WATCH
        ),
        setIfManga = data.listStatus?.status.myAnimeListStatusFormatted(
            MyAnimeListConstant.MYANIMELIST_STATUS_PLAN_TO_READ
        )
    )

    val statusList = type.setListBasedOnMyAnimeListType(
        setIfAnime = listOf(
            MyAnimeListConstant.MYANIMELIST_STATUS_WATCHING,
            MyAnimeListConstant.MYANIMELIST_STATUS_COMPLETED,
            MyAnimeListConstant.MYANIMELIST_STATUS_ON_HOLD,
            MyAnimeListConstant.MYANIMELIST_STATUS_DROPPED,
            MyAnimeListConstant.MYANIMELIST_STATUS_PLAN_TO_WATCH
        ),
        setIfManga = listOf(
            MyAnimeListConstant.MYANIMELIST_STATUS_READING,
            MyAnimeListConstant.MYANIMELIST_STATUS_COMPLETED,
            MyAnimeListConstant.MYANIMELIST_STATUS_ON_HOLD,
            MyAnimeListConstant.MYANIMELIST_STATUS_DROPPED,
            MyAnimeListConstant.MYANIMELIST_STATUS_PLAN_TO_READ
        )
    )

    val score = if (data.listStatus?.score != null) {
        data.listStatus?.score.toAnimeScoreFormat()
    } else {
        MyAnimeListConstant.MYANIMELIST_SCORE_1
    }

    val scoreList = listOf(
        MyAnimeListConstant.MYANIMELIST_SCORE_1,
        MyAnimeListConstant.MYANIMELIST_SCORE_2,
        MyAnimeListConstant.MYANIMELIST_SCORE_3,
        MyAnimeListConstant.MYANIMELIST_SCORE_4,
        MyAnimeListConstant.MYANIMELIST_SCORE_5,
        MyAnimeListConstant.MYANIMELIST_SCORE_6,
        MyAnimeListConstant.MYANIMELIST_SCORE_7,
        MyAnimeListConstant.MYANIMELIST_SCORE_8,
        MyAnimeListConstant.MYANIMELIST_SCORE_9,
        MyAnimeListConstant.MYANIMELIST_SCORE_10
    )

    val numberWatched = if (type.equals(MyAnimeListConstant.MYANIMELIST_TYPE_ANIME, true)) {
        data.listStatus?.episodes.replaceIfNull()
    } else {
        data.listStatus?.chapters.replaceIfNull()
    }

    var statusText by remember { mutableStateOf(status) }
    var scoreText by remember { mutableStateOf(score) }
    var numberWatchedText by remember { mutableStateOf(numberWatched.toString()) }
    var textfieldSize by remember { mutableStateOf(Size.Zero) }
    val isRewatching = if (type.equals(MyAnimeListConstant.MYANIMELIST_TYPE_ANIME, true)) {
        data.listStatus?.isRewatching ?: false
    } else {
        data.listStatus?.isReReading ?: false
    }
    val isRewatchingState = remember { mutableStateOf(isRewatching) }
    val icon = when {
        statusExpanded -> {
            Icons.Filled.ArrowDropUp
        }

        scoreExpanded -> {
            Icons.Filled.ArrowDropUp
        }

        else -> {
            Icons.Filled.ArrowDropDown
        }
    }


    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .navigationBarsPadding()
            .imePadding()
    ) {
        if (data.listStatus != null) {
            IconButton(
                onClick = {
                    data.id?.let {
                        type.doBasedOnMyAnimeListType(
                            doIfAnime = { viewModel.deleteMyAnimeListAnimeList(it) },
                            doIfManga = { viewModel.deleteMyAnimeListMangaList(it) }
                        )
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
            label = { Text(text = MyAnimeListConstant.MYANIMELIST_STATUS) },
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = OtherConstant.EMPTY_STRING,
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
        val fieldEnabled =
            !(statusText.equals(MyAnimeListConstant.MYANIMELIST_STATUS_PLAN_TO_WATCH, true)
                    || statusText.equals(MyAnimeListConstant.MYANIMELIST_STATUS_PLAN_TO_READ, true))

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
            label = { Text(text = MyAnimeListConstant.MYANIMELIST_SCORE) },
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = OtherConstant.EMPTY_STRING,
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
                if (type.equals(MyAnimeListConstant.MYANIMELIST_TYPE_ANIME, true)) {
                    Text(
                        text = String.format(
                            OtherConstant.STRING_FORMAT_S_SPACE_S,
                            MyAnimeListConstant.MYANIMELIST_EPISODES,
                            MyAnimeListConstant.MYANIMELIST_WATCHED
                        )
                    )
                } else {
                    Text(
                        text = String.format(
                            OtherConstant.STRING_FORMAT_S_SPACE_S,
                            MyAnimeListConstant.MYANIMELIST_CHAPTERS,
                            MyAnimeListConstant.MYANIMELIST_READ
                        )
                    )
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
                text = type.setStringBasedOnMyAnimeListType(
                    setIfAnime = MyAnimeListConstant.MYANIMELIST_IS_REWATCHING,
                    setIfManga = MyAnimeListConstant.MYANIMELIST_IS_REREADING
                ),
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
                    type.doBasedOnMyAnimeListType(
                        doIfAnime = {
                            viewModel.updateMyAnimeListAnimeList(
                                it,
                                statusText.myAnimeListStatusApiFormat(),
                                isRewatchingState.value,
                                scoreIndex + OtherConstant.ONE,
                                numberWatchedText.toInt()
                            )
                        },
                        doIfManga = {
                            viewModel.updateMyAnimeListMangaList(
                                it,
                                statusText.myAnimeListStatusApiFormat(),
                                isRewatchingState.value,
                                scoreIndex + OtherConstant.ONE,
                                numberWatchedText.toInt()
                            )
                        }
                    )
                }

                scope.launch { scaffoldState.bottomSheetState.collapse() }
            }
        ) {
            val text = if (data.listStatus != null) {
                type.setStringBasedOnMyAnimeListType(
                    setIfAnime = MyAnimeListConstant.MYANIMELIST_UPDATE_LIST_ANIME,
                    setIfManga = MyAnimeListConstant.MYANIMELIST_UPDATE_LIST_MANGA
                )
            } else {
                type.setStringBasedOnMyAnimeListType(
                    setIfAnime = MyAnimeListConstant.MYANIMELIST_ADD_LIST_ANIME,
                    setIfManga = MyAnimeListConstant.MYANIMELIST_ADD_LIST_MANGA
                )
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
        menu.forEachIndexed { index, animeDetailsTab ->
            Tab(
                selected = menuId == index,
                text = { Text(stringResource(id = animeDetailsTab.title)) },
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
                OtherConstant.ZERO -> DESCRIPTION
                OtherConstant.ONE -> RELATED
                OtherConstant.TWO -> RECOMMENDATION
                else -> DESCRIPTION
            }
        }
    }
}