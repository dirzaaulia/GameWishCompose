package com.dirzaaulia.gamewish.network.rawg.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dirzaaulia.gamewish.data.model.rawg.Platform
import com.dirzaaulia.gamewish.extension.pagingSucceeded
import com.dirzaaulia.gamewish.repository.RawgRepository

class RawgPlatformPagingSource(
    private val repository: RawgRepository
) : PagingSource<Int, Platform>() {

    override fun getRefreshKey(state: PagingState<Int, Platform>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Platform> {
        val page = params.key ?: 1
        return repository.getPlatforms(page).pagingSucceeded { data ->
            LoadResult.Page(
                data = data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.isEmpty()) null else page.plus(1)
            )
        }
    }
}