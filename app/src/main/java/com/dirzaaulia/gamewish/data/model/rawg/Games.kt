package com.dirzaaulia.gamewish.data.model.rawg

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Games(
    val id: Long? = null,
    val name: String? = null,
    val released: String? = null,
    val metacritic: Int? = null,
    val platforms: List<Platforms>? = null,
    @Json(name = "background_image")
    val backgroundImage: String? = null,
    @Json(name = "esrb_rating")
    val esrbRating: EsrbRating? = null,
    @Json(name = "short_screenshots")
    val shortScreenshots: List<ShortScreenshots>? = null
) : Parcelable