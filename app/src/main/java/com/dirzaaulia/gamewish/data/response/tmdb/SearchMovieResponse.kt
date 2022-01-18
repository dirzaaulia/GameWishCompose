package com.dirzaaulia.gamewish.data.response.tmdb

import androidx.annotation.Keep
import com.dirzaaulia.gamewish.data.model.tmdb.Movie

@Keep
data class SearchMovieResponse (
    val page: Int?,
    val results: List<Movie>?,
)