package id.dtprsty.movieme.data.remote.movie

import id.dtprsty.movieme.BuildConfig
import id.dtprsty.movieme.data.remote.ApiService
import id.dtprsty.movieme.util.Constant
import java.util.*

class MovieRepository(private val apiService: ApiService) {

    suspend fun getNowPlaying() = apiService.getMovies(
        Constant.NOW_PLAYING,
        Locale.getDefault().toString(),
        BuildConfig.API_KEY
    )

    suspend fun getUpcoming() =
        apiService.getMovies(Constant.UPCOMING, Locale.getDefault().toString(), BuildConfig.API_KEY)

    suspend fun getPopular() =
        apiService.getMovies(Constant.POPULAR, Locale.getDefault().toString(), BuildConfig.API_KEY)
}