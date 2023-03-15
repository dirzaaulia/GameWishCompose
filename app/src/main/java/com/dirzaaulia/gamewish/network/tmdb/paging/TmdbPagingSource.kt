package com.dirzaaulia.gamewish.network.tmdb.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dirzaaulia.gamewish.data.model.tmdb.Movie
import com.dirzaaulia.gamewish.data.model.tmdb.ServiceCode
import com.dirzaaulia.gamewish.utils.pagingSucceeded
import com.dirzaaulia.gamewish.repository.TmdbRepository
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.replaceIfNull

class TmdbPagingSource(
    private val repository: TmdbRepository,
    private val searchQuery: String,
    private val movieId: Long,
    private val serviceCode: ServiceCode,
): PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(OtherConstant.ONE)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(OtherConstant.ONE)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key.replaceIfNull(OtherConstant.ONE)
        return when (serviceCode) {
            ServiceCode.SEARCH_MOVIE -> {
                repository.searchMovie(searchQuery, page).pagingSucceeded { data ->
                    loadResult(data = data, page = page)
                }
            }
            ServiceCode.SEARCH_TV -> {
                repository.searchTv(searchQuery, page).pagingSucceeded { data ->
                    loadResult(data = data, page = page)
                }
            }
            ServiceCode.MOVIE_RECOMMENDATIONS -> {
                repository.getMovieRecommendations(movieId, page).pagingSucceeded { data ->
                    loadResult(data = data, page = page)
                }
            }
            ServiceCode.TV_RECOMMENDATIONS -> {
                repository.getTVRecommendations(movieId, page).pagingSucceeded { data ->
                    loadResult(data = data, page = page)
                }
            }
        }
    }

    private fun loadResult(data: List<Movie>, page: Int) = LoadResult.Page(
        data = data,
        prevKey = if (page == OtherConstant.ONE) null else page - OtherConstant.ONE,
        nextKey = if (data.isEmpty()) null else page.plus(OtherConstant.ONE)
    )
}