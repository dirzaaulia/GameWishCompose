package com.dirzaaulia.gamewish.network.myanimelist.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dirzaaulia.gamewish.data.model.myanimelist.ParentNode
import com.dirzaaulia.gamewish.extension.pagingSucceeded
import com.dirzaaulia.gamewish.repository.MyAnimeListRepository

class MyAnimeListPagingSource(
    private val repository: MyAnimeListRepository,
    private val serviceCode: Int,
    private val accessToken: String,
    private val listStatus: String,
) : PagingSource<Int, ParentNode>() {

    override fun getRefreshKey(state: PagingState<Int, ParentNode>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ParentNode> {
        val offset = params.key ?: 0
        return when (serviceCode) {
            1 -> repository.getUserAnimeList(accessToken, listStatus, offset)
                .pagingSucceeded { data ->
                    LoadResult.Page(
                        data = data,
                        prevKey = if (offset == 0) null else offset - 1,
                        nextKey = if (data.isEmpty()) null else offset.plus(1)
                    )
                }
            else -> {
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }
        }
    }
}