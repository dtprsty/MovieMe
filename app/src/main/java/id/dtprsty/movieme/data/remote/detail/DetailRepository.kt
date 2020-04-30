package id.dtprsty.movieme.data.remote.detail

import id.dtprsty.movieme.BuildConfig
import id.dtprsty.movieme.data.remote.ApiService
import java.util.*

class DetailRepository(private val apiService: ApiService) {
    suspend fun getReviews(type: String, movieId: Int) = apiService.getReviews(type, movieId, Locale.getDefault().toString(), BuildConfig.API_KEY)
    suspend fun getVideos(type: String, movieId: Int) = apiService.getVideos(type, movieId, Locale.getDefault().toString(), BuildConfig.API_KEY)
}