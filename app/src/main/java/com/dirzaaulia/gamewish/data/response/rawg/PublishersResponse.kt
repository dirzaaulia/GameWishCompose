package com.dirzaaulia.gamewish.data.response.rawg

import com.dirzaaulia.gamewish.data.model.rawg.Publisher

data class PublishersResponse(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<Publisher>?
)