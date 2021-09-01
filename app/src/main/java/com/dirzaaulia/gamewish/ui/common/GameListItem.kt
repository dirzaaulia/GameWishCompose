package com.dirzaaulia.gamewish.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dirzaaulia.gamewish.data.model.Wishlist
import com.dirzaaulia.gamewish.ui.theme.GameWishTheme
import com.dirzaaulia.gamewish.utils.NetworkImage

@Composable
fun GameListItem(
    wishlist: Wishlist,
    onClick: () -> Unit,
    modifier : Modifier = Modifier,
    elevation : Dp = GameWishTheme.elevations.card,
    titleStyle: TextStyle = MaterialTheme.typography.h5
) {
    Surface(
        elevation = elevation,
        modifier = modifier,
    ) {
        Column(modifier = Modifier.clickable(onClick = onClick)) {
            wishlist.image?.let { imageUrl ->
                NetworkImage(
                    url = imageUrl,
                    contentDescription = null,
                    modifier = modifier.fillMaxWidth().height(200.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
            wishlist.name?.let { name ->
                Text(
                    text = name,
                    style = titleStyle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                )
            }
        }
    }
}

@Preview(name = "Game List Item")
@Composable
private fun GameListItemPreview() {
    GameListItem(
        wishlist = Wishlist(
            3328,
            "The Witcher 3: Wild Hult",
            "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg"),
        onClick = {}
    )
}