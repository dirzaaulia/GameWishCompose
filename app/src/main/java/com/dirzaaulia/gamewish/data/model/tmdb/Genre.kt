package com.dirzaaulia.gamewish.data.model.tmdb

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Genre(
    val id: Long?,
    val name: String?
) : Parcelable