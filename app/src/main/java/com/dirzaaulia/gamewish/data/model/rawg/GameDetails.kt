package com.dirzaaulia.gamewish.data.model.rawg

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameDetails(
    val id: Int?,
    val slug: String?,
    val name: String?,
    @Json(name = "name_original")
    val nameOriginal: String?,
    val description: String?,
    @Json(name = "description_raw")
    val descriptionRaw: String?,
    val metacritic: Int?,
    @Json(name = "metacritic_platforms")
    val metacriticPlatforms: List<MetacriticPlatforms>?,
    @Json(name = "metacritic_url")
    val metacriticUrl: String?,
    val released: String?,
    @Json(name = "background_image")
    val backgroundImage: String?,
    @Json(name = "background_image_additional")
    val backgroundImageAdditional: String?,
    val website: String?,
    @Json(name = "reddit_url")
    val redditUrl: String?,
    val platforms: List<Platforms>?,
    val stores: List<Stores>?,
    val developers: List<Developer>?,
    val publishers: List<Publisher>?,
    @Json(name = "esrb_rating")
    val esrbRating: EsrbRating?,
    val clip: Clip?
) : Parcelable