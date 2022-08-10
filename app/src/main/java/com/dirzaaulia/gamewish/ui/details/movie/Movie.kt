package com.dirzaaulia.gamewish.ui.details.movie

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.tmdb.Image
import com.dirzaaulia.gamewish.data.model.tmdb.Movie
import com.dirzaaulia.gamewish.data.model.tmdb.MovieDetail
import com.dirzaaulia.gamewish.data.model.wishlist.MovieWishlist
import com.dirzaaulia.gamewish.extension.isError
import com.dirzaaulia.gamewish.extension.isSucceeded
import com.dirzaaulia.gamewish.extension.visible
import com.dirzaaulia.gamewish.theme.Grey700
import com.dirzaaulia.gamewish.theme.Red700
import com.dirzaaulia.gamewish.theme.White
import com.dirzaaulia.gamewish.ui.common.*
import com.dirzaaulia.gamewish.ui.details.DetailsViewModel
import com.dirzaaulia.gamewish.utils.*
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MovieDetails(
    viewModel: DetailsViewModel = hiltViewModel(),
    upPress: () -> Unit,
    movieId: Long,
    type: String,
    navigateToMovieDetails: (Long, String) -> Unit,
) {
    val menu  = MovieDetailsTab.values()
    val menuId: Int by viewModel.selectedMovieTab.collectAsState(0)
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val data by viewModel.movieDetails.collectAsState(null)
    val wishlistData = viewModel.movieWishlistedData.value
    val dataResult by viewModel.movieDetailsResult.collectAsState(null)
    val images by viewModel.movieImages.collectAsState(null)
    val movieRecommendations: LazyPagingItems<Movie>? =
        viewModel.movieRecommendations?.collectAsLazyPagingItems()
    val updateMovieResult by viewModel.updateMovieResult.collectAsState()
    val deleteMovieResult by viewModel.deleteMovieResult.collectAsState()
    val loading = viewModel.loading.value
    val errorMessage =
        stringResource(id = R.string.tv_show_details_error)

    LaunchedEffect(viewModel) {
        viewModel.setMovieType(type)
        viewModel.setMovieId(movieId)
        viewModel.getMovieImages(movieId)
        viewModel.getMovieDetails(movieId)
        viewModel.getMovieRecommendations()
        viewModel.checkIfMovieWishlisted(movieId)
    }

    when {
        updateMovieResult.isSucceeded -> {
            LaunchedEffect(updateMovieResult) {
                if (wishlistData != null) {
                    val text = if (type.equals("Movie", true)) {
                        "This Movie has been updated on your Watchlist"
                    } else {
                        "This TV Show has been updated on your Watchlist"
                    }
                    scaffoldState.snackbarHostState.showSnackbar(text)
                } else {
                    val text = if (type.equals("Movie", true)) {
                        "This Movie has been added to your Watchlist"
                    } else {
                        "This TV Show has been added to your Watchlist"
                    }
                    scaffoldState.snackbarHostState.showSnackbar(text)
                }
            }
        }
        updateMovieResult.isError -> {
            LaunchedEffect(updateMovieResult) {
                if (wishlistData != null) {
                    val text = if (type.equals("Movie", true)) {
                        "Something wrong happen went updating Movie on your Watchlist"
                    } else {
                        "Something wrong happen went updating TV Show on your Watchlist"
                    }
                    scaffoldState.snackbarHostState.showSnackbar(text)
                } else {
                    val text = if (type.equals("Movie", true)) {
                        "Something wrong happen went adding Movie into your Watchlist"
                    } else {
                        "Something wrong happen went updating TV Show on your Watchlist"
                    }
                    scaffoldState.snackbarHostState.showSnackbar(text)
                }
            }
        }
        deleteMovieResult.isSucceeded -> {
            LaunchedEffect(deleteMovieResult) {
                val text = if (type.equals("Movie", true)) {
                    "This Movie has been deleted from your Watchlist"
                } else {
                    "This TV Show has been deleted from your Watchlist"
                }
                scaffoldState.snackbarHostState.showSnackbar(text)
            }
        }
        deleteMovieResult.isError -> {
            LaunchedEffect(deleteMovieResult) {
                val text = if (type.equals("Movie", true)) {
                    "Something wrong happen when deleting Movie from your Watchlist"
                } else {
                    "Something wrong happen when deleting TV Show from your Watchlist"
                }
                scaffoldState.snackbarHostState.showSnackbar(text)
            }
        }
    }

    CommonLoading(visibility = loading)
    AnimatedVisibility(visible = !loading) {
        when {
            dataResult.isError -> {
                val errorScaffoldState = rememberScaffoldState()

                viewModel.setLoading(false)

                Scaffold(scaffoldState = errorScaffoldState) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ErrorConnect(text = errorMessage) {
                            viewModel.getMovieDetails(movieId)
                        }
                    }
                }
            }
            dataResult.isSucceeded -> {
                Scaffold(
                    topBar = {
                        MovieDetailsTopBar(
                            upPress = upPress,
                            loading = loading,
                            imageList = images,
                        )
                    }
                ) {
                    BottomSheetScaffold(
                        scaffoldState = scaffoldState,
                        topBar = {
                            MovieDetailsTabMenu(
                                menu = menu,
                                menuId = menuId,
                                viewModel = viewModel
                            )
                        },
                        sheetContent = {
                            data?.let { value ->
                                MovieDetailsSheetContent(
                                    data = value,
                                    type = type,
                                    wishlist = wishlistData,
                                    viewModel = viewModel,
                                    scope = scope,
                                    scaffoldState = scaffoldState
                                )
                            }
                        },
                        sheetPeekHeight = 0.dp
                    ) { innerPadding ->
                        val innerModifier = Modifier.padding(innerPadding)
                        Crossfade(
                            targetState = MovieDetailsTab.getTabFromResource(menuId)
                        ) { destination ->
                            when (destination) {
                                MovieDetailsTab.DESCRIPTION -> {
                                    MovieDescriptionTab(
                                        modifier = innerModifier,
                                        loading = loading,
                                        data = data,
                                        type = type,
                                        scope = scope,
                                        scaffoldState = scaffoldState
                                    )
                                }
                                MovieDetailsTab.RECOMMENDATION -> {
                                    movieRecommendations?.let { item ->
                                        MovieRecommendationsTab(
                                            data = item,
                                            type = type,
                                            navigateToMovieDetails = navigateToMovieDetails
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
fun MovieRecommendationsTab(
    modifier: Modifier = Modifier,
    data: LazyPagingItems<Movie>,
    type: String,
    navigateToMovieDetails: (Long, String) -> Unit,
) {
    val emptyString = if (type.equals("Movie", true)) {
        "There is no recommendations for this Movie"
    } else {
        "There is no recommendations for this TV Show"
    }

    val errorString = if (type.equals("Movie", true)) {
        stringResource(id = R.string.search_movie_recommendations_error)
    } else {
        stringResource(id = R.string.search_tv_show_recommendations_error)
    }

    Column(modifier = modifier.padding(8.dp)) {
        if (type.equals("Movie", true)) {
            Text(
                text = stringResource(R.string.movie_data_source),
                style = MaterialTheme.typography.caption,
            )
        } else {
            Text(
                text = stringResource(R.string.tv_show_data_source),
                style = MaterialTheme.typography.caption,
            )
        }
        CommonVerticalList(
            data = data,
            lazyListState = rememberLazyListState(),
            emptyString = emptyString,
            errorString = errorString,
        ) { movie ->
            CommonMovieItem(
                movie = movie,
                type = type,
                navigateToDetails = navigateToMovieDetails
            )
        }
    }
}

@Composable
fun MovieDescriptionTab(
    modifier: Modifier = Modifier,
    loading: Boolean,
    data: MovieDetail?,
    type: String,
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState
) {
    LazyColumn(
        modifier = modifier
            .padding(8.dp)
            .navigationBarsPadding()
    ) {
        item {
            MovieDescriptionHeader(
                loading = loading,
                data = data,
                type = type
            )
        }
        item {
            MovieDescriptionFooter(
                data = data,
                scope = scope,
                type = type,
                scaffoldState = scaffoldState
            )
        }
    }
}

@Composable
fun MovieDescriptionHeader(
    loading: Boolean,
    data: MovieDetail?,
    type: String
) {
    Row {
        Card(
            modifier = Modifier
                .fillMaxHeight()
                .padding(end = 4.dp)
                .weight(1f),
            shape = MaterialTheme.shapes.small
        ) {
            if (data?.posterPath.isNullOrBlank()) {
                NetworkImage(
                    url = OtherConstant.NO_IMAGE_URL,
                    contentDescription = null,
                    modifier = Modifier.visible(!loading)
                )
            } else {
                NetworkImage(
                    url = "${TmdbConstant.TMDB_BASE_IMAGE_URL}${data?.posterPath}",
                    contentDescription = null,
                    modifier = Modifier.visible(!loading)
                )
            }
        }
        Card(
            modifier = Modifier
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
                    data?.status?.let { status ->
                        Text(
                            text = status.replace("_", " ").capitalizeWords(),
                            color = White,
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier.padding(4.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Text(
                    text = stringResource(R.string.runtime),
                    style = MaterialTheme.typography.subtitle2,
                )
                if (type.equals("Movie", true)) {
                    Text(
                        text = "${data?.runtime}",
                        color = Color.Gray,
                        style = MaterialTheme.typography.caption,
                    )
                } else {
                    Text(
                        text = "${data?.episodeRunTime?.get(0) ?: stringResource(R.string.unknown)}",
                        color = Color.Gray,
                        style = MaterialTheme.typography.caption,
                    )
                }
                Text(
                    text = "Score",
                    style = MaterialTheme.typography.subtitle2,
                )
                Text(
                    text = "${data?.voteAverage}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.caption,
                )
                Text(
                    text = "Popularity",
                    style = MaterialTheme.typography.subtitle2,
                )
                Text(
                    text = "${data?.popularity}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.caption,
                )
                Text(
                    text = "Production Companies",
                    style = MaterialTheme.typography.subtitle2,
                )
                Text(
                    text = data?.productionCompanies.toProductionCompany(),
                    color = Color.Gray,
                    style = MaterialTheme.typography.caption,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun MovieDescriptionFooter(
    data: MovieDetail?,
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState,
    type: String
) {
    Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
        data?.id?.let {
            if (type.equals("Movie", true)) {
                Text(
                    text = stringResource(id = R.string.movie_data_source),
                    style = MaterialTheme.typography.caption,
                )
            } else {
                Text(
                    text = stringResource(id = R.string.tv_show_data_source),
                    style = MaterialTheme.typography.caption,
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (type.equals("Movie", true)) {
                data?.title?.let {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = it,
                        style = MaterialTheme.typography.h4,
                    )
                }
            } else {
                data?.name?.let {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = it,
                        style = MaterialTheme.typography.h4,
                    )
                }
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
        data?.tagline?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        data?.releaseDate?.let {
            val releaseDate = if (it.isNotBlank()) {
                it.changeDateFormat("yyyy-MM-dd")
            } else {
                stringResource(R.string.no_release_date)
            }
            Text(
                text = releaseDate,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        if (type.equals("Movie", true)) {
            Row {
                data?.budget?.let {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Budget : ${String.format(Locale.ITALIAN, "%,d", it)}",
                        style = MaterialTheme.typography.caption,
                    )
                }
                data?.revenue?.let {
                    Text(
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End,
                        text = "Revenue : ${String.format(Locale.ITALIAN, "%,d", it)}",
                        style = MaterialTheme.typography.caption,
                    )
                }
            }
        } else {
            Row {
                data?.numberOfSeasons?.let {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Seasons : ${String.format(Locale.ITALIAN, "%,d", it)}",
                        style = MaterialTheme.typography.caption,
                    )
                }
                data?.numberOfEpisodes?.let {
                    Text(
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End,
                        text = "Episodes : ${String.format(Locale.ITALIAN, "%,d", it)}",
                        style = MaterialTheme.typography.caption,
                    )
                }
            }
        }

        data?.genres?.let {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                textAlign = TextAlign.Center,
                text = it.toMovieGenre(),
                style = MaterialTheme.typography.body2,
            )
        }
        data?.overview?.let {
            Text(
                text = stringResource(R.string.synopsis),
                style = MaterialTheme.typography.h6,
            )
            Text(
                textAlign = TextAlign.Justify,
                text = it.fromHtml(),
                style = MaterialTheme.typography.body1,
            )
        }
    }
}

@Composable
fun MovieDetailsTopBar(
    upPress: () -> Unit,
    loading: Boolean,
    imageList: List<Image>?
) {
    Box(modifier = Modifier
        .height(300.dp)
        .fillMaxWidth()
    ) {
        if (!imageList.isNullOrEmpty()) {
            val pagerState = rememberPagerState()

            CommonMovieCarousel(
                pagerState = pagerState,
                imageList = imageList
            )
        } else if (imageList?.isEmpty() == true) {
            NetworkImage(
                url = OtherConstant.NO_IMAGE_URL,
                contentDescription = null,
                modifier = Modifier.visible(!loading)
            )
        }
        TopAppBar(
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
            contentColor = White,
            modifier = Modifier
                .height(80.dp)
                .statusBarsPadding()
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
        }
    }
}

@Composable
fun MovieDetailsSheetContent(
    data: MovieDetail,
    type: String,
    wishlist: MovieWishlist?,
    viewModel: DetailsViewModel,
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState
) {

    var statusExpanded by remember { mutableStateOf(false) }
    val status = wishlist?.status ?: "Plan To Watch"
    val statusList = listOf("Watching", "Completed", "On-Hold", "Dropped", "Plan To Watch")
    var statusText by remember { mutableStateOf(status) }

    var textfieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = when {
        statusExpanded -> {
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
            .navigationBarsPadding().imePadding()
    ) {
        if (wishlist != null) {
            IconButton(
                onClick = {
                    wishlist.let {
                        viewModel.deleteMovieWishlist(it)
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
        OutlinedButton(
            modifier = Modifier
                .padding(top = 4.dp, bottom = 16.dp)
                .fillMaxWidth(),
            onClick = {
                if (type.equals("Movie", true)) {
                    val item = data.let {
                        MovieWishlist(it.id, it.title, it.posterPath, statusText, type)
                    }
                    viewModel.addToMovieWishlist(item)
                } else {
                    val item = data.let {
                        MovieWishlist(it.id, it.name, it.posterPath, statusText, type)
                    }
                    viewModel.addToMovieWishlist(item)
                }


                scope.launch {
                    scaffoldState.bottomSheetState.collapse()
                }
            }
        ) {
            val text = if (wishlist?.status != null) {
                if (type.equals("Movie", true)) {
                    "Update Movie in your Watchlist"
                } else {
                    "Update TV Show in your Watchlist"
                }
            } else {
                if (type.equals("Movie", true)) {
                    "Add Movie into your Watchlist"
                } else {
                    "Add TV Show into your Watchlist"
                }
            }
            Text(text = text)
        }
    }
}

@Composable
fun MovieDetailsTabMenu(
    menu: Array<MovieDetailsTab>,
    menuId: Int,
    viewModel: DetailsViewModel
) {
    TabRow(selectedTabIndex = menuId) {
        menu.forEachIndexed { index, movieDetailsTab ->
            Tab(
                selected = menuId == index,
                text = { Text(stringResource(id = movieDetailsTab.title)) },
                onClick = { viewModel.selectMovieDetailsTab(index) }
            )
        }
    }
}

enum class MovieDetailsTab(@StringRes val title: Int) {
    DESCRIPTION(R.string.description),
    RECOMMENDATION(R.string.recommendation);

    companion object {
        fun getTabFromResource(index: Int): MovieDetailsTab {
            return when (index) {
                0 -> DESCRIPTION
                1 -> RECOMMENDATION
                else -> DESCRIPTION
            }
        }
    }
}