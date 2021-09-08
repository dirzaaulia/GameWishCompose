package com.dirzaaulia.gamewish.data.request.cheapshark

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DealsRequest(
    val storeID: String?,
    val lowerPrice: Long?,
    val upperPrice: Long?,
    val title: String?,
    val AAA: Boolean?
) : Parcelable