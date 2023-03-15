package com.dirzaaulia.gamewish.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.dirzaaulia.gamewish.network.NetworkConfig
import com.dirzaaulia.gamewish.network.cheapshark.CheapSharkService
import com.dirzaaulia.gamewish.network.myanimelist.MyAnimeListApiUrlService
import com.dirzaaulia.gamewish.network.myanimelist.MyAnimeListBaseUrlService
import com.dirzaaulia.gamewish.network.rawg.RawgService
import com.dirzaaulia.gamewish.network.tmdb.TmdbService
import com.dirzaaulia.gamewish.utils.CheapSharkConstant
import com.dirzaaulia.gamewish.utils.MyAnimeListConstant
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.RawgConstant
import com.dirzaaulia.gamewish.utils.TmdbConstant
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun provideChucker(@ApplicationContext context: Context): ChuckerInterceptor =
        ChuckerInterceptor.Builder(context)
            .collector(ChuckerCollector(context))
            .maxContentLength(250000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(false)
            .build()

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        chucker: ChuckerInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(chucker)
        .connectTimeout(OtherConstant.THIRTY_LONG, TimeUnit.SECONDS)
        .readTimeout(OtherConstant.THIRTY_LONG, TimeUnit.SECONDS)
        .writeTimeout(OtherConstant.THIRTY_LONG, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Singleton
    @Provides
    fun provideCheapSharkService(
        client: OkHttpClient,
        moshi: Moshi,
    ): CheapSharkService {
        return NetworkConfig(
            client = client,
            moshi = moshi,
            baseUrl = CheapSharkConstant.CHEAPSHARK_BASE_URL
        ).create(CheapSharkService::class.java)
    }

    @Singleton
    @Provides
    fun provideRawgService(
        client: OkHttpClient,
        moshi: Moshi,
    ): RawgService {
        return NetworkConfig(
            client = client,
            moshi = moshi,
            baseUrl = RawgConstant.RAWG_BASE_URL
        ).create(RawgService::class.java)
    }

    @Singleton
    @Provides
    fun provideMyAnimeListBaseUrlService(
        client: OkHttpClient,
        moshi: Moshi
    ): MyAnimeListBaseUrlService {
        return NetworkConfig(
            client = client,
            moshi = moshi,
            baseUrl = MyAnimeListConstant.MYANIMELIST_BASE_URL
        ).create(MyAnimeListBaseUrlService::class.java)
    }

    @Singleton
    @Provides
    fun provideMyAnimeListApiUrlService(
        client: OkHttpClient,
        moshi: Moshi
    ): MyAnimeListApiUrlService {
        return NetworkConfig(
            client = client,
            moshi = moshi,
            baseUrl = MyAnimeListConstant.MYANIMELIST_API_URL
        ).create(MyAnimeListApiUrlService::class.java)
    }

    @Singleton
    @Provides
    fun provideTmdbService(
        client: OkHttpClient,
        moshi: Moshi
    ): TmdbService {
        return NetworkConfig(
            client = client,
            moshi = moshi,
            baseUrl = TmdbConstant.TMDB_BASE_URL
        ).create(TmdbService::class.java)
    }
}