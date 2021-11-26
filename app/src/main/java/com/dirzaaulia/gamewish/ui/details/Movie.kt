package com.dirzaaulia.gamewish.ui.details

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Delete
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
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.model.myanimelist.Details
import com.dirzaaulia.gamewish.data.model.tmdb.Backdrop
import com.dirzaaulia.gamewish.data.model.tmdb.Movie
import com.dirzaaulia.gamewish.data.model.wishlist.MovieWishlist
import com.dirzaaulia.gamewish.extension.visible
import com.dirzaaulia.gamewish.ui.common.CommonAnimeCarousel
import com.dirzaaulia.gamewish.ui.common.CommonMovieCarousel
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
fun MovieDetails(
    viewModel: DetailsViewModel = hiltViewModel(),
    upPress: () -> Unit,
    type: String,
    movieId: Long,
//    navigateToMovieDetails: (Long, String) -> Unit,
) {
    val menu  = MovieDetailsTab.values()
    val menuId: Int by viewModel.selectedMovieTab.collectAsState(0)
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val data by viewModel.movieDetails.collectAsState(null)
    val dataResult by viewModel.movieDetailsResult.collectAsState(null)
    val loading = viewModel.loading.value
}

@Composable
fun MovieDescriptionHeader(
    type: String,
    loading: Boolean,
    data: Movie,
    backdrops: List<Backdrop>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
    ) {
        if (!backdrops.isNullOrEmpty()) {
            val pagerState = rememberPagerState(
                pageCount = backdrops.size,
                initialOffscreenLimit = 2,
            )

            CommonMovieCarousel(
                pagerState = pagerState,
                backdrops = backdrops
            )
        } else if (backdrops.isEmpty()) {
            NetworkImage(
                modifier = Modifier.visible(!loading),
                url = OtherConstant.NO_IMAGE_URL,
                contentDescription = null
            )
        }
    }
}

@Composable
fun MovieDetailsTopBar(
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
fun MovieDetailsSheetContent(
    type: String,
    data: Movie,
    wishlist: MovieWishlist,
    viewModel: DetailsViewModel,
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState
) {

    var statusExpanded by remember { mutableStateOf(false) }
    val status = wishlist.status ?: "Plan To Watch"
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
        if (wishlist.status != null) {
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
            val text = if (wishlist.status != null) {
                if (type.equals("Movie", true)) {
                    "Movie updated in your watchlist"
                } else {
                    "TV Show updated in your watchlist"
                }
            } else {
                if (type.equals("Movie", true)) {
                    "Movie added to your watchlist"
                } else {
                    "TV Show added to your watchlist"
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