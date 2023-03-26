package com.dirzaaulia.gamewish.data.model.tmdb

import android.os.Parcelable
import androidx.annotation.Keep
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Movie (
    @Json(name = "backdrop_path")
    val backdropPath: String? = OtherConstant.EMPTY_STRING,
    @Json(name = "genre_ids")
    val genreIdList: List<Long> = emptyList(),
    val id: Long = 0L,
    @Json(name = "original_title")
    val title: String = OtherConstant.EMPTY_STRING,
    @Json(name = "original_name")
    val name: String = OtherConstant.EMPTY_STRING,
    val overview: String = OtherConstant.EMPTY_STRING,
    @Json(name = "poster_path")
    val posterPath: String? = OtherConstant.EMPTY_STRING,
    @Json(name = "release_date")
    val releaseDate: String = OtherConstant.EMPTY_STRING,
) : Parcelable