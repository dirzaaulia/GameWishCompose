package com.dirzaaulia.gamewish.data.model.rawg

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EsrbRating(
    val id: Int?,
    val name: String?,
    val slug: String?,
    val name_en: String?
) : Parcelable