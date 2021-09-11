package com.dirzaaulia.gamewish.data.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "wishlist_table")
@Parcelize
data class Wishlist(
    @PrimaryKey
    var id: Long? = null,
    var name: String? = null,
    val image: String? = null,
    val status: String? = null
) : Parcelable {
    companion object {
        fun mock() = Wishlist(
            3328,
            "The Witcher 3: Wild Hult",
            "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg",
            "Plan To Buy"
        )
    }
}