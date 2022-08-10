package com.dirzaaulia.gamewish.data.model.wishlist

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Keep
@Entity(tableName = "wishlist_table")
@Parcelize
data class GameWishlist(
    @PrimaryKey
    var id: Long? = null,
    var name: String? = null,
    val image: String? = null,
    val status: String? = null
) : Parcelable {
    companion object {
        fun mock() = GameWishlist(
            3328,
            "The Witcher 3: Wild Hult",
            "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg",
            "Plan To Buy"
        )
    }
}