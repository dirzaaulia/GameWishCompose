package com.dirzaaulia.gamewish.data.request.myanimelist

import android.os.Parcelable
import com.dirzaaulia.gamewish.utils.OtherConstant
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchGameRequest(
    val searchQuery: String = OtherConstant.EMPTY_STRING,
    val genreId: Int?,
    val publisherId: Int?,
    val platformId: Int?
) : Parcelable {
    companion object {
        fun default() = SearchGameRequest(
            searchQuery = OtherConstant.EMPTY_STRING,
            genreId = null,
            publisherId = null,
            platformId = null
        )
    }
}