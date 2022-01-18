package com.dirzaaulia.gamewish.data.model.myanimelist

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class MainPicture(
    val medium: String?,
    val large: String?
) : Parcelable