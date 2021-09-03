package com.dirzaaulia.gamewish.data.model.rawg

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Store(
    val id: Int?,
    val name: String?,
    val slug: String?,
    val domain: String?,

    ) : Parcelable