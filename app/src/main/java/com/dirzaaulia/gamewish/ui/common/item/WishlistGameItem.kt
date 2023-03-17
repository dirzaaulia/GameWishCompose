package com.dirzaaulia.gamewish.ui.common.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.dirzaaulia.gamewish.data.model.wishlist.GameWishlist
import com.dirzaaulia.gamewish.utils.NetworkImage

@Composable
fun WishlistGameItem(
    modifier: Modifier = Modifier,
    gameWishlist: GameWishlist,
    navigateToGameDetails: (Long) -> Unit = { },
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { gameWishlist.id?.let { navigateToGameDetails(it) } },
        elevation = 0.dp,
    ) {
        Column (
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            NetworkImage(
                url = gameWishlist.image.toString(),
                modifier = modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.FillBounds
            )
            Text(
                text = gameWishlist.status.toString(),
                style = MaterialTheme.typography.body2,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, start = 8.dp)
            )
            Text(
                text = gameWishlist.name.toString(),
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
            )
        }
    }
}