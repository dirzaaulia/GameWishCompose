package com.dirzaaulia.gamewish.data.response.rawg

import com.dirzaaulia.gamewish.data.model.rawg.Platform

data class PlatformsResponse(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<Platform>?
)