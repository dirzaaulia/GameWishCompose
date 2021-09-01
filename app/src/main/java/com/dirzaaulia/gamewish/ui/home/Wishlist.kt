package com.dirzaaulia.gamewish.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dirzaaulia.gamewish.data.model.Wishlist
import com.dirzaaulia.gamewish.ui.common.GameListItem
import com.google.accompanist.insets.statusBarsHeight

@Composable
fun Wishlist(
    listWishlist: List<Wishlist>,
    selectWishlist:(Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier) {
        item {
            Spacer(Modifier.statusBarsHeight())
        }
        item {
        }
        itemsIndexed(listWishlist) { _, wishlist ->
            Wishlist(wishlist = wishlist, selectWishlist = selectWishlist)
        }
    }
}

@Composable
fun Wishlist(
    wishlist: Wishlist,
    selectWishlist: (Long) -> Unit
) {
    Row(modifier = Modifier.padding(bottom = 8.dp)) {
        GameListItem(
            wishlist = wishlist,
            onClick = { wishlist.id?.let { id -> selectWishlist(id) } },
        )
    }
}