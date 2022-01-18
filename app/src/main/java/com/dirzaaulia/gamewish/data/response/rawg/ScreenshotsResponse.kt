package com.dirzaaulia.gamewish.data.response.rawg

import androidx.annotation.Keep
import com.dirzaaulia.gamewish.data.model.rawg.Screenshots

@Keep
data class ScreenshotsResponse(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<Screenshots>?
)