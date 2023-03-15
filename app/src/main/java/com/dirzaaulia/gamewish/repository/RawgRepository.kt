package com.dirzaaulia.gamewish.repository

import androidx.annotation.WorkerThread
import com.dirzaaulia.gamewish.base.ResponseResult
import com.dirzaaulia.gamewish.base.executeWithData
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
        emit(ResponseResult.Loading)
        emit(
            executeWithResponse { service.getGameDetails(gameId) }
        )
    }

    @WorkerThread
    fun getGameDetailsScreenshots(gameId: Long) = flow {
        emit(ResponseResult.Loading)
        emit(
            executeWithResponse { service.getGameDetailsScreenshots(gameId) }
        )
    }

    suspend fun getGenres(page: Int) : ResponseResult<List<Genre>> {
        return withContext(Dispatchers.IO) {
            executeWithData {
                service.getGenres(page = page).body()?.results ?: emptyList()
            }
        }
    }

    suspend fun getPublishers(page: Int) : ResponseResult<List<Publisher>> {
        return withContext(Dispatchers.IO) {
            executeWithData {
                service.getPublishers(page = page)
                    .body()?.results ?: emptyList()
            }
        }
    }

    suspend fun getPlatforms(page: Int) : ResponseResult<List<Platform>> {
        return withContext(Dispatchers.IO) {
            executeWithData {
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
            executeWithData {
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