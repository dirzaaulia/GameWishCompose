package com.dirzaaulia.gamewish.data.model.myanimelist

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListStatus(
    val status: String?,
    val score: Int?,
    @Json(name = "num_episodes_watched")
    val episodes: Int?,
    @Json(name = "is_rewatching")
    val isRewatching: Boolean?,
    @Json(name = "is_rereading")
    val isReReading: Boolean?,
    @Json(name = "num_chapters_read")
    val chapters: Int?
) : Parcelable