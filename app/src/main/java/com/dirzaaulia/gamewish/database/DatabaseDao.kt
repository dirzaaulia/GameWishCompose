package com.dirzaaulia.gamewish.database

import androidx.paging.PagingSource
import androidx.room.*
import com.dirzaaulia.gamewish.data.model.Wishlist
import kotlinx.coroutines.flow.Flow

@Dao
interface DatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wishlist: Wishlist)

    @Delete
    suspend fun delete(wishlist: Wishlist)

    @Query("SELECT * FROM wishlist_table")
    fun getAllWishlist(): PagingSource<Int, Wishlist>

    @Query("SELECT * FROM wishlist_table WHERE name LIKE '%' || :gameName || '%'")
    fun getFilteredWishlist(gameName: String): PagingSource<Int, Wishlist>

    @Query("SELECT * FROM wishlist_table WHERE id = :gameId LIMIT 1")
    fun getWishlist(gameId: Int): Flow<Wishlist>
}