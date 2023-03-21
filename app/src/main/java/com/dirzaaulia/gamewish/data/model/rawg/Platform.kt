package com.dirzaaulia.gamewish.data.model.rawg

import android.os.Parcelable
import androidx.annotation.Keep
import com.dirzaaulia.gamewish.utils.replaceIfNull
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Platform(
    val id: Int?,
    val platform: Int?,
    val name: String?,
    val slug: String?,
    @Json(name = "games_count")
    val gamesCount: Int?,
    @Json(name = "image_background")
    val imageBackground: String?,
    val games: List<Games>?
) : Parcelable {
    companion object {
        fun Platform.toSearchTab() = SearchTab(
            id = id.replaceIfNull(),
            type = SearchTabType.GENRE,
            image = imageBackground.replaceIfNull(),
            name = name.replaceIfNull()
        )
    }
}