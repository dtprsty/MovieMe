package id.dtprsty.movieme.data.remote

import id.dtprsty.movieme.data.remote.movie.MovieResponse
import id.dtprsty.movieme.data.remote.review.ReviewResponse
import id.dtprsty.movieme.data.remote.tvshow.TvShowResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/{filtering}")
    suspend fun getMovies(
        @Path("filtering") filtering: String,
        @Query("language") language: String,
        @Query("api_key") apiKey: String
    ): MovieResponse

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReview(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String,
        @Query("api_key") apiKey: String
    ): ReviewResponse

    @GET("tv/{filtering}")
    suspend fun getTvShows(
        @Path("filtering") filtering: String,
        @Query("language") language: String,
        @Query("api_key") apiKey: String
    ): TvShowResponse

}
