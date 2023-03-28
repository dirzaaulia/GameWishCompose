package com.dirzaaulia.gamewish.data.model.wishlist

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dirzaaulia.gamewish.utils.OtherConstant
import kotlinx.parcelize.Parcelize

@Keep
@Entity(tableName = "movie_wishlist_table")
@Parcelize
data class MovieWishlist(
    @PrimaryKey
    var id: Long = OtherConstant.ZERO_LONG,
    var name: String = OtherConstant.EMPTY_STRING,
    val image: String = OtherConstant.EMPTY_STRING,
    val status: String = OtherConstant.EMPTY_STRING,
    var type: String = OtherConstant.EMPTY_STRING,
) : Parcelable