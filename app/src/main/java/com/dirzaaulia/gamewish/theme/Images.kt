package com.dirzaaulia.gamewish.theme

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import com.dirzaaulia.gamewish.utils.OtherConstant

/**
 * Images that can vary by theme.
 */
@Immutable
data class Images(@DrawableRes val lockupLogo: Int)

internal val LocalImages = staticCompositionLocalOf<Images> {
    error(OtherConstant.LOCAL_IMAGES_ERROR)
}
