package com.dirzaaulia.gamewish.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dirzaaulia.gamewish.data.model.wishlist.GameWishlist
import com.dirzaaulia.gamewish.data.model.wishlist.MovieWishlist
import com.dirzaaulia.gamewish.database.DatabaseDao
import com.dirzaaulia.gamewish.utils.DatabaseConstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    private val dao: DatabaseDao
) {
    fun getGameWishlist(gameId: Long) = dao.getGameWishlist(gameId).distinctUntilChanged()

    fun getMovieWishlist(movieId: Long) = dao.getMovieWishlist(movieId).distinctUntilChanged()

    fun getTVShowWishlist(tvShowId: Long) = dao.getTVShowWishlist(tvShowId).distinctUntilChanged()

    fun getGameFilteredWishlist(query: String, status: String): Flow<PagingData<GameWishlist>> {
        return Pager(config = PagingConfig(pageSize = DatabaseConstant.DATABASE_PAGING_SIZE)) {
            dao.getGameFilteredWishlist(query, status)
        }.flow
    }

    fun getMovieFilteredWishlist(query: String, status: String): Flow<PagingData<MovieWishlist>> {
        return Pager(config = PagingConfig(pageSize = DatabaseConstant.DATABASE_PAGING_SIZE)) {
            dao.getMovieFilteredWishlist(query, status)
        }.flow
    }

    fun getTVShowFilteredWishlist(query: String, status: String): Flow<PagingData<MovieWishlist>> {
        return Pager(config = PagingConfig(pageSize = DatabaseConstant.DATABASE_PAGING_SIZE)) {
            dao.getTVShowFilteredWishlist(query, status)
        }.flow
    }

    suspend fun addToGameWishlist(gameWishlist: GameWishlist) {
        dao.insertGame(gameWishlist)
    }

    suspend fun deleteGameWishlist(gameWishlist: GameWishlist) {
        dao.deleteGame(gameWishlist)
    }

    suspend fun addToMovieWishlist(movieWishlist: MovieWishlist) {
        dao.insertMovie(movieWishlist)
    }

    suspend fun deleteMovieWishlist(movieWishlist: MovieWishlist) {
        dao.deleteMovie(movieWishlist)
    }
}