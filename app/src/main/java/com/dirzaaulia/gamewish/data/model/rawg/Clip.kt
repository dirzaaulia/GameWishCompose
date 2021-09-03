package com.dirzaaulia.gamewish.data.model.rawg

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Clip(
    val video: String?,
    val preview: String?
) : Parcelable