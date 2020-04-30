package id.dtprsty.movieme.data.remote

import id.dtprsty.movieme.data.remote.detail.Review
import id.dtprsty.movieme.data.remote.detail.Video
import id.dtprsty.movieme.data.remote.movie.Movie
import id.dtprsty.movieme.data.remote.tvshow.TvShow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/{filtering}")
    suspend fun getMovies(
        @Path("filtering") filtering: String,
        @Query("language") language: String,
        @Query("api_key") apiKey: String
    ): BaseResponse<MutableList<Movie>>

    @GET("{type}/{id}/reviews")
    suspend fun getReviews(
        @Path("type") type: String,
        @Path("id") movieId: Int,
        @Query("language") language: String,
        @Query("api_key") apiKey: String
    ): BaseResponse<MutableList<Review>>

    @GET("tv/{filtering}")
    suspend fun getTvShows(
        @Path("filtering") filtering: String,
        @Query("language") language: String,
        @Query("api_key") apiKey: String
    ): BaseResponse<MutableList<TvShow>>

    @GET("{type}/{id}/videos")
    suspend fun getVideos(
        @Path("type") type: String,
        @Path("id") movieId: Int,
        @Query("language") language: String,
        @Query("api_key") apiKey: String
    ): BaseResponse<MutableList<Video>>
}
