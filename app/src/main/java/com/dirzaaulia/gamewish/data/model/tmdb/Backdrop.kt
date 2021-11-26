package com.dirzaaulia.gamewish.data.model.tmdb

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Backdrop (
    @Json(name = "file_path")
    val path: String?
): Parcelable