package com.dirzaaulia.gamewish.data.response.myanimelist

import androidx.annotation.Keep
import com.dirzaaulia.gamewish.data.model.myanimelist.Paging
import com.dirzaaulia.gamewish.data.model.myanimelist.ParentNode
import com.dirzaaulia.gamewish.data.model.myanimelist.Season

@Keep
data class MyAnimeListSearchResponse(
    val data: List<ParentNode>?,
    val paging: Paging?,
    val season: Season?,
    val error: String?,
    val message: String?
)