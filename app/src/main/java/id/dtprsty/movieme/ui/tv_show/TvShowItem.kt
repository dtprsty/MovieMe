package id.dtprsty.movieme.ui.tv_show

import android.app.Activity
import android.content.Intent
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import id.dtprsty.movieme.BuildConfig
import id.dtprsty.movieme.R
import id.dtprsty.movieme.data.remote.tvshow.TvShow
import id.dtprsty.movieme.ui.detail.DetailMovieActivity
import id.dtprsty.movieme.util.Constant
import id.dtprsty.movieme.util.DateHelper
import kotlinx.android.synthetic.main.item_movie.*

class TvShowItem(private val activity: Activity, private val tvShow: TvShow) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.tvMovieTitle.text = tvShow.title
        viewHolder.tvYear.text = DateHelper.dateToYear(tvShow.firstAirDate)
        Glide.with(viewHolder.itemView.context)
            .load("${BuildConfig.IMAGE_URL}${tvShow.poster}")
            .centerCrop()
            .dontAnimate()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .apply(
                RequestOptions.placeholderOf(R.drawable.img_placeholder)
            )
            .into(viewHolder.ivBackdrop)

        viewHolder.cardMovie.setOnClickListener {
            val intent = Intent(activity, DetailMovieActivity::class.java).apply {
                putExtra(DetailMovieActivity.EXTRA_TVSHOW, tvShow)
                putExtra(DetailMovieActivity.EXTRA_TYPE, Constant.TYPE_TVSHOW)
            }

            val activityOptions =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity,
                    Pair(
                        viewHolder.ivBackdrop,
                        DetailMovieActivity.EXTRA_POSTER_IMAGE
                    )
                )
            ActivityCompat.startActivity(activity, intent, activityOptions.toBundle())
        }
    }

    override fun getLayout(): Int {
        return R.layout.item_movie
    }
}