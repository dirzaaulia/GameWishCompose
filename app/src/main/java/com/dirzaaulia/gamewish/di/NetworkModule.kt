package com.dirzaaulia.gamewish.di

import android.content.Context
import com.dirzaaulia.gamewish.network.cheapshark.CheapSharkService
import com.dirzaaulia.gamewish.network.myanimelist.MyAnimeListApiUrlService
import com.dirzaaulia.gamewish.network.myanimelist.MyAnimeListBaseUrlService
import com.dirzaaulia.gamewish.network.rawg.RawgService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideCheapSharkService(@ApplicationContext context: Context): CheapSharkService {
        return CheapSharkService.create(context)
    }

    @Singleton
    @Provides
    fun provideRawgServer(@ApplicationContext context: Context): RawgService {
        return RawgService.create(context)
    }

    @Singleton
    @Provides
    fun provideMyAnimeListBaseUrlService(): MyAnimeListBaseUrlService {
        return MyAnimeListBaseUrlService.create()
    }

    @Singleton
    @Provides
    fun provideMyAnimeListApiUrlService(@ApplicationContext context: Context): MyAnimeListApiUrlService {
        return MyAnimeListApiUrlService.create(context)
    }
//
//    @Provides
//    fun provideFirebaseRepository() : FirebaseRepository {
//        return FirebaseRepository()
//    }
}