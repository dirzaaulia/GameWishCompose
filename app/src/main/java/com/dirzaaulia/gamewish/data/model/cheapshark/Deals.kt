package com.dirzaaulia.gamewish.data.model.cheapshark

import androidx.annotation.Keep

@Keep
data class Deals(
    val internalName: String? = null,
    val title: String? = null,
    val dealID: String? = null,
    val storeID: String? = null,
    var storeName: String? = null,
    val gameID: String? = null,
    val salePrice: String? = null,
    val normalPrice: String? = null,
    val savings: String? = null,
    val steamAppID: String? = null,
    val thumb: String? = null
)