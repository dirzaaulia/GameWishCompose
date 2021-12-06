package com.dirzaaulia.gamewish.repository

import androidx.annotation.WorkerThread
import com.dirzaaulia.gamewish.base.NotFoundException
import com.dirzaaulia.gamewish.base.ResponseResult
import com.dirzaaulia.gamewish.base.executeWithResponse
import com.dirzaaulia.gamewish.data.model.tmdb.Movie
import com.dirzaaulia.gamewish.network.tmdb.TmdbService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TmdbRepository @Inject constructor(
    private val service: TmdbService
) {

    suspend fun searchMovie(searchQuery: String, page: Int) : ResponseResult<List<Movie>> {
        return withContext(Dispatchers.IO) {
            executeWithResponse {
                service.searchMovie(searchQuery = searchQuery, page = page)
                    .body()?.results ?: emptyList()
            }
        }
    }

    suspend fun searchTv(searchQuery: String, page: Int) : ResponseResult<List<Movie>> {
        return withContext(Dispatchers.IO) {
            executeWithResponse {
                service.searchTv(searchQuery = searchQuery, page = page)
                    .body()?.results ?: emptyList()
            }
        }
    }

    suspend fun getMovieRecommendations(
        movieId: Long,
        page: Int
    ): ResponseResult<List<Movie>> {
        return withContext(Dispatchers.IO) {
            executeWithResponse {
                service.getMovieRecommendations(movieId = movieId, page = page)
                    .body()?.results ?: emptyList()
            }
        }
    }

    @WorkerThread
    fun getMovieDetails(movieId: Long) = flow {
        try {
            service.getMovieDetail(movieId = movieId).body()?.let {
                emit(ResponseResult.Success(it))
            } ?: run {
                throw NotFoundException()
            }
        } catch (e: Exception) {
            emit(ResponseResult.Error(e))
        }
    }

    @WorkerThread
    fun getMovieImages(movieId: Long) = flow {
        try {
            service.getMovieImages(movieId = movieId).body()?.let {
                emit(ResponseResult.Success(it))
            } ?: run {
                throw NotFoundException()
            }
        } catch (e: Exception) {
            emit(ResponseResult.Error(e))
        }
    }
}