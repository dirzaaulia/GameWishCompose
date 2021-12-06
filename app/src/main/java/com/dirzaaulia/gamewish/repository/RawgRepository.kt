package com.dirzaaulia.gamewish.repository

import androidx.annotation.WorkerThread
import com.dirzaaulia.gamewish.base.NotFoundException
import com.dirzaaulia.gamewish.base.ResponseResult
import com.dirzaaulia.gamewish.base.executeWithResponse
import com.dirzaaulia.gamewish.data.model.rawg.Games
import com.dirzaaulia.gamewish.data.model.rawg.Genre
import com.dirzaaulia.gamewish.data.model.rawg.Platform
import com.dirzaaulia.gamewish.data.model.rawg.Publisher
import com.dirzaaulia.gamewish.network.rawg.RawgService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class RawgRepository @Inject constructor(
    private val service: RawgService
) {

    @WorkerThread
    fun getGameDetails(gameId: Long) = flow {
        try {
            service.getGameDetails(gameId).body()?.let {
                emit(ResponseResult.Success(it))
            } ?: run {
                throw NotFoundException()
            }
        } catch (e: Exception) {
            emit(ResponseResult.Error(e))
        }
    }

    @WorkerThread
    fun getGameDetailsScreenshots(gameId: Long) = flow {
        try {
            service.getGameDetailsScreenshots(gameId).body()?.let {
                emit(ResponseResult.Success(it))
            } ?: run {
                throw NotFoundException()
            }
        } catch (e: Exception) {
            emit(ResponseResult.Error(e))
        }
    }

    suspend fun getGenres(page: Int) : ResponseResult<List<Genre>> {
        return withContext(Dispatchers.IO) {
            executeWithResponse {
                service.getGenres(page = page).body()?.results ?: emptyList()
            }
        }
    }

    suspend fun getPublishers(page: Int) : ResponseResult<List<Publisher>> {
        return withContext(Dispatchers.IO) {
            executeWithResponse {
                service.getPublishers(page = page)
                    .body()?.results ?: emptyList()
            }
        }
    }

    suspend fun getPlatforms(page: Int) : ResponseResult<List<Platform>> {
        return withContext(Dispatchers.IO) {
            executeWithResponse {
                service.getPlatforms(page = page)
                    .body()?.results ?: emptyList()
            }
        }
    }

    suspend fun searchGames(
        page: Int,
        query: String,
        genreId: Int?,
        publisherId: Int?,
        platformId: Int?
    ) : ResponseResult<List<Games>> {
        return withContext(Dispatchers.IO) {
            executeWithResponse {
                service.searchGames(
                    page = page,
                    search = query,
                    genres = genreId,
                    publishers = publisherId,
                    platforms = platformId
                ).body()?.results ?: emptyList()
            }
        }
    }
}