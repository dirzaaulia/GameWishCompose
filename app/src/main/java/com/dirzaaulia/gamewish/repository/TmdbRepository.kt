package com.dirzaaulia.gamewish.repository

import androidx.annotation.WorkerThread
import com.dirzaaulia.gamewish.base.ResponseResult
import com.dirzaaulia.gamewish.base.executeWithData
import com.dirzaaulia.gamewish.base.executeWithResponse
import com.dirzaaulia.gamewish.data.model.tmdb.Movie
import com.dirzaaulia.gamewish.network.tmdb.TmdbService
import com.dirzaaulia.gamewish.utils.replaceIfNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TmdbRepository @Inject constructor(
    private val service: TmdbService
) {

    suspend fun searchMovie(searchQuery: String, page: Int) : ResponseResult<List<Movie>> {
        return withContext(Dispatchers.IO) {
            executeWithData {
                service.searchMovie(searchQuery = searchQuery, page = page)
                    .body()?.results.replaceIfNull()
            }
        }
    }

    suspend fun searchTv(searchQuery: String, page: Int) : ResponseResult<List<Movie>> {
        return withContext(Dispatchers.IO) {
            executeWithData {
                service.searchTv(searchQuery = searchQuery, page = page)
                    .body()?.results.replaceIfNull()
            }
        }
    }

    suspend fun getMovieRecommendations(
        movieId: Long,
        page: Int
    ): ResponseResult<List<Movie>> {
        return withContext(Dispatchers.IO) {
            executeWithData {
                service.getMovieRecommendations(movieId = movieId, page = page)
                    .body()?.results.replaceIfNull()
            }
        }
    }

    suspend fun getTVRecommendations(
        tvId: Long,
        page: Int
    ): ResponseResult<List<Movie>> {
        return withContext(Dispatchers.IO) {
            executeWithData {
                service.getTVRecommendations(tvId = tvId, page = page)
                    .body()?.results.replaceIfNull()
            }
        }
    }

    @WorkerThread
    fun getMovieDetails(movieId: Long) = flow {
        emit(ResponseResult.Loading)
        emit(
            executeWithResponse {
                service.getMovieDetail(movieId = movieId)
            }
        )
    }

    @WorkerThread
    fun getTVDetails(tvId: Long) = flow {
        emit(ResponseResult.Loading)
        emit(
            executeWithResponse {
                service.getTVDetail(tvId = tvId)
            }
        )
    }

    @WorkerThread
    fun getMovieImages(movieId: Long) = flow {
        emit(ResponseResult.Loading)
        emit(
            executeWithResponse {
                service.getMovieImages(movieId = movieId)
            }
        )
    }

    @WorkerThread
    fun getTVImages(tvId: Long) = flow {
        emit(ResponseResult.Loading)
        emit(
            executeWithResponse {
                service.getTVImages(tvId = tvId)
            }
        )
    }
}