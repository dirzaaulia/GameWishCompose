import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.utils.OtherConstant

@Composable
fun WishlistTabMenu(
    menu: Array<WishlistTab>,
    menuId: Int,
    viewModel: HomeViewModel
) {
    TabRow(
        selectedTabIndex = menuId,
    ) {
        menu.forEachIndexed { index, wishlistTab ->
            Tab(
                selected = menuId == index,
                text = {
                    Text(
                        text = stringResource(id = wishlistTab.title),
                        style = MaterialTheme.typography.labelSmall,
                    )
                },
                onClick = { viewModel.selectWishlistTab(index) }
            )
        }
    }
}

enum class WishlistTab(@StringRes val title: Int) {
    GAME(R.string.game),
    ANIME(R.string.anime),
    MANGA(R.string.manga),
    MOVIE(R.string.movie),
    TVSHOW(R.string.tv_show);

    companion object {
        fun getTabFromResource(index: Int): WishlistTab {
            return when (index) {
                OtherConstant.ZERO -> GAME
                OtherConstant.ONE -> ANIME
                OtherConstant.TWO -> MANGA
                OtherConstant.THREE -> MOVIE
                OtherConstant.FOUR -> TVSHOW
                else -> GAME
            }
        }
    }
}