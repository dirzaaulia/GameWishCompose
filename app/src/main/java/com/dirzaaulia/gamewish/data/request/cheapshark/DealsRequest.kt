package com.dirzaaulia.gamewish.data.request.cheapshark

import android.os.Parcelable
import com.dirzaaulia.gamewish.utils.CheapSharkConstant
import com.dirzaaulia.gamewish.utils.OtherConstant
import kotlinx.parcelize.Parcelize

@Parcelize
data class DealsRequest(
    val storeID: String?,
    val lowerPrice: Long?,
    val upperPrice: Long?,
    val title: String?,
    val aaa: Boolean?
) : Parcelable {
    companion object {
        fun default() = DealsRequest(
            storeID = CheapSharkConstant.CHEAPSHARK_DEFAULT_STORE_ID,
            lowerPrice = CheapSharkConstant.CHEAPSHARK_DEFAULT_LOWER_PRICE,
            upperPrice = CheapSharkConstant.CHEAPSHARK_DEFAULT_UPPER_PRICE,
            title = OtherConstant.EMPTY_STRING,
            aaa = false
        )
    }
}