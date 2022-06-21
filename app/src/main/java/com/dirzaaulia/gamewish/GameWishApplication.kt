package com.dirzaaulia.gamewish

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.dirzaaulia.gamewish.repository.ProtoRepository
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class GameWishApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this).build()
    }
}