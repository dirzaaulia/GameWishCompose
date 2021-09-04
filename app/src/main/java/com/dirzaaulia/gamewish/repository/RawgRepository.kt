package com.dirzaaulia.gamewish.repository

import androidx.annotation.WorkerThread
import com.dirzaaulia.gamewish.base.NotFoundException
import com.dirzaaulia.gamewish.base.ResponseResult
import com.dirzaaulia.gamewish.data.model.rawg.GameDetails
import com.dirzaaulia.gamewish.data.model.rawg.Screenshots
import com.dirzaaulia.gamewish.network.rawg.RawgService
import com.dirzaaulia.gamewish.utils.RawgConstant
import com.dirzaaulia.gamewish.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class RawgRepository @Inject constructor(
    private val service: RawgService
) {
//    fun refreshSearchGames(search: String?, genres: Int?, publisher: Int?, platforms: Int?)
//            : Flow<PagingData<Games>> {
//        return Pager(
//            config = PagingConfig(enablePlaceholders = false, pageSize = GAMES_PAGE_SIZE),
//            pagingSourceFactory = { SearchGamesPagingSource(service, search, genres, publisher, platforms) }
//        ).flow
//    }

    @WorkerThread
    fun getGameDetails(gameId: Long) = flow {
        try {
            service.getGameDetails(gameId, RawgConstant.RAWG_KEY).body()?.let {
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
            service.getGameDetailsScreenshots(gameId, RawgConstant.RAWG_KEY).body()?.let {
                emit(ResponseResult.Success(it))
            } ?: run {
                throw NotFoundException()
            }
        } catch (e: Exception) {
            emit(ResponseResult.Error(e))
        }
    }

//    fun getGenres() : Flow<PagingData<Genre>> {
//        return Pager(
//            config = PagingConfig(enablePlaceholders = false, pageSize = GAMES_PAGE_SIZE),
//            pagingSourceFactory = { GenresPagingSource(service) }
//        ).flow
//    }
//
//    fun getPublishers() : Flow<PagingData<Publisher>> {
//        return Pager(
//            config = PagingConfig(enablePlaceholders = false, pageSize = GAMES_PAGE_SIZE),
//            pagingSourceFactory = { PublishersPagingSource(service) }
//        ).flow
//    }
//
//    fun getPlatforms() : Flow<PagingData<Platform>> {
//        return Pager(
//            config = PagingConfig(enablePlaceholders = false, pageSize = GAMES_PAGE_SIZE),
//            pagingSourceFactory = { PlatformsPagingSource(service) }
//        ).flow
//    }
}