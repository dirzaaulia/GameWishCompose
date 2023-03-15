package com.dirzaaulia.gamewish.network.myanimelist.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dirzaaulia.gamewish.data.model.myanimelist.ParentNode
import com.dirzaaulia.gamewish.data.model.myanimelist.ServiceCode
import com.dirzaaulia.gamewish.utils.pagingSucceeded
import com.dirzaaulia.gamewish.repository.MyAnimeListRepository
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.replaceIfNull

class MyAnimeListPagingSource(
    private val repository: MyAnimeListRepository,
    private val serviceCode: ServiceCode,
    private val accessToken: String,
    private val listStatus: String,
    private val seasonalQuery: String,
    private val searchQuery: String,
) : PagingSource<Int, ParentNode>() {

    override fun getRefreshKey(state: PagingState<Int, ParentNode>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(OtherConstant.ONE)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(OtherConstant.ONE)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ParentNode> {
        val offset = params.key.replaceIfNull()
        return when (serviceCode) {
            ServiceCode.USER_ANIME_LIST -> repository.getUserAnimeList(accessToken, listStatus, offset)
                .pagingSucceeded { data -> loadResult(data = data, offset = offset) }
            ServiceCode.USER_MANGA_LIST -> repository.getUserMangaList(accessToken, listStatus, offset)
                .pagingSucceeded { data -> loadResult(data = data, offset = offset) }
            ServiceCode.SEASONAL_ANIME -> repository.getSeasonalAnime(accessToken, offset, seasonalQuery)
                .pagingSucceeded { data -> loadResult(data = data, offset = offset) }
            ServiceCode.SEARCH_ANIME -> repository.searchAnime(accessToken, offset, searchQuery)
                .pagingSucceeded { data -> loadResult(data = data, offset = offset) }
            ServiceCode.SEARCH_MANGA -> repository.searchManga(accessToken, offset, searchQuery)
                .pagingSucceeded { data -> loadResult(data = data, offset = offset) }
        }
    }

    private fun loadResult(data: List<ParentNode>, offset: Int) = LoadResult.Page(
        data = data,
        prevKey = if (offset == OtherConstant.ZERO) null else offset - OtherConstant.TEN,
        nextKey = if (data.isEmpty()) null else offset.plus(OtherConstant.TEN)
    )
}