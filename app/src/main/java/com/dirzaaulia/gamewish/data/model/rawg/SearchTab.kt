package com.dirzaaulia.gamewish.data.model.rawg

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dirzaaulia.gamewish.R
import com.dirzaaulia.gamewish.data.request.myanimelist.SearchGameRequest
import com.dirzaaulia.gamewish.utils.OtherConstant

enum class SearchTabType {
    GENRE,
    PUBLISHER,
    PLATFORM;

    companion object {
        @Composable
        fun SearchTabType.setRawgDataSource(): String {
            return when (this) {
                GENRE -> stringResource(id = R.string.genre_data_source)
                PUBLISHER -> stringResource(id = R.string.publisher_data_source)
                PLATFORM -> stringResource(id = R.string.platform_data_source)
            }
        }
    }
}

data class SearchTab(
    val id: Int = -1,
    val type: SearchTabType,
    val image: String = OtherConstant.EMPTY_STRING,
    val name: String = OtherConstant.EMPTY_STRING
) {
    companion object {
        fun SearchTab.toSearchGameRequest(searchQuery: String): SearchGameRequest {
            return when (this.type) {
                SearchTabType.GENRE -> SearchGameRequest(
                    searchQuery = searchQuery,
                    genreId = id,
                    publisherId = null,
                    platformId = null
                )
                SearchTabType.PUBLISHER -> SearchGameRequest(
                    searchQuery = searchQuery,
                    genreId = null,
                    publisherId = id,
                    platformId = null
                )
                SearchTabType.PLATFORM -> SearchGameRequest(
                    searchQuery = searchQuery,
                    genreId = null,
                    publisherId = null,
                    platformId = id
                )
            }
        }
    }
}