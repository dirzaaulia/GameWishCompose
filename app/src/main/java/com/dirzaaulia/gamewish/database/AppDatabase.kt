package com.dirzaaulia.gamewish.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dirzaaulia.gamewish.data.model.wishlist.GameWishlist
import com.dirzaaulia.gamewish.data.model.wishlist.MovieWishlist
import com.dirzaaulia.gamewish.utils.DatabaseConstant
import com.dirzaaulia.gamewish.utils.OtherConstant

@Database(
    entities = [GameWishlist::class, MovieWishlist::class],
    version = OtherConstant.FOUR,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun databaseDao(): DatabaseDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DatabaseConstant.DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}