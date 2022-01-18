package com.dirzaaulia.gamewish.data.model.wishlist

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "movie_wishlist_table")
@Parcelize
data class MovieWishlist(
    @PrimaryKey
    var id: Long? = null,
    var name: String? = null,
    val image: String? = null,
    val status: String? = null,
    var type: String? = null,
) : Parcelable {
    companion object {
        fun mock() = MovieWishlist(
            24428,
            "The Avengers",
            "https://image.tmdb.org/t/p/original/nNmJRkg8wWnRmzQDe2FwKbPIsJV.jpg",
            "Plan To Watch",
            "Movie"
        )
    }
}