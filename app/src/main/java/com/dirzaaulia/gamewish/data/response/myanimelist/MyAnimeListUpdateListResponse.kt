package com.dirzaaulia.gamewish.data.response.myanimelist

import com.squareup.moshi.Json

data class MyAnimeListUpdateListResponse(
    val status: String?,
    val score: Int?,
    @Json(name = "num_watched_episodes")
    val episodes: Int?,
    @Json(name = "is_rewatching")
    val isRewatching: Boolean?
)