package id.dtprsty.movieme.data.remote.tvshow

import com.google.gson.annotations.SerializedName

data class TvShowResponse(
    @SerializedName("results")
    val listTvShow: MutableList<TvShow> = mutableListOf()
)