package id.dtprsty.movieme.ui.favorite

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import id.dtprsty.movieme.BuildConfig
import id.dtprsty.movieme.R
import id.dtprsty.movieme.data.local.FavoriteMovie
import id.dtprsty.movieme.util.DateHelper
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieItemHolder(itemView: View, private val listener: IRecyclerView) :
    RecyclerView.ViewHolder(itemView) {
    fun bind(movie: FavoriteMovie) {
        itemView.tvMovieTitle.text = movie.title
        itemView.tvYear.text = DateHelper.dateToYear(movie.releaseDate)
        Glide.with(itemView.context)
            .load("${BuildConfig.IMAGE_URL}${movie.backdrop}")
            .centerCrop()
            .dontAnimate()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .apply(
                RequestOptions.placeholderOf(R.drawable.img_placeholder)
            )
            .into(itemView.ivBackdrop)

        itemView.cardMovie.setOnClickListener {
            listener.onClick(movie)
        }

    }
}