package id.dtprsty.movieme.data.remote.review

import id.dtprsty.movieme.BuildConfig
import id.dtprsty.movieme.data.local.FavoriteMovie
import id.dtprsty.movieme.data.local.MovieDao
import id.dtprsty.movieme.data.remote.ApiService
import id.dtprsty.movieme.util.Constant
import java.util.*

class MovieRepository(private val apiService: ApiService, private val movieDao: MovieDao) {

    suspend fun getNowPlaying() = apiService.getMovies(
        Constant.NOW_PLAYING,
        Locale.getDefault().toString(),
        BuildConfig.API_KEY
    )

    suspend fun getUpcoming() =
        apiService.getMovies(Constant.UPCOMING, Locale.getDefault().toString(), BuildConfig.API_KEY)

    suspend fun getPopular() =
        apiService.getMovies(Constant.POPULAR, Locale.getDefault().toString(), BuildConfig.API_KEY)

    suspend fun getReview(movieId: Int) =
        apiService.getMovieReview(movieId, Locale.getDefault().toString(), BuildConfig.API_KEY)

    fun movieById(movieId: Int) = movieDao.loadMoviewByIds(movieId)
    fun movies(type: String) = movieDao.loadAllMovie(type)
    suspend fun insert(favoriteMovie: FavoriteMovie) =
        movieDao.insetMovie(favoriteMovie)

    suspend fun deleteById(movieId: Int) = movieDao.delete(movieId)
}