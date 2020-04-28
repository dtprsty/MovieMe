package id.dtprsty.movieme.ui.favorite

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import id.dtprsty.movieme.BuildConfig
import id.dtprsty.movieme.R
import id.dtprsty.movieme.data.local.FavoriteMovie
import id.dtprsty.movieme.ui.detail.DetailMovieActivity
import id.dtprsty.movieme.util.Constant
import id.dtprsty.movieme.util.DateHelper
import kotlinx.android.synthetic.main.item_movie.view.*

class FavoriteAdapter internal constructor(
    private val activity: Activity
) :
    PagedListAdapter<FavoriteMovie, FavoriteAdapter.FavoriteViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<FavoriteMovie> =
            object : DiffUtil.ItemCallback<FavoriteMovie>() {
                override fun areItemsTheSame(
                    oldMovie: FavoriteMovie,
                    newMovie: FavoriteMovie
                ) = oldMovie.id == newMovie.id


                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldMovie: FavoriteMovie,
                    newMovie: FavoriteMovie
                ) = oldMovie == newMovie
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position) as FavoriteMovie)
    }

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: FavoriteMovie) {
            with(itemView) {
                tvMovieTitle.text = movie.title
                tvYear.text = DateHelper.dateToYear(movie.releaseDate)
                Glide.with(itemView.context)
                    .load("${BuildConfig.IMAGE_URL}${movie.poster}")
                    .centerCrop()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.img_placeholder)
                    )
                    .into(ivBackdrop)

                cardMovie.setOnClickListener {
                    val intent = Intent(activity, DetailMovieActivity::class.java).apply {
                        putExtra(DetailMovieActivity.EXTRA_MOVIE, movie)
                        putExtra(DetailMovieActivity.EXTRA_TYPE, Constant.TYPE_FAVORITE)
                    }

                    val activityOptions =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            activity,
                            Pair(
                                ivBackdrop,
                                DetailMovieActivity.EXTRA_POSTER_IMAGE
                            )
                        )
                    ActivityCompat.startActivity(activity, intent, activityOptions.toBundle())
                }
            }
        }

    }
}