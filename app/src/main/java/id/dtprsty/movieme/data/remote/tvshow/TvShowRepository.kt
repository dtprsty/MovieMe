package id.dtprsty.movieme.data.remote.tvshow

import id.dtprsty.movieme.BuildConfig
import id.dtprsty.movieme.data.remote.ApiService
import id.dtprsty.movieme.util.Constant
import java.util.*

class TvShowRepository(private val apiService: ApiService) {

    suspend fun getAiringToday() =
        apiService.getTvShows(
            Constant.AIRING_TODAY,
            Locale.getDefault().toString(),
            BuildConfig.API_KEY
        )

    suspend fun getOnTheAir() =
        apiService.getTvShows(
            Constant.ON_THE_AIR,
            Locale.getDefault().toString(),
            BuildConfig.API_KEY
        )
}