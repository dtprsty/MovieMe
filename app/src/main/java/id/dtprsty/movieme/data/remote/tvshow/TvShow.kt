package id.dtprsty.movieme.data.remote.tvshow

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TvShow(
    @SerializedName("vote_count")
    var voteCount: String? = null,
    @SerializedName("poster_path")
    var poster: String? = null,
    @SerializedName("id")
    var id: Int? = 0,
    @SerializedName("backdrop_path")
    var backdrop: String? = null,
    @SerializedName("original_name")
    var title: String? = null,
    @SerializedName("vote_average")
    var rating: Double? = 0.0,
    @SerializedName("overview")
    var overview: String? = null,
    @SerializedName("first_air_date")
    var firstAirDate: String? = null
) : Parcelable