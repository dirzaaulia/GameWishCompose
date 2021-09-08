package com.dirzaaulia.gamewish.di

import android.content.Context
import com.dirzaaulia.gamewish.network.cheapshark.CheapSharkService
import com.dirzaaulia.gamewish.network.myanimelist.MyAnimeListBaseUrlService
import com.dirzaaulia.gamewish.repository.CheapSharkRepository
import com.dirzaaulia.gamewish.repository.FirebaseRepository
import com.dirzaaulia.gamewish.repository.MyAnimeListRepository
import com.dirzaaulia.gamewish.repository.ProtoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(ViewModelComponent::class)
@Module
class RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideCheapSharkRepository(
        service: CheapSharkService
    ): CheapSharkRepository {
        return CheapSharkRepository(service)
    }

    @Provides
    fun provideFirebaseRepository() : FirebaseRepository {
        return FirebaseRepository()
    }

    @Provides
    @ViewModelScoped
    fun provideMyAnimeListRepository(
        baseUrlService: MyAnimeListBaseUrlService
    ) : MyAnimeListRepository {
        return MyAnimeListRepository(baseUrlService)
    }
}