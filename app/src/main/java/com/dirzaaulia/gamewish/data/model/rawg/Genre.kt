package com.dirzaaulia.gamewish.data.model.rawg

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Genre(
    val id: Int?,
    val name: String?,
    val slug: String?,
    @Json(name = "games_count")
    val gamesCount: Int?,
    @Json(name = "image_background")
    val imageBackground: String?,
    val games: List<Games>?
) : Parcelable