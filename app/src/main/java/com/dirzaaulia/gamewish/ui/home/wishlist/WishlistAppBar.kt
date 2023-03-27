package com.dirzaaulia.gamewish.ui.home.wishlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
    Row (
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .visible(
                    if (searchMenu == SearchMenu.ANIME)
                        myAnimeListUser.id != null
                    else true
                )
                .weight(1f)
                .padding(start = 16.dp),
            text = sortStatus,
            style = MaterialTheme.typography.titleLarge
        )
        Row (
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = {
                    scope.launch {
                        scaffoldState.bottomSheetState.expand()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Sort,
                    contentDescription = OtherConstant.EMPTY_STRING,
                )
            }
            IconButton(
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