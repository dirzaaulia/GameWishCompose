package com.dirzaaulia.gamewish.extension

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha

fun Modifier.visible(visibility: Boolean): Modifier = this.then(alpha(if (visibility) 1f else 0f))
