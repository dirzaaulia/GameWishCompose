package com.dirzaaulia.gamewish.data.model.rawg

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class MetacriticPlatforms(
    val metascore: Int?,
    val url: String?,
    val platform: Platform?
) : Parcelable