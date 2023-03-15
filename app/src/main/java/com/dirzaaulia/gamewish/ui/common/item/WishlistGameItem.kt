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
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.dirzaaulia.gamewish.data.model.wishlist.GameWishlist
import com.dirzaaulia.gamewish.utils.NetworkImage
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder

@Composable
fun WishlistGameItem(
    modifier: Modifier = Modifier,
    gameWishlist: GameWishlist,
    loadState: CombinedLoadStates,
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
                contentDescription = null,
                modifier = modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .placeholder(
                        visible = loadState.refresh is LoadState.Loading,
                        highlight = PlaceholderHighlight.shimmer(),
                        color = MaterialTheme.colors.secondary,
                        shape = MaterialTheme.shapes.small
                    ),
                contentScale = ContentScale.FillBounds
            )
            Text(
                text = gameWishlist.status.toString(),
                style = MaterialTheme.typography.body2,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, start = 8.dp)
                    .placeholder(
                        visible = loadState.refresh is LoadState.Loading,
                        highlight = PlaceholderHighlight.shimmer(),
                        color = MaterialTheme.colors.secondary,
                        shape = MaterialTheme.shapes.small
                    )
            )
            Text(
                text = gameWishlist.name.toString(),
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
                    .placeholder(
                        visible = loadState.refresh is LoadState.Loading,
                        highlight = PlaceholderHighlight.shimmer(),
                        color = MaterialTheme.colors.secondary,
                        shape = MaterialTheme.shapes.small
                    )

            )
        }
    }
}