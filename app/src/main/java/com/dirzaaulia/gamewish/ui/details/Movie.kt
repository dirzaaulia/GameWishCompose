package com.dirzaaulia.gamewish.ui.details

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import java.util.*

@Composable
fun MovieDetails(
    viewModel: DetailsViewModel = hiltViewModel(),
    upPress: () -> Unit,
    movieId: Long,
    navigateToMovieDetails: (Long) -> Unit,
) {
    val menu  = MovieDetailsTab.values()
    val menuId: Int by viewModel.selectedMovieTab.collectAsState(0)
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val data by viewModel.movieDetails.collectAsState(null)
    val dataResult by viewModel.movieDetailsResult.collectAsState(null)
    val images by viewModel.movieImages.collectAsState(null)
    val imagesResult by viewModel.movieImagesResult.collectAsState(null)
    val movieRecommendations: LazyPagingItems<Movie> =
        viewModel.movieRecommendations.collectAsLazyPagingItems()
    val loading = viewModel.loading.value
    val errorMessage =
        stringResource(id = R.string.tv_show_details_error)

    LaunchedEffect(viewModel) {
        viewModel.setMovieId(movieId)
        viewModel.getMovieImages(movieId)
        viewModel.getMovieDetails(movieId)
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
                                    wishlist = null,
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
                                        scope = scope,
                                        scaffoldState = scaffoldState
                                    )
                                }
                                MovieDetailsTab.RECOMMENDATION -> {
                                    MovieRecommendationsTab(
                                        data = movieRecommendations,
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

@Composable
fun MovieRecommendationsTab(
    modifier: Modifier = Modifier,
    data: LazyPagingItems<Movie>,
    navigateToMovieDetails: (Long) -> Unit,
) {
    Column(modifier = modifier.padding(8.dp)) {
        Text(
            text = stringResource(R.string.movie_data_source),
            style = MaterialTheme.typography.caption,
        )
        CommonVerticalList(
            data = data,
            lazyListState = rememberLazyListState(),
            emptyString = "There is no recommendations for this Movie",
            errorString = stringResource(id = R.string.search_movie_recommendations_error),
        ) { movie ->
            CommonMovieItem(
                movie = movie,
                type = "Movie",
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
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState
) {
    LazyColumn(modifier = modifier.padding(8.dp)) {
        item {
            MovieDescriptionHeader(
                loading = loading,
                data = data
            )
        }
        item {
            MovieDescriptionFooter(
                data = data,
                scope = scope,
                scaffoldState = scaffoldState
            )
        }
    }
}

@Composable
fun MovieDescriptionHeader(
    loading: Boolean,
    data: MovieDetail?,
) {
    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        Card(
            modifier = Modifier
                .fillMaxHeight()
                .padding(end = 4.dp)
                .weight(1f),
            shape = MaterialTheme.shapes.small
        ) {
            if (data?.posterPath.isNullOrBlank()) {
                NetworkImage(
                    modifier = Modifier.visible(!loading),
                    url = OtherConstant.NO_IMAGE_URL,
                    contentDescription = null
                )
            } else {
                NetworkImage(
                    modifier = Modifier.visible(!loading),
                    url = "${TmdbConstant.TMDB_BASE_IMAGE_URL}${data?.posterPath}",
                    contentDescription = null
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
                            text = status.replace("_"," ").capitalizeWords(),
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
                Text(
                    text = "${data?.runtime}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.caption,
                )
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
                    text = movieProductionCompaniesFormat(data?.productionCompanies),
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
    scaffoldState: BottomSheetScaffoldState
) {
    Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
        data?.title?.let {
            Text(
                text = stringResource(id = R.string.movie_data_source),
                style = MaterialTheme.typography.caption,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            data?.title?.let {
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
        data?.tagline?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        data?.releaseDate?.let {
            Text(
                text = textDateFormatter2(it),
                style = MaterialTheme.typography.body2,
            )
        }
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
        data?.genres?.let {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                textAlign = TextAlign.Center,
                text = movieGenreFormat(it),
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
                text = htmlToTextFormatter(it).toString(),
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
                modifier = Modifier.visible(!loading),
                url = OtherConstant.NO_IMAGE_URL,
                contentDescription = null
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
            .navigationBarsWithImePadding()
    ) {
        if (wishlist?.status != null) {
            IconButton(
                onClick = {
                    data.id?.let {
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
                data.id?.let {

                }
                scope.launch { scaffoldState.bottomSheetState.collapse() }
            }
        ) {
            val text = if (wishlist?.status != null) {
                "Movie updated in your watchlist"
            } else {

                "Movie added to your watchlist"
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