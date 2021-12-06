package com.dirzaaulia.gamewish.data.model.tmdb

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    val height: Int?,
    val width: Int?,
    @Json(name = "file_path")
    val filePath: String?
): Parcelable