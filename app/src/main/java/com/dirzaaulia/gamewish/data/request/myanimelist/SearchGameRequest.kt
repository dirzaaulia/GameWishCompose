package com.dirzaaulia.gamewish.data.request.myanimelist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchGameRequest(
    val searchQuery: String = "",
    val genreId: Int?,
    val publisherId: Int?,
    val platformId: Int?
) : Parcelable