package com.dirzaaulia.gamewish.data.model.rawg

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Stores(
    val storeID: String?,
    val storeName: String?,
    val id: Int?,
    val url: String?,
    val store: Store?
) : Parcelable