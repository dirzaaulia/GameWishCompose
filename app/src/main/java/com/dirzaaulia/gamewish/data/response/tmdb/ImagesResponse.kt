package com.dirzaaulia.gamewish.data.response.tmdb

import android.os.Parcelable
import androidx.annotation.Keep
import com.dirzaaulia.gamewish.data.model.tmdb.Image
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ImagesResponse (
    @Json(name = "backdrops")
    val imageList: List<Image>?
) : Parcelable