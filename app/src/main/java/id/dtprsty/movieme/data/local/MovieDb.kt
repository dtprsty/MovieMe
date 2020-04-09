package id.dtprsty.movieme.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import id.dtprsty.movieme.util.Constant


@Database(entities = [FavoriteMovie::class], version = Constant.DB_VERSION, exportSchema = false)
abstract class MovieDb : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}