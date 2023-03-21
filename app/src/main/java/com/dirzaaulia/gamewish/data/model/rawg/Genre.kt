package com.dirzaaulia.gamewish.data.model.rawg

import android.os.Parcelable
import androidx.annotation.Keep
import com.dirzaaulia.gamewish.utils.replaceIfNull
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Genre(
    val id: Int? = null,
    val name: String? = null,
    val slug: String? = null,
    @Json(name = "games_count")
    val gamesCount: Int? = null,
    @Json(name = "image_background")
    val imageBackground: String? = null,
    val games: List<Games>? = null
) : Parcelable {
    companion object {
        fun Genre.toSearchTab() = SearchTab(
            id = id.replaceIfNull(),
            type = SearchTabType.GENRE,
            image = imageBackground.replaceIfNull(),
            name = name.replaceIfNull()
        )
    }
}