package com.dirzaaulia.gamewish.network.myanimelist

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.dirzaaulia.gamewish.base.ResponseResult
import com.dirzaaulia.gamewish.data.model.myanimelist.Details
import com.dirzaaulia.gamewish.data.model.myanimelist.ListStatus
import com.dirzaaulia.gamewish.data.model.myanimelist.User
import com.dirzaaulia.gamewish.data.response.myanimelist.MyAnimeListSearchResponse
import com.dirzaaulia.gamewish.utils.MyAnimeListConstant
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface MyAnimeListApiUrlService {

    @GET("v2/users/{user_name}")
    suspend fun getMyAnimeListUser(
        @Header("Authorization") authorization: String,
        @Path("user_name") username: String = "@me",
        @Query("fields") fields: String = "anime_statistics"
    ): Response<User>

    @GET("v2/anime")
    suspend fun searchMyAnimeListAnime(
        @Header("Authorization") authorization: String,
        @Query("q") value: String,
        @Query("offset") offset: Int
    ): ResponseResult<MyAnimeListSearchResponse>

    @GET("v2/manga")
    suspend fun searchMyAnimeListManga(
        @Header("Authorization") authorization: String,
        @Query("q") value: String,
        @Query("offset") offset: Int
    ): MyAnimeListSearchResponse

    @GET("v2/anime/season/{year}/{season}")
    suspend fun getMyAnimeListSeasonalAnime(
        @Header("Authorization") authorization: String,
        @Path("year") year: String,
        @Path("season") season: String,
        @Query("offset") offset: Int
    ): MyAnimeListSearchResponse

    @GET("v2/anime/{anime_id}")
    suspend fun getMyAnimeListAnimeDetails(
        @Header("Authorization") authorization: String,
        @Path("anime_id") animeId: String,
        @Query("fields") fields: String,
        @Query("offset") offset: Int
    ): Details

    @GET("v2/manga/{manga_id}")
    suspend fun getMyAnimeListMangaDetails(
        @Header("Authorization") authorization: String,
        @Path("manga_id") animeId: String,
        @Query("fields") fields: String,
        @Query("offset") offset: Int
    ): Details

    @GET("v2/users/{user_name}/animelist")
    suspend fun getMyAnimeListAnimeList(
        @Header("Authorization") authorization: String,
        @Path("user_name") username: String = "@me",
        @Query("fields") fields: String = "list_status",
        @Query("status") status: String?,
        @Query("offset") offset: Int?
    ): Response<MyAnimeListSearchResponse>

    @GET("v2/users/{user_name}/mangalist")
    suspend fun getMyAnimeListMangaList(
        @Header("Authorization") authorization: String,
        @Path("user_name") username: String,
        @Query("fields") fields: String,
        @Query("status") status: String?,
        @Query("offset") offset: Int
    ): MyAnimeListSearchResponse

    @FormUrlEncoded
    @PUT("v2/anime/{anime_id}/my_list_status")
    suspend fun updateMyAnimeListAnimeList(
        @Header("Authorization") authorization: String,
        @Path("anime_id") id: Int,
        @Field("status") status: String,
        @Field("is_rewatching") isRewatching: Boolean?,
        @Field("score") score: Int?,
        @Field("num_watched_episodes") episode: Int?
    ): ListStatus

    @DELETE("v2/anime/{anime_id}/my_list_status")
    suspend fun deleteMyAnimeListAnimeList(
        @Header("Authorization") authorization: String,
        @Path("anime_id") id: Int
    )

    @FormUrlEncoded
    @PUT("v2/manga/{manga_id}/my_list_status")
    suspend fun updateMyAnimeListMangaList(
        @Header("Authorization") authorization: String,
        @Path("manga_id") id: Int,
        @Field("status") status: String,
        @Field("is_rereading") isRereading: Boolean?,
        @Field("score") score: Int?,
        @Field("num_chapters_read") episode: Int?
    ): ListStatus

    @DELETE("v2/manga/{manga_id}/my_list_status")
    suspend fun deleteMyAnimeListMangaList(
        @Header("Authorization") authorization: String,
        @Path("manga_id") id: Int
    )

    companion object {
        fun create(context: Context): MyAnimeListApiUrlService {
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
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build()

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .baseUrl(MyAnimeListConstant.MYANIMELIST_API_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(MyAnimeListApiUrlService::class.java)
        }
    }
}