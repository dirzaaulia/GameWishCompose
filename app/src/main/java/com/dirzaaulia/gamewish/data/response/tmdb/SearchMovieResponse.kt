package com.dirzaaulia.gamewish.data.response.tmdb

import com.dirzaaulia.gamewish.data.model.tmdb.Movie

data class SearchMovieResponse (
    val page: Int?,
    val results: List<Movie>?,
)