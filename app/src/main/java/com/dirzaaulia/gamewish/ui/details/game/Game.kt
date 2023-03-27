package com.dirzaaulia.gamewish.ui.details.game

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.rawg.EsrbRating
import com.dirzaaulia.gamewish.data.model.rawg.GameDetails
import com.dirzaaulia.gamewish.data.model.rawg.Screenshots
import com.dirzaaulia.gamewish.data.model.wishlist.GameWishlist
import com.dirzaaulia.gamewish.ui.common.CommonGameCarousel
import com.dirzaaulia.gamewish.ui.common.CommonLoading
import com.dirzaaulia.gamewish.ui.common.ErrorConnect
import com.dirzaaulia.gamewish.ui.common.list.GameDetailsPlatformList
import com.dirzaaulia.gamewish.ui.common.list.GameDetailsStoresList
import com.dirzaaulia.gamewish.ui.details.DetailsViewModel
import com.dirzaaulia.gamewish.utils.*
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GameDetails(
    gameId: Long,
    upPress: () -> Unit,
    viewModel: DetailsViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val wishlistData = viewModel.gameWishlistedData.value
    val gameDetailsResult by viewModel.gameDetailsResult.collectAsState(null)
    val gameDetails by viewModel.gameDetails.collectAsState(null)
    val screenshots by viewModel.gameDetailsScreenshots.collectAsState(null)
    val updateGameResult by viewModel.updateGameResult.collectAsState()
    val deleteGameResult by viewModel.deleteGameResult.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.getGameDetails(gameId)
        viewModel.getGameDetailsScreenshots(gameId)
        viewModel.checkIfGameWishlisted(gameId)
    }

    when {
        gameDetailsResult.isLoading -> {
            CommonLoading(visibility = true)
        }
        gameDetailsResult.isSucceeded -> {
            BottomSheetScaffold(
                sheetContent = {
                    gameDetails?.let { gameDetail ->
                        GameWishlistSheetContent(
                            gameDetail,
                            wishlistData,
                            viewModel,
                            scope,
                            scaffoldState
                        )
                    }
                },
                scaffoldState = scaffoldState,
                sheetPeekHeight = 0.dp,
            ) {
                LazyColumn(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .fillMaxSize()
                ) {
                    screenshots?.results?.let { screenshots ->
                        item {
                            GameDetailsHeader(
                                screenshots = screenshots,
                                upPress = upPress
                            )
                        }
                    }
                    item {
                        gameDetails?.let { gameDetail ->
                            GameDetailsMiddleContent(
                                data = gameDetail,
                                scope = scope,
                                scaffoldState = scaffoldState
                            )
                        }
                    }
                }

                when {
                    updateGameResult.isSucceeded -> {
                        LaunchedEffect(updateGameResult) {
                            if (wishlistData != null) {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    RawgConstant.RAWG_WISHLIST_UPDATED
                                )
                            } else {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    RawgConstant.RAWG_WISHLIST_ADDED
                                )
                            }
                            gameDetails?.id?.let { id -> viewModel.checkIfGameWishlisted(id) }
                        }
                    }

                    updateGameResult.isError -> {
                        LaunchedEffect(updateGameResult) {
                            if (wishlistData != null) {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    RawgConstant.RAWG_WISHLIST_UPDATE_ERROR
                                )
                            } else {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    RawgConstant.RAWG_WISHLIST_ADD_ERROR
                                )
                            }
                        }
                    }

                    deleteGameResult.isSucceeded -> {
                        LaunchedEffect(deleteGameResult) {
                            scaffoldState.snackbarHostState.showSnackbar(
                                RawgConstant.RAWG_WISHLIST_DELETED
                            )
                        }
                    }

                    deleteGameResult.isError -> {
                        LaunchedEffect(deleteGameResult) {
                            scaffoldState.snackbarHostState.showSnackbar(
                                RawgConstant.RAWG_WISHLIST_DELETE_ERROR
                            )
                        }
                    }
                }
            }
        }

        gameDetailsResult.isError -> {
            val snackbarHostState = remember { SnackbarHostState() }
            Scaffold(
                modifier = Modifier.navigationBarsPadding(),
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    ErrorConnect(
                        text = stringResource(
                            id = R.string.game_details_error
                        )
                    ) {
                        viewModel.getGameDetails(gameId)
                        viewModel.getGameDetailsScreenshots(gameId)
                        viewModel.checkIfGameWishlisted(gameId)
                    }
                }
            }
        }
    }
}

@Composable
fun GameDetailsHeader(
    screenshots: List<Screenshots>,
    upPress: () -> Unit
) {
    Box {
        if (screenshots.isNotEmpty()) {
            val pagerState = rememberPagerState()

            CommonGameCarousel(
                pagerState = pagerState,
                height = 300.dp,
                screenshots = screenshots
            )
        } else {
            NetworkImage(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                url = OtherConstant.NO_IMAGE_URL,
                contentDescription = OtherConstant.EMPTY_STRING,

            )
        }
        TopAppBar(
            modifier = Modifier.statusBarsPadding(),
            title = { },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = White
            ),
            navigationIcon = {
                IconButton(onClick = upPress) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.label_back),
                        tint = White
                    )
                }
            }
        )
    }
}

@Composable
fun GameDetailsMiddleContent(
    data: GameDetails,
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 8.dp)
    ) {
        data.name?.let {
            Text(
                text = stringResource(id = R.string.data_by_rawg),
                style = MaterialTheme.typography.labelSmall,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            data.name?.let {
                Text(
                    modifier = Modifier.weight(1f),
                    text = it,
                    style = MaterialTheme.typography.displaySmall
                )
            }
            OutlinedButton(
                modifier = Modifier.size(50.dp),
                onClick = {
                    scope.launch {
                        if (!scaffoldState.bottomSheetState.isVisible) {
                            scaffoldState.bottomSheetState.expand()
                        } else {
                            scaffoldState.bottomSheetState.hide()
                        }
                    }
                },
                shape = CircleShape,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
                contentPadding = PaddingValues(0.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = OtherConstant.EMPTY_STRING,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Row {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                data.released?.let {
                    val releaseDate = if (it.isBlank()) {
                        stringResource(R.string.no_release_date)
                    } else {
                        it.changeDateFormat(OtherConstant.DATE_FORMAT_STRIP_yyyy_MM_dd)
                    }


                    Text(
                        text = stringResource(id = R.string.release_date),
                        style = MaterialTheme.typography.headlineLarge

                    )
                    Text(
                        text = releaseDate,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                data.developers?.let {
                    Text(
                        text = stringResource(id = R.string.developer),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        text = it.toDeveloper(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                data.publishers?.let {
                    Text(
                        text = stringResource(id = R.string.publishers),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        text = it.toPublisher(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                data.website?.let { url ->
                    Text(
                        text = stringResource(id = R.string.link),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    ClickableText(
                        text = AnnotatedString(
                            RawgConstant.RAWG_HOMEPAGE,
                            SpanStyle(
                                color = MaterialTheme.colorScheme.onSurface,
                                textDecoration = TextDecoration.Underline
                            )
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        onClick = { openLink(context, url) }
                    )
                }
                data.redditUrl?.let { url ->
                    if (url.isNotEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_reddit_logo),
                                contentDescription = null
                            )
                            ClickableText(
                                text = AnnotatedString(
                                    url.getSubReddit(),
                                    SpanStyle(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        textDecoration = TextDecoration.Underline
                                    )
                                ),
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.weight(1f),
                                onClick = { openLink(context, url) }
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                data.esrbRating?.let {
                    Text(
                        text = stringResource(id = R.string.esrb_rating),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Image(
                        painter = painterResource(id = EsrbRating.getRatingDrawable(it)),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp, 70.dp)
                    )
                }
            }
        }
        data.platforms?.let {
            Text(
                text = stringResource(id = R.string.platforms),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
            GameDetailsPlatformList(data = it, code = 0)
        }
        data.stores?.let {
            Text(
                text = stringResource(id = R.string.stores),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
            GameDetailsStoresList(data = it, code = 1)
        }
        data.description?.let {
            Text(
                text = stringResource(id = R.string.description),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                textAlign = TextAlign.Justify,
                text = AnnotatedString(it.fromHtml()),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun GameWishlistSheetContent(
    gameDetails: GameDetails,
    gameWishlist: GameWishlist?,
    viewModel: DetailsViewModel,
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState
) {

    var expanded by remember { mutableStateOf(false) }
    val status = gameWishlist?.status.replaceIfNull(RawgConstant.RAWG_STATUS_PLAN_TO_BUY)
    val statusList = listOf(
        RawgConstant.RAWG_STATUS_PLAYING,
        RawgConstant.RAWG_STATUS_COMPLETED,
        RawgConstant.RAWG_STATUS_ON_HOLD,
        RawgConstant.RAWG_STATUS_DROPPED,
        RawgConstant.RAWG_STATUS_PLAN_TO_BUY
    )
    var statusText by remember { mutableStateOf(status) }
    var textfieldSize by remember { mutableStateOf(Size.Zero) }
    val icon = if (expanded)
        Icons.Filled.ArrowDropUp //it requires androidx.compose.material:material-icons-extended
    else
        Icons.Filled.ArrowDropDown

    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        if (gameWishlist != null) {
            IconButton(
                onClick = {
                    gameWishlist.let {
                        viewModel.deleteGameWishlist(it)
                    }
                    scope.launch {
                        scaffoldState.bottomSheetState.hide()
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = OtherConstant.EMPTY_STRING,
                    tint = Red
                )
            }
        }
        gameDetails.name?.let { name ->
            Text(
                text = name,
                style = MaterialTheme.typography.headlineLarge
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
            label = { Text(RawgConstant.RAWG_STATUS) },
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = OtherConstant.EMPTY_STRING,
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
                    text = { Text(text = item) },
                    onClick = {
                        statusText = item
                        expanded = false
                    }
                )
            }
        }
        OutlinedButton(
            modifier = Modifier
                .padding(top = 4.dp, bottom = 16.dp)
                .fillMaxWidth(),
            onClick = {
                val data = gameDetails.let {
                    GameWishlist(it.id, it.name, it.backgroundImage, statusText)
                }
                viewModel.addToGameWishlist(data)

                scope.launch {
                    scaffoldState.bottomSheetState.hide()
                }
            }
        ) {
            val text = if (gameWishlist != null) {
                RawgConstant.RAWG_UPDATE_WISHLIST
            } else {
                RawgConstant.RAWG_ADD_WISHLIST
            }

            Text(text = text)
        }
    }
}