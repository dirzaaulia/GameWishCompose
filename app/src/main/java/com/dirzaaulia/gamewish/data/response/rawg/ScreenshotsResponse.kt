package com.dirzaaulia.gamewish.data.response.rawg

import androidx.compose.runtime.Immutable
import com.dirzaaulia.gamewish.data.model.rawg.Screenshots

@Immutable
data class ScreenshotsResponse(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<Screenshots>?
)