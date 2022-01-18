package com.dirzaaulia.gamewish.data.model.myanimelist

import androidx.annotation.Keep

@Keep
data class Paging(
    val previous: String?,
    val next: String?
)