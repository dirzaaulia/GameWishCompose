package com.dirzaaulia.gamewish.data.model.myanimelist

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Details(
    val id: Int?,
    val title: String?,
    @Json(name = "alternative_titles")
    val alternativeTitles: AlternativeTitles?,
    @Json(name = "start_date")
    val startDate: String?,
    @Json(name = "end_date")
    val endDate: String?,
    val synopsis: String?,
    val mean: Float?,
    val rank: Long?,
    val popularity: Long?,
    @Json(name = "num_list_users")
    val members: Long?,
    @Json(name = "media_type")
    val mediaType: String?,
    val status: String?,
    val genres: List<Genre>,
    @Json(name = "my_list_status")
    var listStatus: ListStatus?,
    @Json(name = "num_episodes")
    val episodes: Int?,
    @Json(name = "num_chapters")
    val chapters: Int?,
    val authors: List<ParentNode>?,
    val source: String?,
    val rating: String?,
    val pictures: List<MainPicture>?,
    val background: String?,
    @Json(name = "related_anime")
    val relatedAnime: List<ParentNode>?,
    @Json(name = "related_manga")
    val relatedManga: List<ParentNode>?,
    val recommendations: List<ParentNode>?
) : Parcelable