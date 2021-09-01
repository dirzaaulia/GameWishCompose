package com.dirzaaulia.gamewish.ui.theme

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Images that can vary by theme.
 */
@Immutable
data class Images(@DrawableRes val lockupLogo: Int)

internal val LocalImages = staticCompositionLocalOf<Images> {
    error("No LocalImages specified")
}
