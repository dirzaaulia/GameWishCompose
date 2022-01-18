package com.dirzaaulia.gamewish.data.model.rawg

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Developer(
    val id: Int?,
    val name: String?,
    val slug: String?,

    ) : Parcelable