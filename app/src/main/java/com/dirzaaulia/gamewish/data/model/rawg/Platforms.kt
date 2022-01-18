package com.dirzaaulia.gamewish.data.model.rawg

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Platforms(
    val platform: Platform?
) : Parcelable