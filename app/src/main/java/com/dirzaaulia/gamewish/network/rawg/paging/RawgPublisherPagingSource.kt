package com.dirzaaulia.gamewish.network.rawg.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dirzaaulia.gamewish.data.model.rawg.Publisher
import com.dirzaaulia.gamewish.extension.pagingSucceeded
import com.dirzaaulia.gamewish.repository.RawgRepository

class RawgPublisherPagingSource(
    private val repository: RawgRepository
) : PagingSource<Int, Publisher>() {

    override fun getRefreshKey(state: PagingState<Int, Publisher>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Publisher> {
        val page = params.key ?: 1
        return repository.getPublishers(page).pagingSucceeded { data ->
            LoadResult.Page(
                data = data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.isEmpty()) null else page.plus(1)
            )
        }
    }
}