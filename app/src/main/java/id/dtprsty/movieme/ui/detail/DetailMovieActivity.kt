package id.dtprsty.movieme.ui.detail

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.LiveData
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
import id.dtprsty.movieme.util.*
import kotlinx.android.synthetic.main.activity_detail_movie.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class DetailMovieActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_MOVIE = "extra_movie"
        const val EXTRA_TVSHOW = "extra_tvshow"
        const val EXTRA_TYPE = "type" //Movie or TV Show
        const val EXTRA_POSTER_IMAGE = "detail:poster:image"
        const val EXTRA_BACKDROP_IMAGE = "detail:backdrop:image"
    }

    private val viewModel: DetailViewModel by viewModel()
    private var groupReview = GroupAdapter<GroupieViewHolder>()

    private var isFavorite = false
    private var menu: Menu? = null
    private var favoriteMovie: FavoriteMovie? = null
    private var movie: Movie? = null
    private var tvShow: TvShow? = null
    private lateinit var type: String
    private lateinit var movieFavorite: LiveData<FavoriteMovie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)
        init()
    }

    private fun init() {
        type = intent.getStringExtra(EXTRA_TYPE)
        setToolbar()
        if (type == Constant.TYPE_MOVIE) {
            movie = intent.getParcelableExtra(EXTRA_MOVIE)
            viewModel.getMovieReview(movie?.id ?: 0)
            movieFavorite = viewModel.getMovieLocalById(movie?.id ?: 0)
            tvReview.visibility = View.VISIBLE
        } else if (type == Constant.TYPE_TVSHOW) {
            tvShow = intent.getParcelableExtra(EXTRA_TVSHOW)
            movieFavorite = viewModel.getMovieLocalById(tvShow?.id ?: 0)
        } else if (type == Constant.TYPE_FAVORITE) {
            favoriteMovie = intent.getParcelableExtra(EXTRA_MOVIE)
            isFavorite = true
        }
        subscribe()
        setData()
    }

    private fun subscribe() {
        viewModel.reviewResponse.observe(this, Observer {
            Timber.d("REVIEW $it")
            it?.data?.map {
                groupReview.add(ReviewItem(it))
            }
            reviewList()
            if (it.data.isNullOrEmpty()) {
                tvReview.visibility = View.GONE
            } else {
                tvReview.visibility = View.VISIBLE
            }
        })

        if (type != Constant.TYPE_FAVORITE) {
            movieFavorite.observe(this, Observer {
                if (it != null) {
                    favoriteMovie = it
                    isFavorite = true
                    if (menu != null)
                        setFavorite(menu!!)
                }
                Timber.d("FAVORITE MOVIE $it")
            })
        }

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
        val title = if (type == Constant.TYPE_MOVIE) {
            "Movie Detail"
        } else if (type == Constant.TYPE_TVSHOW) {
            "TV Show Detail"
        } else if (type == Constant.TYPE_FAVORITE) {
            "Favorite Detail"
        } else {
            "Undefined"
        }
        toolbar.apply {
            this.title = title
            setTitleTextColor(Color.WHITE)
        }
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        }
    }

    private fun setData() {
        ViewCompat.setTransitionName(ivBackdrop, EXTRA_BACKDROP_IMAGE)
        ViewCompat.setTransitionName(ivPoster, EXTRA_POSTER_IMAGE)
        if (type == Constant.TYPE_MOVIE) {
            tvDate.text = DateHelper.toSimpleString(movie?.releaseDate)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                loadThumbnail("${BuildConfig.IMAGE_URL}${movie?.backdrop}", ivBackdrop)
            } else {
                loadFullSizeImage("${BuildConfig.IMAGE_URL}${movie?.backdrop}", ivBackdrop)
            }

            Glide.with(this)
                .load("${BuildConfig.IMAGE_URL}${movie?.poster}")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.img_placeholder)
                )
                .into(ivPoster)

            tvTitle.text = movie?.title
            tvOverview.text = movie?.overview
            tvRatings.text = movie?.rating.toString()
            tvVoter.text = movie?.voteCount
        } else if (type == Constant.TYPE_TVSHOW) {
            tvDate.text = DateHelper.toSimpleString(tvShow?.firstAirDate)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                loadThumbnail("${BuildConfig.IMAGE_URL}${tvShow?.backdrop}", ivBackdrop)
            } else {
                loadFullSizeImage("${BuildConfig.IMAGE_URL}${tvShow?.backdrop}", ivBackdrop)
            }

            Glide.with(this)
                .load("${BuildConfig.IMAGE_URL}${tvShow?.poster}")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.img_placeholder)
                )
                .into(ivPoster)

            tvTitle.text = tvShow?.title
            tvOverview.text = tvShow?.overview
            tvRatings.text = tvShow?.rating.toString()
            tvVoter.text = tvShow?.voteCount
        } else if (type == Constant.TYPE_FAVORITE) {
            tvDate.text = DateHelper.toSimpleString(favoriteMovie?.releaseDate)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                loadThumbnail("${BuildConfig.IMAGE_URL}${favoriteMovie?.backdrop}", ivBackdrop)
            } else {
                loadFullSizeImage("${BuildConfig.IMAGE_URL}${favoriteMovie?.backdrop}", ivBackdrop)
            }

            Glide.with(this)
                .load("${BuildConfig.IMAGE_URL}${favoriteMovie?.poster}")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.img_placeholder)
                )
                .into(ivPoster)

            tvTitle.text = favoriteMovie?.title
            tvOverview.text = favoriteMovie?.overview
            tvRatings.text = favoriteMovie?.rating.toString()
            tvVoter.text = favoriteMovie?.voteCount
        }

    }

    fun loadThumbnail(resource: String, imageView: ImageView) {
        Glide.with(this)
            .load(resource)
            .listener(
                GlidePalette.with("${BuildConfig.IMAGE_URL}${favoriteMovie?.poster}")
                    .use(BitmapPalette.Profile.VIBRANT)
                    .intoBackground(toolbar)
                    .crossfade(true)
            )
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(imageView)
    }

    fun loadFullSizeImage(resource: String, imageView: ImageView) {
        Glide.with(this)
            .load(resource)
            .listener(
                GlidePalette.with("${BuildConfig.IMAGE_URL}${favoriteMovie?.poster}")
                    .use(BitmapPalette.Profile.VIBRANT)
                    .intoBackground(toolbar)
                    .crossfade(true)
            )
            .listener(ivPoster.requestGlideListener())
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(imageView)
    }


    private fun addToFav() {
        if (favoriteMovie == null) {
            EspressoIdlingResource.increment()
            if (type == Constant.TYPE_MOVIE) {
                favoriteMovie = FavoriteMovie(
                    movie?.id, movie?.voteCount, movie?.poster, movie?.backdrop, movie?.title,
                    movie?.rating, movie?.overview, movie?.releaseDate, Constant.TYPE_MOVIE
                )
            } else if (type == Constant.TYPE_TVSHOW) {
                favoriteMovie = FavoriteMovie(
                    tvShow?.id, tvShow?.voteCount, tvShow?.poster, tvShow?.backdrop, tvShow?.title,
                    tvShow?.rating, tvShow?.overview, tvShow?.firstAirDate, Constant.TYPE_TVSHOW
                )
            }
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
        EspressoIdlingResource.increment()
        viewModel.delete(favoriteMovie?.id ?: 0)
        val snackbar: Snackbar = Snackbar
            .make(root, "Removed from Favorite", Snackbar.LENGTH_LONG)
        snackbar.show()
        menu?.getItem(0)?.setIcon(R.drawable.ic_add_to_fav)
        isFavorite = false
        if (menu != null)
            setFavorite(menu!!)
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
        this.menu = menu
        setFavorite(menu)
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
