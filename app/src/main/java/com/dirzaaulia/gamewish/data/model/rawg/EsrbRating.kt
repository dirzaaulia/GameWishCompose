package com.dirzaaulia.gamewish.data.model.rawg

import android.os.Parcelable
import androidx.annotation.Keep
import com.dirzaaulia.gamewish.R
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class EsrbRating(
    val id: Int?,
    val name: String?,
    val slug: String?,
    val name_en: String?
) : Parcelable {
    companion object {
        fun getRatingDrawable(data: EsrbRating): Int {
            return when (data.name) {
                "Everyone" -> {
                    R.drawable.image_esrb_rating_everyone
                }
                "Everyone 10+" -> {
                    R.drawable.image_esrb_rating_everyone10
                }
                "Teen" -> {
                    R.drawable.image_esrb_rating_teen
                }
                "Mature" -> {
                    R.drawable.image_esrb_rating_mature
                }
                "Adults Only" -> {
                    R.drawable.image_esrb_rating_adults_only
                }
                "Rating Pending" -> {
                    R.drawable.image_esrb_rating_pending
                }
                else -> R.drawable.image_esrb_rating_everyone
            }
        }
    }
}