package com.dirzaaulia.gamewish.network.rawg.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dirzaaulia.gamewish.data.model.rawg.Genre
import com.dirzaaulia.gamewish.utils.pagingSucceeded
import com.dirzaaulia.gamewish.repository.RawgRepository
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.replaceIfNull

class RawgGenrePagingSource(
    private val repository: RawgRepository,
) : PagingSource<Int, Genre>() {

    override fun getRefreshKey(state: PagingState<Int, Genre>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(OtherConstant.ONE)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(OtherConstant.ONE)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Genre> {
        val page = params.key.replaceIfNull(OtherConstant.ONE)
        return repository.getGenres(page).pagingSucceeded { data ->
            LoadResult.Page(
                data = data,
                prevKey = if (page == OtherConstant.ONE) null else page - OtherConstant.ONE,
                nextKey = if (data.isEmpty()) null else page.plus(OtherConstant.ONE)
            )
        }
    }
}