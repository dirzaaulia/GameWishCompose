package com.dirzaaulia.gamewish.network.cheapshark

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.dirzaaulia.gamewish.data.model.cheapshark.Deals
import com.dirzaaulia.gamewish.data.model.rawg.Stores
import com.dirzaaulia.gamewish.utils.CheapSharkConstant
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface CheapSharkService {
    @GET("deals")
    suspend fun getGameDeals(
        @Query("storeID") storeID: String?,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @Query("lowerPrice") lowerPrice: Long?,
        @Query("upperPrice") upperPrice: Long?,
        @Query("title") title: String?,
        @Query("AAA") AAA: Boolean?
    ): Response<List<Deals>>

    @GET("stores")
    suspend fun getStoresList() : Response<List<Stores>>

    companion object {
        fun create(context: Context): CheapSharkService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            val chuckerLogger = ChuckerInterceptor.Builder(context)
                .collector(ChuckerCollector(context))
                .maxContentLength(250000L)
                .redactHeaders(emptySet())
                .alwaysReadResponseBody(false)
                .build()

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(chuckerLogger)
                .build()

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .baseUrl(CheapSharkConstant.CHEAPSHARK_BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(CheapSharkService::class.java)
        }
    }
}