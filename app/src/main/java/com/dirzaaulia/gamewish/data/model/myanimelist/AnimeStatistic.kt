package com.dirzaaulia.gamewish.data.model.myanimelist

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class AnimeStatistic(
    @Json(name = "num_items_watching")
    val numItemsWatching: Int?,
    @Json(name = "num_items_completed")
    val numItemsCompleted: Int?,
    @Json(name = "num_items_on_hold")
    val numItemsOnHold: Int?,
    @Json(name = "num_items_dropped")
    val numItemsDropped: Int?,
    @Json(name = "num_items_plan_to_watch")
    val numItemsPlanToWatch: Int?,
    @Json(name = "num_items")
    val numItems: Int?,
    @Json(name = "num_days_watched")
    val numDaysWatched: Float?,
    @Json(name = "num_days_watching")
    val numDaysWatching: Float?,
    @Json(name = "num_days_completed")
    val numDaysCompleted: Float?,
    @Json(name = "num_days_on_hold")
    val numDaysOnHold: Float?,
    @Json(name = "num_days_dropped")
    val numDaysDropped: Float?,
    @Json(name = "num_days")
    val numDays: Float?,
    @Json(name = "num_episodes")
    val numEpisodes: Int?,
    @Json(name = "num_times_rewatched")
    val numTitlesRewatched: Int?,
    @Json(name = "mean_score")
    val meanScore: Float?
)