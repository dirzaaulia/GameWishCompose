package com.dirzaaulia.gamewish.network.rawg.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dirzaaulia.gamewish.data.model.rawg.Games
import com.dirzaaulia.gamewish.extension.pagingSucceeded
import com.dirzaaulia.gamewish.repository.RawgRepository

class RawgSearchPagingSource(
    private val repository: RawgRepository,
    private val query: String,
    private val genreId: Int?,
    private val publisherId: Int?,
    private val platformId: Int?
) : PagingSource<Int, Games>() {

    override fun getRefreshKey(state: PagingState<Int, Games>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Games> {
        val page = params.key ?: 1

        return when {
            genreId != null -> {
                repository.searchGames(
                    page = page,
                    query = query,
                    genreId = genreId,
                    publisherId = null,
                    platformId = null
                ).pagingSucceeded { data ->
                    LoadResult.Page(
                        data = data,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (data.isEmpty()) null else page.plus(1)
                    )
                }
            }
            publisherId != null -> {
                repository.searchGames(
                    page = page,
                    query = query,
                    genreId = null,
                    publisherId = publisherId,
                    platformId = null
                ).pagingSucceeded { data ->
                    LoadResult.Page(
                        data = data,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (data.isEmpty()) null else page.plus(1)
                    )
                }
            }
            platformId != null -> {
                repository.searchGames(
                    page = page,
                    query = query,
                    genreId = null,
                    publisherId = null,
                    platformId = platformId
                ).pagingSucceeded { data ->
                    LoadResult.Page(
                        data = data,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (data.isEmpty()) null else page.plus(1)
                    )
                }
            }
            else -> {
                repository.searchGames(
                    page = page,
                    query = query,
                    genreId = null,
                    publisherId = null,
                    platformId = null
                ).pagingSucceeded { data ->
                    LoadResult.Page(
                        data = data,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (data.isEmpty()) null else page.plus(1)
                    )
                }
            }
        }
    }
}