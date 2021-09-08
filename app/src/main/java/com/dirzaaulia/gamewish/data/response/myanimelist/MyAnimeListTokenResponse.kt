package com.dirzaaulia.gamewish.data.response.myanimelist

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class MyAnimeListTokenResponse (
    @Json(name = "token_type")
    val tokenType : String?,
    @Json(name = "expires_in")
    val expiresIn : Int?,
    @Json(name = "access_token")
    val accessToken : String?,
    @Json(name = "refresh_token")
    val refreshToken : String?,
    val error : String?,
    val message : String?,
    val hint : String?
) : Parcelable