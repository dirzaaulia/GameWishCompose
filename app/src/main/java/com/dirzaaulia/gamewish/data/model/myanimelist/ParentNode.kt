package com.dirzaaulia.gamewish.data.model.myanimelist

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ParentNode(
    val node: Node? = null,
    @Json(name = "relation_type_formatted")
    val relationType: String? = null,
    @Json(name = "list_status")
    var listStatus: ListStatus? = null,
    val role: String? = null
) : Parcelable