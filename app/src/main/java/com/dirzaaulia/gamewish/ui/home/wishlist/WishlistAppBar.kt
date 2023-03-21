package com.dirzaaulia.gamewish.ui.home.wishlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dirzaaulia.gamewish.data.model.myanimelist.User
import com.dirzaaulia.gamewish.data.model.wishlist.SearchMenu
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.visible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun WishlistAppBar(
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState,
    searchMenu: SearchMenu,
    sortStatus: String,
    myAnimeListUser: User,
    navigateToSearch: (Int) -> Unit = { },
) {
    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier
            .statusBarsPadding()
            .wrapContentHeight()
    ) {
        Text(
            modifier = Modifier
                .visible(
                    if (searchMenu == SearchMenu.ANIME)
                        myAnimeListUser.id != null
                    else true
                )
                .padding(horizontal = 8.dp),
            text = sortStatus,
            style = MaterialTheme.typography.h6,
        )
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = {
                    scope.launch {
                        if (scaffoldState.bottomSheetState.isCollapsed) {
                            scaffoldState.bottomSheetState.expand()
                        } else {
                            scaffoldState.bottomSheetState.collapse()
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Sort,
                    contentDescription = OtherConstant.EMPTY_STRING,
                )
            }
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = { navigateToSearch(searchMenu.ordinal) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = OtherConstant.EMPTY_STRING,
                )
            }
        }
    }
}