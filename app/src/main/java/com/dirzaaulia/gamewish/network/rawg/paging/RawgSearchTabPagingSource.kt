package com.dirzaaulia.gamewish.network.rawg.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dirzaaulia.gamewish.data.model.rawg.Genre.Companion.toSearchTab
import com.dirzaaulia.gamewish.data.model.rawg.Platform.Companion.toSearchTab
import com.dirzaaulia.gamewish.data.model.rawg.Publisher.Companion.toSearchTab
import com.dirzaaulia.gamewish.data.model.rawg.SearchTab
import com.dirzaaulia.gamewish.data.model.rawg.SearchTabType
import com.dirzaaulia.gamewish.repository.RawgRepository
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.pagingSucceeded
import com.dirzaaulia.gamewish.utils.replaceIfNull

class RawgSearchTabPagingSource(
    private val repository: RawgRepository,
    private val searchTabType: SearchTabType
) : PagingSource<Int, SearchTab>() {

    override fun getRefreshKey(state: PagingState<Int, SearchTab>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(OtherConstant.ONE)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(OtherConstant.ONE)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchTab> {
        val page = params.key.replaceIfNull(OtherConstant.ONE)
        return when (searchTabType) {
            SearchTabType.GENRE -> repository.getGenres(page).pagingSucceeded { data ->
                    LoadResult.Page(
                        data = data.map { it.toSearchTab() },
                        prevKey = if (page == OtherConstant.ONE) null else page - OtherConstant.ONE,
                        nextKey = if (data.isEmpty()) null else page.plus(OtherConstant.ONE)
                    )
                }
            SearchTabType.PUBLISHER -> repository.getPublishers(page).pagingSucceeded { data ->
                LoadResult.Page(
                    data = data.map { it.toSearchTab() },
                    prevKey = if (page == OtherConstant.ONE) null else page - OtherConstant.ONE,
                    nextKey = if (data.isEmpty()) null else page.plus(OtherConstant.ONE)
                )
            }
            SearchTabType.PLATFORM -> repository.getPlatforms(page).pagingSucceeded { data ->
                LoadResult.Page(
                    data = data.map { it.toSearchTab() },
                    prevKey = if (page == OtherConstant.ONE) null else page - OtherConstant.ONE,
                    nextKey = if (data.isEmpty()) null else page.plus(OtherConstant.ONE)
                )
            }
        }
    }
}