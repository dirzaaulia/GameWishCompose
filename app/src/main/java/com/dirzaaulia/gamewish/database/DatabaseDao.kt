package com.dirzaaulia.gamewish.database

import androidx.paging.PagingSource
import androidx.room.*
import com.dirzaaulia.gamewish.data.model.wishlist.GameWishlist
import kotlinx.coroutines.flow.Flow

@Dao
interface DatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gameWishlist: GameWishlist)

    @Delete
    suspend fun delete(gameWishlist: GameWishlist)

    @Query("DELETE FROM wishlist_table WHERE id = :gameId")
    suspend fun deleteById(gameId: Long)

    @Query("SELECT * FROM wishlist_table")
    fun getAllWishlist(): PagingSource<Int, GameWishlist>

    @Query("SELECT * FROM wishlist_table WHERE name LIKE '%' || :gameName || '%' AND status LIKE '%' || :status || '%'")
    fun getFilteredWishlist(gameName: String, status: String): PagingSource<Int, GameWishlist>

    @Query("SELECT * FROM wishlist_table WHERE id = :gameId LIMIT 1")
    fun getWishlist(gameId: Long): Flow<GameWishlist>
}