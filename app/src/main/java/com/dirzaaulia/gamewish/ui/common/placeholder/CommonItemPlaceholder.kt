package com.dirzaaulia.gamewish.ui.common.placeholder

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder

@Composable
fun CommonItemPlaceholder(
    modifier: Modifier = Modifier,
    height: Dp,
    shape: Shape
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .padding(vertical = 4.dp)
            .placeholder(
                visible = true,
                color = MaterialTheme.colors.primary.copy(alpha = 0.2f),
                shape = shape,
                highlight = PlaceholderHighlight.shimmer()
            )
    ) { }
}