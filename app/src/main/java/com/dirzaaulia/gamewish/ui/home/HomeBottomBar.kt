import androidx.annotation.StringRes
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.utils.OtherConstant
import java.util.Locale

@Composable
fun HomeBottomBar(
    menu: Array<HomeBottomNavMenu>,
    menuId: Int,
    viewModel: HomeViewModel
) {
    BottomNavigation(
        modifier = Modifier.navigationBarsPadding()
    ) {
        menu.forEach { menu ->
            BottomNavigationItem(
                icon = { Icon(imageVector = menu.icon, contentDescription = OtherConstant.EMPTY_STRING) },
                label = { Text(stringResource(menu.title).uppercase(Locale.getDefault())) },
                selected = menu == HomeBottomNavMenu.getHomeBottomNavMenuFromResource(menuId),
                onClick = {
                    viewModel.selectBottomNavMenu(menu.title)
                },
                selectedContentColor = MaterialTheme.colors.secondary,
                unselectedContentColor = LocalContentColor.current,
                modifier = Modifier.navigationBarsPadding()
            )
        }
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
        fun getHomeBottomNavMenuFromResource(@StringRes resources: Int): HomeBottomNavMenu {
            return when (resources) {
                R.string.wishlist -> WISHLIST
                R.string.deals -> DEALS
                R.string.about -> ABOUT
                else -> WISHLIST
            }
        }
    }
}
