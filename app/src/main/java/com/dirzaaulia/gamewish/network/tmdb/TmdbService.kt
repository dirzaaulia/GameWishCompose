package com.dirzaaulia.gamewish.network.tmdb

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.dirzaaulia.gamewish.data.model.tmdb.MovieDetail
import com.dirzaaulia.gamewish.data.response.tmdb.ImagesResponse
import com.dirzaaulia.gamewish.data.response.tmdb.SearchMovieResponse
import com.dirzaaulia.gamewish.utils.TmdbConstant
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

interface TmdbService {

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("api_key") apiKey : String = TmdbConstant.TMDB_KEY,
        @Query("page") page : Int,
        @Query("query") searchQuery : String,
        @Query("include_adult") includeAdult : Boolean = false
    ): Response<SearchMovieResponse>

    @GET("search/tv")
    suspend fun searchTv(
        @Query("api_key") apiKey : String = TmdbConstant.TMDB_KEY,
        @Query("page") page : Int,
        @Query("query") searchQuery : String,
        @Query("include_adult") includeAdult : Boolean = false
    ): Response<SearchMovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Long,
        @Query("api_key") apiKey : String = TmdbConstant.TMDB_KEY
    ): Response<MovieDetail>

    @GET("movie/{movie_id}/images")
    suspend fun getMovieImages(
        @Path("movie_id") movieId: Long,
        @Query("api_key") apiKey : String = TmdbConstant.TMDB_KEY
    ): Response<ImagesResponse>

    @GET("movie/{movie_id}/recommendations")
    suspend fun getMovieRecommendations(
        @Path("movie_id") movieId: Long,
        @Query("api_key") apiKey: String = TmdbConstant.TMDB_KEY,
        @Query("page") page : Int,
        @Query("include_adult") includeAdult : Boolean = false
    ): Response<SearchMovieResponse>

    @GET("tv/{tv_id}")
    suspend fun getTVDetail(
        @Path("tv_id") tvId: Long,
        @Query("api_key") apiKey : String = TmdbConstant.TMDB_KEY
    ): Response<MovieDetail>

    @GET("tv/{tv_id}/images")
    suspend fun getTVImages(
        @Path("tv_id") tvId: Long,
        @Query("api_key") apiKey : String = TmdbConstant.TMDB_KEY
    ): Response<ImagesResponse>

    @GET("tv/{tv_id}/recommendations")
    suspend fun getTVRecommendations(
        @Path("tv_id") tvId: Long,
        @Query("api_key") apiKey: String = TmdbConstant.TMDB_KEY,
        @Query("page") page : Int,
        @Query("include_adult") includeAdult : Boolean = false
    ): Response<SearchMovieResponse>

    companion object {
        fun create(context: Context): TmdbService {
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
                .baseUrl(TmdbConstant.TMDB_BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(TmdbService::class.java)
        }
    }
}