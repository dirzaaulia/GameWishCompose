package com.dirzaaulia.gamewish.ui.common

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource

@Composable
fun ActionWishlistFab(
    icon: ImageVector,
    action: () -> Unit
) {
    FloatingActionButton(
        onClick = action ,
    ) {
        Icon(imageVector = icon, contentDescription = null)
    }
}