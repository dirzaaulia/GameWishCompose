package com.dirzaaulia.gamewish.data.model.tmdb

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDetail (
    val id: Long?,
    val budget: Long?,
    val genres: List<Genre>?,
    val homepage: String?,
    @Json(name = "original_title")
    val title: String?,
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
    val posterPath: String?
) : Parcelable