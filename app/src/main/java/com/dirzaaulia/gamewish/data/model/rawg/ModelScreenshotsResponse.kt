package com.dirzaaulia.gamewish.data.model.rawg

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScreenshotsResponse(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<Screenshots>?
) : Parcelable