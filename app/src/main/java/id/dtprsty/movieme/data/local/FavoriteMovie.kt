package id.dtprsty.movieme.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class FavoriteMovie(
    @PrimaryKey(autoGenerate = false) var id: Int? = null,
    @ColumnInfo(name = "vote_count")
    var voteCount: String? = null,
    @ColumnInfo(name = "poster_path")
    var poster: String? = null,
    @ColumnInfo(name = "backdrop_path")
    var backdrop: String? = null,
    @ColumnInfo(name = "title")
    var title: String? = null,
    @ColumnInfo(name = "vote_average")
    var rating: Double? = null,
    @ColumnInfo(name = " overview")
    var overview: String? = null,
    @ColumnInfo(name = "release_date")
    var releaseDate: String?,
    @ColumnInfo(name = "type")
    var type: String?
) : Parcelable