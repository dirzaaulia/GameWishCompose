package com.dirzaaulia.gamewish.database

import androidx.paging.PagingSource
import androidx.room.*
import com.dirzaaulia.gamewish.data.model.wishlist.GameWishlist
import com.dirzaaulia.gamewish.data.model.wishlist.MovieWishlist
import kotlinx.coroutines.flow.Flow

@Dao
interface DatabaseDao {
    @Insert(
        entity = GameWishlist::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertGame(gameWishlist: GameWishlist)

    @Insert(
        entity = MovieWishlist::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertMovie(movieWishlist: MovieWishlist)

    @Delete(entity = GameWishlist::class)
    suspend fun deleteGame(gameWishlist: GameWishlist)

    @Delete(entity = MovieWishlist::class)
    suspend fun deleteMovie(movieWishlist: MovieWishlist)

    @Query("DELETE FROM wishlist_table WHERE id = :gameId")
    suspend fun deleteById(gameId: Long)

    @Query("SELECT * FROM wishlist_table")
    fun getAllGameWishlist(): PagingSource<Int, GameWishlist>

    @Query("SELECT * FROM wishlist_table WHERE name LIKE '%' || :gameName || '%' AND status LIKE '%' || :status || '%'")
    fun getGameFilteredWishlist(gameName: String, status: String): PagingSource<Int, GameWishlist>

    @Query("SELECT * FROM wishlist_table WHERE id = :gameId LIMIT 1")
    fun getGameWishlist(gameId: Long): Flow<GameWishlist>

    @Query("SELECT * FROM movie_wishlist_table WHERE id = :movieId AND type = 'Movie' LIMIT 1")
    fun getMovieWishlist(movieId: Long): Flow<MovieWishlist>

    @Query("SELECT * FROM movie_wishlist_table WHERE id = :tvShowId AND type = 'TV Show' LIMIT 1")
    fun getTVShowWishlist(tvShowId: Long): Flow<MovieWishlist>

    @Query("SELECT * FROM movie_wishlist_table WHERE name LIKE '%' || :movieName || '%' AND status LIKE '%' || :status || '%' AND type = 'Movie'")
    fun getMovieFilteredWishlist(movieName: String, status: String): PagingSource<Int, MovieWishlist>

    @Query("SELECT * FROM movie_wishlist_table WHERE name LIKE '%' || :tvShowName || '%' AND status LIKE '%' || :status || '%' AND type = 'TV Show'")
    fun getTVShowFilteredWishlist(tvShowName: String, status: String): PagingSource<Int, MovieWishlist>
}