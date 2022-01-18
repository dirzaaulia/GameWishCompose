package com.dirzaaulia.gamewish.data.model.tmdb

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Movie (
    @Json(name = "backdrop_path")
    val backdropPath: String?,
    @Json(name = "genre_ids")
    val genreIdList: List<Long>?,
    val id: Long?,
    @Json(name = "original_title")
    val title: String?,
    @Json(name = "original_name")
    val name: String?,
    val overview: String?,
    @Json(name = "poster_path")
    val posterPath: String?,
    @Json(name = "release_date")
    val releaseDate: String?,
) : Parcelable