package com.dirzaaulia.gamewish.ui.home.wishlist.myanimelist

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.dirzaaulia.gamewish.data.model.myanimelist.ParentNode
import com.dirzaaulia.gamewish.ui.common.CommonVerticalList
import com.dirzaaulia.gamewish.ui.common.MyAnimeListWebViewClient
import com.dirzaaulia.gamewish.ui.common.item.CommonMyAnimeListItem
import com.dirzaaulia.gamewish.ui.home.HomeViewModel
import com.dirzaaulia.gamewish.utils.MyAnimeListConstant
import com.dirzaaulia.gamewish.utils.PlaceholderConstant
import com.dirzaaulia.gamewish.utils.ResponseResult
import com.dirzaaulia.gamewish.utils.isError
import com.dirzaaulia.gamewish.utils.isSucceeded

@Composable
fun WishlistMyAnimeList(
    accessTokenResult: ResponseResult<String>?,
    viewModel: HomeViewModel,
    lazyListState: LazyListState,
    data: LazyPagingItems<ParentNode>,
    animeType: String,
    emptyString: String,
    errorString: String,
    navigateToAnimeDetails: (Long, String) -> Unit,
) {
    when {
        accessTokenResult.isSucceeded -> {
            MyAnimeListLoggedIn(
                data,
                lazyListState,
                emptyString,
                errorString,
                viewModel,
                navigateToAnimeDetails,
                animeType
            )
        }
        accessTokenResult.isError -> {
            MyAnimeListWebViewClient(
                from = MyAnimeListConstant.MYANIMELIST_WEBVIEW_WISHLIST,
                viewModel = viewModel,
            )
        }
    }
}

@Composable
private fun MyAnimeListLoggedIn(
    data: LazyPagingItems<ParentNode>,
    lazyListState: LazyListState,
    emptyString: String,
    errorString: String,
    viewModel: HomeViewModel,
    navigateToAnimeDetails: (Long, String) -> Unit,
    animeType: String
) {
    CommonVerticalList(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        data = data,
        lazyListState = lazyListState,
        placeholderType = PlaceholderConstant.ANIME,
        emptyString = emptyString,
        errorString = errorString,
        doWhenMyAnimeListError = { viewModel.getMyAnimeListRefreshToken() }
    ) { parentNode ->
        CommonMyAnimeListItem(
            parentNode = parentNode,
            navigateToAnimeDetails = navigateToAnimeDetails,
            type = animeType,
        )
    }
}