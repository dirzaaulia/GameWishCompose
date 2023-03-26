package com.dirzaaulia.gamewish.data.model.rawg

import android.os.Parcelable
import androidx.annotation.Keep
import com.dirzaaulia.gamewish.utils.replaceIfNull
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Publisher(
    val id: Int?,
    val name: String?,
    val slug: String?,
    @Json(name = "games_count")
    val gamesCount: String?,
    @Json(name = "image_background")
    val imageBackground: String?,
    val games: List<Games>?
) : Parcelable {
    companion object {
        fun Publisher.toSearchTab() = SearchTab(
            id = id.replaceIfNull(),
            type = SearchTabType.PUBLISHER,
            image = imageBackground.replaceIfNull(),
            name = name.replaceIfNull()
        )
    }
}