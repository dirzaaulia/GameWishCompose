package com.dirzaaulia.gamewish.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dirzaaulia.gamewish.data.model.Wishlist
import com.dirzaaulia.gamewish.database.DatabaseDao
import com.dirzaaulia.gamewish.utils.DatabaseConstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    private val dao: DatabaseDao
) {
    fun getWishlist(gameId: Long) = dao.getWishlist(gameId).distinctUntilChanged()

//    suspend fun getFilteredWishlist(gameName : String) : Flow<List<Wishlist>> {
//        return dao.getFilteredWishlist(gameName)
//            .flowOn(Dispatchers.IO)
//            .conflate()
//    }

    fun getFilteredWishlist(query: String): Flow<PagingData<Wishlist>> {
        return Pager(config = PagingConfig(pageSize = DatabaseConstant.DATABASE_PAGING_SIZE)) {
            dao.getFilteredWishlist(query)
        }.flow
    }

    fun getAllWishlist(): Flow<PagingData<Wishlist>> {
        return Pager(config = PagingConfig(pageSize = DatabaseConstant.DATABASE_PAGING_SIZE)) {
            dao.getAllWishlist()
        }.flow
    }

    suspend fun addToWishlist(wishlist: Wishlist) {
        dao.insert(wishlist)
    }

    suspend fun deleteWishlist(wishlist: Wishlist) {
        dao.delete(wishlist)
    }

    suspend fun deleteWishlistById(gameId: Long) {
        return dao.deleteById(gameId)
    }
}