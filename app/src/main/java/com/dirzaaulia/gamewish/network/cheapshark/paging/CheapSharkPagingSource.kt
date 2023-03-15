package com.dirzaaulia.gamewish.network.cheapshark.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dirzaaulia.gamewish.data.model.cheapshark.Deals
import com.dirzaaulia.gamewish.data.request.cheapshark.DealsRequest
import com.dirzaaulia.gamewish.utils.pagingSucceeded
import com.dirzaaulia.gamewish.repository.CheapSharkRepository
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.replaceIfNull

class CheapSharkPagingSource(
    private val repository: CheapSharkRepository,
    private val request: DealsRequest
) : PagingSource<Int, Deals>() {

    override fun getRefreshKey(state: PagingState<Int, Deals>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(OtherConstant.ONE)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(OtherConstant.ONE)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Deals> {
        val page = params.key.replaceIfNull()
        return repository.getDeals(request, page).pagingSucceeded { data ->
            LoadResult.Page(
                data = data,
                prevKey = if (page == OtherConstant.ZERO) null else page - OtherConstant.ONE,
                nextKey = if (data.isEmpty()) null else page.plus(OtherConstant.ONE)
            )
        }
    }
}