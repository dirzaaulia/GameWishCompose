package com.dirzaaulia.gamewish.data.model.myanimelist

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
class AlternativeTitles(
    val en: String?,
    val ja: String?
) : Parcelable