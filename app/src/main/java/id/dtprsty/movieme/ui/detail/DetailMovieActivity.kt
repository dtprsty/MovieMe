package id.dtprsty.movieme.ui.detail

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette
import com.google.android.material.snackbar.Snackbar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import id.dtprsty.movieme.BuildConfig
import id.dtprsty.movieme.R
import id.dtprsty.movieme.data.local.FavoriteMovie
import id.dtprsty.movieme.data.remote.movie.Movie
import id.dtprsty.movieme.data.remote.tvshow.TvShow
import id.dtprsty.movieme.util.DateHelper
import id.dtprsty.movieme.util.EspressoIdlingResource
import id.dtprsty.movieme.util.LoadingState
import id.dtprsty.movieme.util.requestGlideListener
import kotlinx.android.synthetic.main.activity_detail_movie.*
import kotlinx.android.synthetic.main.activity_detail_movie.rootView
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class DetailMovieActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_MOVIE = "extra_movie"
        const val EXTRA_TVSHOW = "extra_tvshow"
        const val EXTRA_TYPE = "type" //Movie or TV Show
    }

    private val viewModel: DetailViewModel by viewModel()
    private var groupReview = GroupAdapter<GroupieViewHolder>()

    private var isFavorite = false
    private var menu: Menu? = null
    private var favoriteMovie: FavoriteMovie? = null
    private lateinit var movie: Movie
    private lateinit var tvShow: TvShow
    private lateinit var type: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)
        init()
    }

    private fun init() {
        setToolbar()
        type = intent.getStringExtra(EXTRA_TYPE)
        if(type == "movie"){
            movie = intent.getParcelableExtra(EXTRA_MOVIE)
        }else if(type == "tv_show"){
            tvShow = intent.getParcelableExtra(EXTRA_TVSHOW)
        }
        setData(movie)
        EspressoIdlingResource.increment()
        viewModel.getReview(movie.id ?: 0)
        EspressoIdlingResource.increment()
        viewModel.getMovieLocalById(movie.id ?: 0)
        subscribe()
    }

    private fun subscribe() {
        viewModel.reviewResponse.observe(this, Observer {
            Timber.d("REVIEW $it")
            it?.listReview?.map {
                groupReview.add(ReviewItem(it))
            }
            reviewList()
            if (it.listReview.isNullOrEmpty()) {
                tvReview.visibility = View.GONE
            } else {
                tvReview.visibility = View.VISIBLE
            }
        })

        viewModel.movieLocal.observe(this, Observer {
            if (it != null) {
                favoriteMovie = it
                isFavorite = true
                if (menu != null)
                    setFavorite(menu!!)
            }
            Timber.d("FAVORITE MOVIE $it")
        })

        viewModel.loadingState.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    LoadingState.Status.RUNNING -> {
                        loading.visibility = View.VISIBLE
                        rootView.alpha = 0.2f
                        ivPoster.alpha = 0.2f
                    }
                    LoadingState.Status.SUCCESS -> {
                        loading.visibility = View.GONE
                        rootView.alpha = 1.0f
                        ivPoster.alpha = 1.0f
                    }
                    LoadingState.Status.FAILED -> Toast.makeText(
                        this,
                        it.msg,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun reviewList() {
        with(rvReview) {
            hasFixedSize()
            isNestedScrollingEnabled = false
            adapter = groupReview
        }

    }

    fun setToolbar() {
        toolbar.apply {
            title = "Movie Detail"
            setTitleTextColor(Color.WHITE)
        }
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        }
    }

    private fun setData(movie: Movie) {
        Glide.with(this)
            .load("${BuildConfig.IMAGE_URL}${movie.poster}")
            .listener(ivPoster.requestGlideListener())
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .apply(
                RequestOptions.placeholderOf(R.drawable.img_placeholder)
            )
            .into(ivPoster)

        Glide.with(this)
            .load("${BuildConfig.IMAGE_URL}${movie.backdrop}")
            .listener(
                GlidePalette.with("${BuildConfig.IMAGE_URL}${movie.poster}")
                    .use(BitmapPalette.Profile.VIBRANT)
                    .intoBackground(toolbar)
                    .crossfade(true)
            )
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .apply(
                RequestOptions.placeholderOf(R.drawable.img_placeholder)
            )
            .into(ivBackdrop)

        tvTitle.text = movie.title
        if(type == "movie") {
            tvDate.text = DateHelper.toSimpleString(movie.releaseDate)
        }else if(type == "tv_show"){
            tvDate.text = DateHelper.toSimpleString(tvShow.firstAirDate)
        }
        tvOverview.text = movie.overview
        tvRatings.text = movie.rating.toString()
        tvVoter.text = movie.voteCount
    }

    private fun addToFav() {
        if (favoriteMovie == null) {
            EspressoIdlingResource.increment()
            favoriteMovie = FavoriteMovie(
                movie.id, movie.voteCount, movie.poster, movie.backdrop, movie.title,
                movie.rating, movie.overview, movie.releaseDate
            )
            viewModel.insert(favoriteMovie!!)
            val snackbar: Snackbar = Snackbar
                .make(root, "Added To Favorite", Snackbar.LENGTH_LONG)
            snackbar.show()
        } else {
            Timber.d("Failed to add favorite")
        }

        menu?.getItem(0)?.setIcon(R.drawable.ic_added_to_fav)
        isFavorite = true
    }

    private fun removeFromFav() {
        if (favoriteMovie != null) {
            EspressoIdlingResource.increment()
            viewModel.delete(movie.id ?: 0)
            val snackbar: Snackbar = Snackbar
                .make(root, "Removed from Favorite", Snackbar.LENGTH_LONG)
            snackbar.show()
            menu?.getItem(0)?.setIcon(R.drawable.ic_add_to_fav)
            isFavorite = false
            if (menu != null)
                setFavorite(menu!!)
        }
    }

    private fun setFavorite(menu: Menu) {
        if (isFavorite) {
            menu.getItem(0).setIcon(R.drawable.ic_added_to_fav)
        } else {
            menu.getItem(0).setIcon(R.drawable.ic_add_to_fav)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_detail, menu)
        setFavorite(menu)
        this.menu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.add_to_favorite -> if (!isFavorite) {
                addToFav()
            } else {
                removeFromFav()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
