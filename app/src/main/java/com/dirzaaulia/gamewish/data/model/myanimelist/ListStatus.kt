package com.dirzaaulia.gamewish.data.model.myanimelist

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ListStatus(
    val status: String? = null,
    val score: Int? = null,
    @Json(name = "num_episodes_watched")
    val episodes: Int? = null,
    @Json(name = "is_rewatching")
    val isRewatching: Boolean? = null,
    @Json(name = "is_rereading")
    val isReReading: Boolean? = null,
    @Json(name = "num_chapters_read")
    val chapters: Int? = null
) : Parcelable