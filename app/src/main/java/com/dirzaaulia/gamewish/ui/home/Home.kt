package com.dirzaaulia.gamewish.ui.home

import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.base.LocalBaseViewModel
import com.dirzaaulia.gamewish.ui.details.GameDetails
import com.dirzaaulia.gamewish.ui.home.wishlist.Wishlist
import com.dirzaaulia.gamewish.ui.theme.GameWishTheme
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import java.util.*

@Composable
fun Home(
    viewModel: HomeViewModel,
    navigateToGameDetails: (Long) -> Unit,
    upPress: () -> Unit
) {
    val menu = HomeBottomNavMenu.values()
    val menuId: Int by viewModel.selectedBottomNav.collectAsState(initial = 0)
    val showSnackbar: Boolean by LocalBaseViewModel.current.isShowSnackbar.collectAsState()
    val openGameFilterDialog = remember { mutableStateOf(false) }
    val searchQuery: String by viewModel.query.collectAsState()

    Scaffold(
        backgroundColor = MaterialTheme.colors.primarySurface,
        topBar = {
            Crossfade(HomeBottomNavMenu.getHomeBottomNavMenuFromResource(menuId)) { destination ->
                when (destination) {
                    HomeBottomNavMenu.WISHLIST -> GameAppBar(openGameFilterDialog)
                    HomeBottomNavMenu.DEALS -> AnimeAppBar()
                    HomeBottomNavMenu.ABOUT -> AnimeAppBar()
                }
            }
        },
        bottomBar = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column {
                    if (showSnackbar) {
                        SnackbarInfo()
                    }
                    HomeBottomBar(menu = menu, menuId = menuId, viewModel = viewModel)
                }
            }
        }
    ) {
        Crossfade(
            targetState = HomeBottomNavMenu.getHomeBottomNavMenuFromResource(menuId)
        ) { destination ->
            when (destination) {
                HomeBottomNavMenu.WISHLIST -> Wishlist(
                    viewModel = viewModel,
                    navigateToGameDetails = navigateToGameDetails
                )
                HomeBottomNavMenu.DEALS -> Wishlist(
                    viewModel = viewModel,
                    navigateToGameDetails = navigateToGameDetails
                )
                HomeBottomNavMenu.ABOUT -> Wishlist(
                    viewModel = viewModel,
                    navigateToGameDetails = navigateToGameDetails
                )
            }
        }
        GameFilterDialog(
            viewModel = viewModel,
            searchQuery = searchQuery,
            openDialog = openGameFilterDialog.value
        ) {
            openGameFilterDialog.value = false
        }
    }
}

@Composable
fun HomeBottomBar(
    menu: Array<HomeBottomNavMenu>,
    menuId: Int,
    viewModel: HomeViewModel
) {
    BottomNavigation(
        modifier = Modifier.navigationBarsHeight(56.dp)
    ) {
        menu.forEach { menu ->
            BottomNavigationItem(
                icon = { Icon(imageVector = menu.icon, contentDescription = null) },
                label = { Text(stringResource(menu.title).uppercase(Locale.getDefault())) },
                selected = menu == HomeBottomNavMenu.getHomeBottomNavMenuFromResource(menuId),
                onClick = { viewModel.selectBottomNavMenu(menu.title) },
                selectedContentColor = MaterialTheme.colors.secondary,
                unselectedContentColor = LocalContentColor.current,
                modifier = Modifier.navigationBarsPadding()
            )
        }
    }
}

@Composable
fun SnackbarInfo(
    modifier: Modifier = Modifier
) {
    Column {
        Snackbar(modifier = modifier.padding(8.dp)) { Text(text = stringResource(id = R.string.exit_info)) }
    }
}

@Composable
fun GameAppBar(openGameFilterDialog: MutableState<Boolean>) {
    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier
            .height(80.dp)
            .statusBarsPadding()
    ) {
        Image(
            modifier = Modifier
                .padding(0.dp, 16.dp, 0.dp, 16.dp)
                .size(100.dp, 0.dp)
                .align(Alignment.CenterVertically)
                .aspectRatio(1.0f),
            painter = painterResource(id = R.drawable.ic_gamewish_dark),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = { openGameFilterDialog.value = true }
            ) {
                Icon(
                    imageVector = Icons.Filled.FilterList,
                    contentDescription = null,
                )
            }
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = { /* todo */ }
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
fun AnimeAppBar() {
    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier
            .height(80.dp)
            .statusBarsPadding()
    ) {
        Image(
            modifier = Modifier
                .padding(0.dp, 16.dp, 0.dp, 16.dp)
                .size(100.dp, 0.dp)
                .align(Alignment.CenterVertically)
                .aspectRatio(1.0f),
            painter = painterResource(id = R.drawable.ic_gamewish_dark),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = { /* todo */ }
            ) {
                Icon(
                    imageVector = Icons.Filled.Sort,
                    contentDescription = null,
                )
            }
        }
    }
}

@Preview
@Composable
fun AppBarPreviewLight() {
    GameWishTheme(darkTheme = false) {
        val openGameFilterDialog = remember { mutableStateOf(true) }
        GameAppBar(openGameFilterDialog)
    }
}

@Preview
@Composable
fun AppBarPreviewDark() {
    GameWishTheme(darkTheme = true) {
        val openGameFilterDialog = remember { mutableStateOf(true) }
        GameAppBar(openGameFilterDialog)
    }
}

enum class HomeBottomNavMenu(
    @StringRes val title: Int,
    val icon: ImageVector
) {
    WISHLIST(R.string.wishlist, Icons.Filled.FavoriteBorder),
    DEALS(R.string.deals, Icons.Filled.LocalOffer),
    ABOUT(R.string.about, Icons.Filled.Info);

    companion object {
        fun getHomeBottomNavMenuFromResource(@StringRes resource: Int): HomeBottomNavMenu {
            return when (resource) {
                R.string.wishlist -> WISHLIST
                R.string.deals -> DEALS
                R.string.about -> ABOUT
                else -> WISHLIST
            }
        }
    }
}