package id.dtprsty.movieme.data.remote.movie

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    @SerializedName("vote_count")
    var voteCount: String? = null,
    @SerializedName("poster_path")
    var poster: String? = null,
    @SerializedName("id")
    var id: Int? = 0,
    @SerializedName("backdrop_path")
    var backdrop: String? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("vote_average")
    var rating: Double? = 0.0,
    @SerializedName("overview")
    var overview: String? = null,
    @SerializedName("release_date")
    var releaseDate: String? = null
) : Parcelable