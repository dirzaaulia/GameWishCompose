package com.dirzaaulia.gamewish.data.model.tmdb

import android.os.Parcelable
import androidx.annotation.Keep
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class MovieDetail (
    val id: Long = OtherConstant.ZERO_LONG,
    val budget: Long?,
    val genres: List<Genre>?,
    val homepage: String?,
    @Json(name = "original_title")
    val title: String = OtherConstant.EMPTY_STRING,
    @Json(name = "original_name")
    val name: String = OtherConstant.EMPTY_STRING,
    val tagline: String?,
    val overview: String?,
    @Json(name = "production_companies")
    val productionCompanies: List<ProductionCompany>?,
    @Json(name = "release_date")
    val releaseDate: String?,
    val revenue: Long?,
    val runtime: Long?,
    val status: String?,
    @Json(name = "vote_average")
    val voteAverage: Double?,
    val popularity: Double?,
    @Json(name = "poster_path")
    val posterPath: String = OtherConstant.EMPTY_STRING,
    @Json(name = "episode_run_time")
    val episodeRunTime: List<Int>?,
    @Json(name = "number_of_episodes")
    val numberOfEpisodes: Int?,
    @Json(name = "number_of_seasons")
    val numberOfSeasons: Int?,
) : Parcelable