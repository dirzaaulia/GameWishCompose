package com.dirzaaulia.gamewish.network.rawg

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.dirzaaulia.gamewish.data.model.rawg.GameDetails
import com.dirzaaulia.gamewish.data.response.rawg.*
import com.dirzaaulia.gamewish.utils.RawgConstant
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RawgService {

    @GET("games")
    suspend fun searchGames(
        @Query("key") key: String = RawgConstant.RAWG_KEY,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = 10,
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
        @Query("page_size") pageSize: Int = 10
    ): Response<PublishersResponse>

    @GET("platforms")
    suspend fun getPlatforms(
        @Query("key") key: String = RawgConstant.RAWG_KEY,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = 10
    ): Response<PlatformsResponse>

    companion object {
        fun create(context: Context): RawgService {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val chuckerLogger = ChuckerInterceptor.Builder(context)
                .collector(ChuckerCollector(context))
                .maxContentLength(250000L)
                .redactHeaders(emptySet())
                .alwaysReadResponseBody(false)
                .build()

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(chuckerLogger)
                .retryOnConnectionFailure(false)
                .build()

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .baseUrl(RawgConstant.RAWG_BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(RawgService::class.java)
        }
    }
}