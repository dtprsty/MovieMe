package id.dtprsty.movieme.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDao {

    @Query("SELECT * FROM FavoriteMovie order by id ASC")
    suspend fun loadAllMovie(): MutableList<FavoriteMovie>

    @Query("SELECT * FROM FavoriteMovie where id IN (:movieId)")
    suspend fun loadMoviewByIds(movieId: Int): FavoriteMovie

    @Insert
    suspend fun insetMovie(vararg favoriteMovies: FavoriteMovie)

    @Query("DELETE FROM FavoriteMovie where id IN (:movieId)")
    suspend fun delete(movieId: Int)
}