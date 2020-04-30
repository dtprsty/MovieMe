package id.dtprsty.movieme.data.remote.detail

import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("author") var author: String? = null,
    @SerializedName("content") var content: String? = null
)