package com.dirzaaulia.gamewish.data.model.tmdb

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
class ProductionCompany (
    val id: Long?,
    val name: String?
) : Parcelable