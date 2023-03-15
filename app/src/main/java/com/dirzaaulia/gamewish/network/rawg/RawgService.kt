package com.dirzaaulia.gamewish.network.rawg

import com.dirzaaulia.gamewish.data.model.rawg.GameDetails
import com.dirzaaulia.gamewish.data.response.rawg.*
import com.dirzaaulia.gamewish.utils.RawgConstant
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RawgService {

    @GET("games")
    suspend fun searchGames(
        @Query("key") key: String = RawgConstant.RAWG_KEY,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = RawgConstant.RAWG_PAGE_SIZE_FIFTY,
        @Query("search_precise") searchPrecise: Boolean = true,
        @Query("search") search: String?,
        @Query("genres") genres: Int?,
        @Query("publishers") publishers: Int?,
        @Query("platforms") platforms: Int?
    ): Response<SearchGamesResponse>

    @GET("games/{id}")
    suspend fun getGameDetails(
        @Path("id") id: Long,
        @Query("key") key: String = RawgConstant.RAWG_KEY
    ): Response<GameDetails>

    @GET("games/{id}/screenshots")
    suspend fun getGameDetailsScreenshots(
        @Path("id") id: Long,
        @Query("key") key: String = RawgConstant.RAWG_KEY
    ): Response<ScreenshotsResponse>

    @GET("genres")
    suspend fun getGenres(
        @Query("key") key: String = RawgConstant.RAWG_KEY,
        @Query("page") page: Int
    ): Response<GenresResponse>

    @GET("publishers")
    suspend fun getPublishers(
        @Query("key") key: String = RawgConstant.RAWG_KEY,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = RawgConstant.RAWG_PAGE_SIZE_TEN
    ): Response<PublishersResponse>

    @GET("platforms")
    suspend fun getPlatforms(
        @Query("key") key: String = RawgConstant.RAWG_KEY,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = RawgConstant.RAWG_PAGE_SIZE_TEN
    ): Response<PlatformsResponse>
}