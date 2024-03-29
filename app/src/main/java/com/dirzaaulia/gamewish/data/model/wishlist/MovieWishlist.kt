package com.dirzaaulia.gamewish.data.model.wishlist

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Keep
@Entity(tableName = "movie_wishlist_table")
@Parcelize
data class MovieWishlist(
    @PrimaryKey
    var id: Long? = null,
    var name: String? = null,
    val image: String? = null,
    val status: String? = null,
    var type: String? = null,
) : Parcelable