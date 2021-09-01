package com.dirzaaulia.gamewish.ui.home

import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.base.LocalBaseViewModel
import com.dirzaaulia.gamewish.data.model.Wishlist
import com.dirzaaulia.gamewish.ui.theme.GameWishTheme
import com.dirzaaulia.gamewish.ui.theme.LocalImages
import com.dirzaaulia.gamewish.ui.theme.White
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import java.util.*

@Composable
fun Home(
    viewModel: HomeViewModel,
    navigateToWishlistDetails: (Long) -> Unit
) {
    val menu = HomeBottomNavMenu.values()
    val menuId: Int by viewModel.selectedBottomNav.collectAsState(initial = 0)
    val lazyListWishlist: LazyPagingItems<Wishlist> =
        viewModel.listWishlist.collectAsLazyPagingItems()
    val showSnackbar: Boolean by LocalBaseViewModel.current.isShowSnackbar.collectAsState()

    Scaffold(
        backgroundColor = MaterialTheme.colors.primarySurface,
        topBar ={
            Crossfade(HomeBottomNavMenu.getHomeBottomNavMenuFromResource(menuId)) { destination ->
                when (destination) {
                    HomeBottomNavMenu.WISHLIST -> GameAppBar()
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
                    BottomNavigation (
                        modifier = Modifier.navigationBarsHeight(56.dp)
                    ) {
                        menu.forEach { menu ->
                            BottomNavigationItem(
                                icon = { Icon(imageVector = menu.icon, contentDescription = null) } ,
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
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        Crossfade(HomeBottomNavMenu.getHomeBottomNavMenuFromResource(menuId)) { destination ->
            when (destination) {
                //TODO Need to add TabLayout for Wishlist category ( Game, Anime & Manga )

            }
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
fun GameAppBar() {
    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier.height(80.dp).statusBarsPadding()
    ) {
        Image(
            modifier = Modifier
                .padding(0.dp, 16.dp, 0.dp, 16.dp)
                .size(125.dp, 0.dp)
                .align(Alignment.CenterVertically)
                .aspectRatio(1.0f),
            painter = painterResource(GameWishTheme.images.lockupLogo),
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
        modifier = Modifier.height(80.dp).statusBarsPadding()
    ) {
        Image(
            modifier = Modifier
                .padding(0.dp, 16.dp, 0.dp, 16.dp)
                .size(125.dp, 0.dp)
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
fun AnimeAppBarPreviewLight() {
    GameWishTheme(darkTheme = false) {
        AnimeAppBar()
    }
}

@Preview
@Composable
fun AnimeAppBarPreviewDark() {
    GameWishTheme(darkTheme = true) {
        AnimeAppBar()
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
        fun getHomeBottomNavMenuFromResource(@StringRes resource : Int) :HomeBottomNavMenu {
            return when (resource) {
                R.string.wishlist -> WISHLIST
                R.string.deals -> DEALS
                R.string.about -> ABOUT
                else -> WISHLIST
            }
        }
    }
}