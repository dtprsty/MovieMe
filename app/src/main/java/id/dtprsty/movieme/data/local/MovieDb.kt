package id.dtprsty.movieme.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [FavoriteMovie::class], version = 2, exportSchema = false)
abstract class MovieDb : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var movieDb: MovieDb? = null

        fun getInstance(context: Context): MovieDb {
            return movieDb ?: synchronized(this) {
                movieDb ?: Room.databaseBuilder(
                    context.applicationContext,
                    MovieDb::class.java,
                    "movie_database"
                ).fallbackToDestructiveMigration()
                    .build()
            }
        }
    }
}