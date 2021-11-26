package com.dirzaaulia.gamewish.network.tmdb.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dirzaaulia.gamewish.data.model.tmdb.Movie
import com.dirzaaulia.gamewish.extension.pagingSucceeded
import com.dirzaaulia.gamewish.repository.TmdbRepository

class TmdbPagingSource (
    private val repository: TmdbRepository,
    private val searchQuery: String,
    private val serviceCode: Int,
): PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return when (serviceCode) {
            1 -> {
                repository.searchMovie(searchQuery, page).pagingSucceeded { data ->
                    LoadResult.Page(
                        data = data,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (data.isEmpty()) null else page.plus(1)
                    )
                }
            }
            2 -> {
                repository.searchTv(searchQuery, page).pagingSucceeded { data ->
                    LoadResult.Page(
                        data = data,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (data.isEmpty()) null else page.plus(1)
                    )
                }
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