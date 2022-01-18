package com.dirzaaulia.gamewish.data.response.rawg

import androidx.annotation.Keep
import com.dirzaaulia.gamewish.data.model.rawg.Games

@Keep
data class SearchGamesResponse(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<Games>?
)