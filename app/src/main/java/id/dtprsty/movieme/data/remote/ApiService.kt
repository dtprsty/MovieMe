package id.dtprsty.movieme.data.remote

import id.dtprsty.movieme.BuildConfig
import id.dtprsty.movieme.data.remote.movie.MovieResponse
import id.dtprsty.movieme.data.remote.review.ReviewResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/{filtering}")
    suspend fun getMovies(
        @Path("filtering") filtering: String,
        @Query("language") language: String,
        @Query("api_key") apiKey: String): MovieResponse

    @GET("movie/{movie_id}/reviews")
    suspend fun getReview(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String,
        @Query("api_key") apiKey: String): ReviewResponse

}

val myApi: ApiService by lazy {
    Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}
