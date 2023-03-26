package com.dirzaaulia.gamewish.network.rawg.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dirzaaulia.gamewish.data.model.rawg.Games
import com.dirzaaulia.gamewish.utils.pagingSucceeded
import com.dirzaaulia.gamewish.repository.RawgRepository
import com.dirzaaulia.gamewish.utils.OtherConstant
import com.dirzaaulia.gamewish.utils.replaceIfNull
import timber.log.Timber

class RawgSearchPagingSource(
    private val repository: RawgRepository,
    private val query: String,
    private val genreId: Int?,
    private val publisherId: Int?,
    private val platformId: Int?
) : PagingSource<Int, Games>() {

    override fun getRefreshKey(state: PagingState<Int, Games>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(OtherConstant.ONE)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(OtherConstant.ONE)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Games> {
        val page = params.key.replaceIfNull(OtherConstant.ONE)
        Timber.d("HAYDEN TAG | Genre : %d, Publisher : %d, Platform : %d", genreId, publisherId, platformId)

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
                        prevKey = if (page == OtherConstant.ONE) null else page - OtherConstant.ONE,
                        nextKey = if (data.isEmpty()) null else page.plus(OtherConstant.ONE)
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
                        prevKey = if (page == OtherConstant.ONE) null else page - OtherConstant.ONE,
                        nextKey = if (data.isEmpty()) null else page.plus(OtherConstant.ONE)
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
                        prevKey = if (page == OtherConstant.ONE) null else page - OtherConstant.ONE,
                        nextKey = if (data.isEmpty()) null else page.plus(OtherConstant.ONE)
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
                        prevKey = if (page == OtherConstant.ONE) null else page - OtherConstant.ONE,
                        nextKey = if (data.isEmpty()) null else page.plus(OtherConstant.ONE)
                    )
                }
            }
        }
    }
}