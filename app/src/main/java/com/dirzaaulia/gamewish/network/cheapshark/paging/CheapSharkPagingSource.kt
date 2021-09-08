package com.dirzaaulia.gamewish.network.cheapshark.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dirzaaulia.gamewish.data.model.cheapshark.Deals
import com.dirzaaulia.gamewish.data.request.cheapshark.DealsRequest
import com.dirzaaulia.gamewish.extension.pagingSucceeded
import com.dirzaaulia.gamewish.repository.CheapSharkRepository

class CheapSharkPagingSource (
    private val repository: CheapSharkRepository,
    private val request: DealsRequest
): PagingSource<Int, Deals>() {

    override fun getRefreshKey(state: PagingState<Int, Deals>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Deals> {
        val page = params.key ?: 0
        return repository.getDeals(request, page).pagingSucceeded { data ->
            LoadResult.Page(
                data = data,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (data.isEmpty()) null else page.plus(1)
            )
        }
    }
}