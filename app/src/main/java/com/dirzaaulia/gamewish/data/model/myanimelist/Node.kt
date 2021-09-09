package com.dirzaaulia.gamewish.data.model.myanimelist

import android.os.Parcelable
import com.dirzaaulia.gamewish.data.model.myanimelist.MainPicture
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Node(
    val id: Int?,
    val title: String?,
    @Json(name = "main_picture")
    val mainPicture: MainPicture?,
    @Json(name = "first_name")
    val firstName: String?,
    @Json(name = "last_name")
    val lastName: String?
) : Parcelable