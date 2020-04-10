package id.dtprsty.movieme.data.remote.review

import id.dtprsty.movieme.BuildConfig
import id.dtprsty.movieme.data.remote.ApiService
import java.util.*

class ReviewRepository(private val apiService: ApiService) {
    suspend fun getReview(movieId: Int) =
        apiService.getMovieReview(movieId, Locale.getDefault().toString(), BuildConfig.API_KEY)
}