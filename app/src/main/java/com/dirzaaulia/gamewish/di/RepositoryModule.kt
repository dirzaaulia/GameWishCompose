package com.dirzaaulia.gamewish.di

import com.dirzaaulia.gamewish.network.cheapshark.CheapSharkService
import com.dirzaaulia.gamewish.network.myanimelist.MyAnimeListApiUrlService
import com.dirzaaulia.gamewish.network.myanimelist.MyAnimeListBaseUrlService
import com.dirzaaulia.gamewish.network.rawg.RawgService
import com.dirzaaulia.gamewish.network.tmdb.TmdbService
import com.dirzaaulia.gamewish.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
class RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideRawgRepository(
        service: RawgService
    ): RawgRepository {
        return RawgRepository(service)
    }

    @Provides
    @ViewModelScoped
    fun provideCheapSharkRepository(
        service: CheapSharkService
    ): CheapSharkRepository {
        return CheapSharkRepository(service)
    }

    @Provides
    fun provideFirebaseRepository(): FirebaseRepository {
        return FirebaseRepository()
    }

    @Provides
    @ViewModelScoped
    fun provideMyAnimeListRepository(
        baseUrlService: MyAnimeListBaseUrlService,
        apiUrlService: MyAnimeListApiUrlService
    ): MyAnimeListRepository {
        return MyAnimeListRepository(baseUrlService, apiUrlService)
    }

    @Provides
    @ViewModelScoped
    fun provideTmdbRepository(
        service: TmdbService
    ): TmdbRepository {
        return TmdbRepository(service)
    }
}