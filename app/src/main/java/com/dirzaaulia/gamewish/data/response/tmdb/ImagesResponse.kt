package com.dirzaaulia.gamewish.data.response.tmdb

import android.os.Parcelable
import com.dirzaaulia.gamewish.data.model.tmdb.Image
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImagesResponse (
    @Json(name = "backdrops")
    val imageList: List<Image>?
) : Parcelable