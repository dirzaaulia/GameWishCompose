package com.dirzaaulia.gamewish.repository

import com.dirzaaulia.gamewish.data.model.rawg.GameDetails
import com.dirzaaulia.gamewish.data.model.rawg.Screenshots
import com.dirzaaulia.gamewish.network.rawg.RawgService
import com.dirzaaulia.gamewish.utils.RawgConstant
import com.dirzaaulia.gamewish.utils.Resource
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


    suspend fun getGameDetails(gameId: Long): Resource<GameDetails> {
        val response = try {
            service.getGameDetails(gameId, RawgConstant.RAWG_KEY).body()
        } catch (e: Exception) {
            return Resource.Error("An unknown error occured!")
        }
        return Resource.Success(response!!)
    }

    suspend fun getGameDetailsScreenshots(gameId: Long): Resource<List<Screenshots>> {
        val response = try {
            service.getGameDetailsScreenshots(gameId, RawgConstant.RAWG_KEY).body()?.results
        } catch (e: Exception) {
            return Resource.Error("An unknown error occured!")
        }
        return Resource.Success(response!!)
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