package com.dirzaaulia.gamewish.data.model.rawg

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Developer(
    val id: Int?,
    val name: String?,
    val slug: String?,

    ) : Parcelable