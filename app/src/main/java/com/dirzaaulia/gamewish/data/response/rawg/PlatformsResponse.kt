package com.dirzaaulia.gamewish.data.response.rawg

import androidx.annotation.Keep
import com.dirzaaulia.gamewish.data.model.rawg.Platform

@Keep
data class PlatformsResponse(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<Platform>?
)