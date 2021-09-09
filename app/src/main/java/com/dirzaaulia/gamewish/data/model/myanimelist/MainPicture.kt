package com.dirzaaulia.gamewish.data.model.myanimelist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainPicture(
    val medium: String?,
    val large: String?
) : Parcelable