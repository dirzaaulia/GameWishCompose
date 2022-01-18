package com.dirzaaulia.gamewish.data.model.tmdb

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Image(
    val height: Int?,
    val width: Int?,
    @Json(name = "file_path")
    val filePath: String?
): Parcelable