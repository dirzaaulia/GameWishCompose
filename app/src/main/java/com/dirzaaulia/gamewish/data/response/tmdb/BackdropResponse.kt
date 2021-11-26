package com.dirzaaulia.gamewish.data.response.tmdb

import android.os.Parcelable
import com.dirzaaulia.gamewish.data.model.tmdb.Backdrop
import kotlinx.parcelize.Parcelize

@Parcelize
class BackdropResponse (
    val backdrops: List<Backdrop>?
) : Parcelable