package id.dtprsty.movieme.feature.main

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import id.dtprsty.movieme.BuildConfig
import id.dtprsty.movieme.R
import id.dtprsty.movieme.data.remote.movie.Movie
import id.dtprsty.movieme.feature.IRecyclerView
import id.dtprsty.movieme.util.DateHelper
import kotlinx.android.synthetic.main.item_movie.*

class MovieItem(private val movie: Movie, private val listener: IRecyclerView) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.tvMovieTitle.text = movie.title
        viewHolder.tvYear.text = DateHelper.dateToYear(movie.releaseDate)
        Glide.with(viewHolder.itemView.context)
            .load("${BuildConfig.IMAGE_URL}${movie.backdrop}")
            .centerCrop()
            .dontAnimate()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .apply(
                RequestOptions.placeholderOf(R.drawable.img_placeholder)
            )
            .into(viewHolder.ivBackdrop)

        viewHolder.cardMovie.setOnClickListener {
            listener.onClick(movie)
        }
    }

    override fun getLayout(): Int {
        return R.layout.item_movie
    }
}