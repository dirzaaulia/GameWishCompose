package com.dirzaaulia.gamewish.ui.details.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
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
import com.dirzaaulia.gamewish.extension.isError
import com.dirzaaulia.gamewish.extension.isSucceeded
import com.dirzaaulia.gamewish.extension.visible
import com.dirzaaulia.gamewish.ui.common.CommonGameCarousel
import com.dirzaaulia.gamewish.ui.common.CommonLoading
import com.dirzaaulia.gamewish.ui.common.ErrorConnect
import com.dirzaaulia.gamewish.theme.Red700
import com.dirzaaulia.gamewish.theme.White
import com.dirzaaulia.gamewish.ui.details.DetailsViewModel
import com.dirzaaulia.gamewish.utils.*
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun GameDetails(
    gameId: Long,
    upPress: () -> Unit,
    viewModel: DetailsViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val loading = viewModel.loading.value
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

    CommonLoading(visibility = loading)
    AnimatedVisibility(visible = !loading) {
        when {
            gameDetailsResult.isSucceeded -> {
                BottomSheetScaffold(
                    sheetContent = {
                        gameDetails?.let {
                            GameWishlistSheetContent(
                                it,
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
                        modifier = Modifier.fillMaxSize()
                    ) {
                        screenshots?.results?.let {
                            item {
                                GameDetailsHeader(
                                    screenshots = it,
                                    loading = loading,
                                    upPress = upPress
                                )
                            }
                        }
                        item {
                            gameDetails?.let {
                                GameDetailsMiddleContent(
                                    data = it,
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
                                    val text = "This game has been updated on your Wishlist."
                                    scaffoldState.snackbarHostState.showSnackbar(text)
                                } else {
                                    val text = "This game has been added to your Wishlist."
                                    scaffoldState.snackbarHostState.showSnackbar(text)
                                }
                                gameDetails?.id?.let { id -> viewModel.checkIfGameWishlisted(id) }
                            }
                        }
                        updateGameResult.isError -> {
                            LaunchedEffect(updateGameResult) {
                                if (wishlistData != null) {
                                    val text = "Something wrong happen went updating game in your Wishlist."
                                    scaffoldState.snackbarHostState.showSnackbar(text)
                                } else {
                                    val text = "Something wrong happen went adding game into your Wishlist"
                                    scaffoldState.snackbarHostState.showSnackbar(text)
                                }
                            }
                        }
                        deleteGameResult.isSucceeded -> {
                            LaunchedEffect(deleteGameResult) {
                                scaffoldState.snackbarHostState.showSnackbar("This game has been deleted from your Wishlist.")
                            }
                        }
                        deleteGameResult.isError -> {
                            LaunchedEffect(deleteGameResult) {
                                scaffoldState.snackbarHostState.showSnackbar("Something went wrong when deleting game from your Wishlist.")
                            }
                        }
                    }
                }
            }
            gameDetailsResult.isError -> {
                val errorScaffoldState = rememberScaffoldState()

                viewModel.setLoading(false)

                Scaffold(scaffoldState = errorScaffoldState) {
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
}

@Composable
fun GameDetailsHeader(
    screenshots: List<Screenshots>,
    loading: Boolean,
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
        } else if (screenshots.isEmpty()) {
            NetworkImage(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .visible(!loading),
                url = OtherConstant.NO_IMAGE_URL,
                contentDescription = null
            )
        }
        TopAppBar(
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
            contentColor = White,
            modifier = Modifier
                .visible(!loading)
                .statusBarsPadding()
        ) {
            IconButton(onClick = upPress) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.label_back),
                    tint = White
                )
            }
        }
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
                style = MaterialTheme.typography.caption,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            data.name?.let {
                Text(
                    modifier = Modifier.weight(1f),
                    text = it,
                    style = MaterialTheme.typography.h4
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
        Row {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                data.released?.let {
                    val releaseDate = if (it.isBlank()) {
                        stringResource(R.string.no_release_date)
                    } else {
                        it.changeDateFormat("yyyy-MM-dd")
                    }


                    Text(
                        text = stringResource(id = R.string.release_date),
                        style = MaterialTheme.typography.h6

                    )
                    Text(
                        text = releaseDate,
                        style = MaterialTheme.typography.body2
                    )
                }
                data.developers?.let {
                    Text(
                        text = stringResource(id = R.string.developer),
                        style = MaterialTheme.typography.h6
                    )
                    Text(
                        text = it.toDeveloper(),
                        style = MaterialTheme.typography.body2
                    )
                }
                data.publishers?.let {
                    Text(
                        text = stringResource(id = R.string.publishers),
                        style = MaterialTheme.typography.h6
                    )
                    Text(
                        text = it.toPublisher(),
                        style = MaterialTheme.typography.body2
                    )
                }
                data.website?.let { url ->
                    Text(
                        text = stringResource(id = R.string.link),
                        style = MaterialTheme.typography.h6
                    )
                    ClickableText(
                        text = AnnotatedString(
                            "Homepage",
                            SpanStyle(
                                color = MaterialTheme.colors.onSurface,
                                textDecoration = TextDecoration.Underline
                            )
                        ),
                        style = MaterialTheme.typography.body2,
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
                                        color = MaterialTheme.colors.onSurface,
                                        textDecoration = TextDecoration.Underline
                                    )
                                ),
                                style = MaterialTheme.typography.caption,
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
                        style = MaterialTheme.typography.h6
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
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(top = 8.dp)
            )
            GameDetailsPlatformList(data = it, code = 0)
        }
        data.stores?.let {
            Text(
                text = stringResource(id = R.string.stores),
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(top = 8.dp)
            )
            GameDetailsStoresList(data = it, code = 1)
        }
        data.description?.let {
            Text(
                text = stringResource(id = R.string.description),
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                textAlign = TextAlign.Justify,
                text = AnnotatedString(it.fromHtml()),
                style = MaterialTheme.typography.body1
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
    val status = gameWishlist?.status ?: "Plan To Buy"
    val statusList = listOf("Playing", "Completed", "On-Hold", "Dropped", "Plan To Buy")
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
        if (gameWishlist != null) {
            IconButton(
                onClick = {
                    gameWishlist.let {
                        viewModel.deleteGameWishlist(it)
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
        gameDetails.name?.let {
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
                val data = gameDetails.let {
                    GameWishlist(it.id, it.name, it.backgroundImage, statusText)
                }
                viewModel.addToGameWishlist(data)

                scope.launch {
                    scaffoldState.bottomSheetState.collapse()
                }
            }
        ) {
            val text = if (gameWishlist != null) {
                "Update Wishlist"
            } else {
                "Add To Wishlist"
            }

            Text(text = text)
        }
    }
}