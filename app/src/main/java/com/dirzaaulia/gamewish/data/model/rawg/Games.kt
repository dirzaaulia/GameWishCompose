package com.dirzaaulia.gamewish.data.model.rawg

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Games(
    val id: Long?,
    val name: String?,
    val released: String?,
    val metacritic: Int?,
    val platforms: List<Platforms>?,
    @Json(name = "background_image")
    val backgroundImage: String?,
    @Json(name = "esrb_rating")
    val esrbRating: EsrbRating?,
    @Json(name = "short_screenshots")
    val shortScreenshots: List<ShortScreenshots>?
) : Parcelable