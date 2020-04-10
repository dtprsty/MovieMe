package id.dtprsty.movieme.data.remote

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("results")
    val data: T? = null
)