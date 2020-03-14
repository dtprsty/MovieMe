package id.dtprsty.movieme.data

import id.dtprsty.movieme.BuildConfig
import id.dtprsty.movieme.app.MyApp
import id.dtprsty.movieme.data.local.FavoriteMovie
import id.dtprsty.movieme.data.remote.ApiService
import id.dtprsty.movieme.data.remote.myApi
import id.dtprsty.movieme.util.Constant
import java.util.*

class MovieRepository {
    private var apiService: ApiService =
        myApi

    companion object {
        @Volatile
        private var remoteRepository: MovieRepository? = null

        fun geInstance(): MovieRepository {
            return remoteRepository
                ?: synchronized(this) {
                remoteRepository
                    ?: MovieRepository()
            }
        }
    }

    suspend fun getNowPlaying() = apiService.getMovies(Constant.NOW_PLAYING, Locale.getDefault().toString(), BuildConfig.API_KEY)
    suspend fun getUpcoming() = apiService.getMovies(Constant.UPCOMING, Locale.getDefault().toString(), BuildConfig.API_KEY)
    suspend fun getPopular() = apiService.getMovies(Constant.POPULAR, Locale.getDefault().toString(), BuildConfig.API_KEY)
    suspend fun getReview(movieId: Int) = apiService.getReview(movieId, Locale.getDefault().toString(), BuildConfig.API_KEY)

    suspend fun movieById(movieId: Int) = MyApp.mInstance.getMovieDao().loadMoviewByIds(movieId)
    suspend fun movies() = MyApp.mInstance.getMovieDao().loadAllMovie()
    suspend fun insert(favoriteMovie: FavoriteMovie) = MyApp.mInstance.getMovieDao().insetMovie(favoriteMovie)
    suspend fun deleteById(movieId: Int) = MyApp.mInstance.getMovieDao().delete(movieId)
}