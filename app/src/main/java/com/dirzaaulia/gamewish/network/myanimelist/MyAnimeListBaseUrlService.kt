package com.dirzaaulia.gamewish.network.myanimelist

import com.dirzaaulia.gamewish.data.response.myanimelist.MyAnimeListTokenResponse
import com.dirzaaulia.gamewish.utils.MyAnimeListConstant
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface MyAnimeListBaseUrlService {
    @FormUrlEncoded
    @POST("v1/oauth2/token")
    suspend fun getMyAnimeListToken(
        @Field("client_id") client_id: String,
        @Field("code") code: String,
        @Field("code_verifier") code_verifier: String,
        @Field("grant_type") grant_type: String
    ): Response<MyAnimeListTokenResponse>

    @FormUrlEncoded
    @POST("v1/oauth2/token")
    suspend fun getMyAnimeListRefreshToken(
        @Field("client_id") client_id: String,
        @Field("grant_type") grant_type: String,
        @Field("refresh_token") refresh_token: String
    ): Response<MyAnimeListTokenResponse>

    companion object {
        fun create(): MyAnimeListBaseUrlService {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build()

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .baseUrl(MyAnimeListConstant.MYANIMELIST_BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(MyAnimeListBaseUrlService::class.java)
        }
    }
}