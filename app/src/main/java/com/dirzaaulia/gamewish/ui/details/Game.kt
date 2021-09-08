package com.dirzaaulia.gamewish.ui.details

import androidx.compose.animation.*
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.Wishlist
import com.dirzaaulia.gamewish.data.model.rawg.GameDetails
import com.dirzaaulia.gamewish.data.model.rawg.Screenshots
import com.dirzaaulia.gamewish.extension.isError
import com.dirzaaulia.gamewish.extension.isSucceeded
import com.dirzaaulia.gamewish.extension.visible
import com.dirzaaulia.gamewish.ui.common.*
import com.dirzaaulia.gamewish.ui.theme.Red700
import com.dirzaaulia.gamewish.ui.theme.White
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
    val wishlistData = viewModel.wishlistedData.value
    val gameDetailsResult by viewModel.gameDetailsResult.collectAsState(null)
    val gameDetails by viewModel.gameDetails.collectAsState(null)
    val screenshotsResult by viewModel.gameDetailsScreenshotsResult.collectAsState(null)
    val screenshots by viewModel.gameDetailsScreenshots.collectAsState(null)

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
                                scaffoldState)
                        }
                    },
                    scaffoldState = scaffoldState,
                    sheetPeekHeight = 0.dp,
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            when {
                                screenshotsResult.isSucceeded -> {
                                    screenshots?.results?.let {
                                        GameDetailsHeader(
                                            screenshots = it,
                                            loading = loading,
                                            upPress = upPress
                                        )
                                    }
                                }
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
                }
            }
            gameDetailsResult.isError -> {
                viewModel.setLoading(false)
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    ErrorConnect(text = stringResource(id = R.string.game_details_error)) {
                        viewModel.getGameDetails(gameId)
                        viewModel.getGameDetailsScreenshots(gameId)
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
        if (!screenshots.isNullOrEmpty()) {
            val pagerState = rememberPagerState(
                pageCount = screenshots.size,
                initialOffscreenLimit = 2,
            )

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
                    contentDescription = stringResource(R.string.label_back)
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
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.data_by_rawg),
                style = MaterialTheme.typography.caption,
                modifier = Modifier.weight(1f)
            )
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
        data.name?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.h4
            )
        }
        Row {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                data.released?.let {
                    Text(
                        text = stringResource(id = R.string.release_date),
                        style = MaterialTheme.typography.h6

                    )
                    Text(
                        text = textDateFormatter2(it),
                        style = MaterialTheme.typography.body2
                    )
                }
                data.developers?.let {
                    Text(
                        text = stringResource(id = R.string.developer),
                        style = MaterialTheme.typography.h6
                    )
                    Text(
                        text = gameDeveloperFormatter(it),
                        style = MaterialTheme.typography.body2
                    )
                }
                data.publishers?.let {
                    Text(
                        text = stringResource(id = R.string.publishers),
                        style = MaterialTheme.typography.h6
                    )
                    Text(
                        text = gamePublishersFormatter(it),
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_reddit_logo),
                            contentDescription = null
                        )
                        ClickableText(
                            text = AnnotatedString(
                                getSubReddit(url),
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
                        painter = painterResource(id = esrbRatingFormatter(it)),
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
                text = AnnotatedString(htmlToTextFormatter(it).toString()),
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
fun GameWishlistSheetContent(
    gameDetails: GameDetails,
    wishlist: Wishlist?,
    viewModel: DetailsViewModel,
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState
) {

    var expanded by remember { mutableStateOf(false) }
    val status = wishlist?.status ?: "Plan To Buy"
    val statusList = listOf("Playing", "Completed", "On-Hold", "Dropped", "Plan To Buy")
    var statusText by remember { mutableStateOf(status) }
    var textfieldSize by remember { mutableStateOf(Size.Zero)}
    val icon = if (expanded)
        Icons.Filled.ArrowDropUp //it requires androidx.compose.material:material-icons-extended
    else
        Icons.Filled.ArrowDropDown

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = {
                wishlist?.let {
                    viewModel.deleteWishlist(it)
                }

                scope.launch {
                    scaffoldState.bottomSheetState.collapse()
                    scaffoldState.snackbarHostState
                        .showSnackbar("This game has been deleted from your Wishlist.")
                }
            },
            modifier = Modifier
                .visible(wishlist != null)
                .align(Alignment.End)
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = null,
                tint = Red700
            )
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
            label = {Text("Status")},
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
                .width(with(LocalDensity.current){textfieldSize.width.toDp()})
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
                    Wishlist(it.id, it.name, it.backgroundImage, statusText)
                }
                viewModel.addToWishlist(data)
                gameDetails.id?.let { viewModel.checkIfGameWishlisted(it) }

                scope.launch {
                    scaffoldState.bottomSheetState.collapse()

                    val text = if (wishlist != null) {
                        "This game has been updated on your Wishlist."
                    } else {
                        "This game has been added to your Wishlist."
                    }

                    scaffoldState.snackbarHostState.showSnackbar(text)
                }
            }
        ) {
            val text = if (wishlist != null) {
                "Update Wishlist"
            } else {
                "Add To Wishlist"
            }

            Text(text = text)
        }
    }
}