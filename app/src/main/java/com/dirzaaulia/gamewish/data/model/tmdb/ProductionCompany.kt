package com.dirzaaulia.gamewish.data.model.tmdb

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ProductionCompany (
    val id: Long?,
    val name: String?
) : Parcelable