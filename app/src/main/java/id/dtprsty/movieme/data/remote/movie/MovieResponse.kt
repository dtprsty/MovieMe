package id.dtprsty.movieme.data.remote.movie

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("results")
    val listMovie: MutableList<Movie> = mutableListOf()
)