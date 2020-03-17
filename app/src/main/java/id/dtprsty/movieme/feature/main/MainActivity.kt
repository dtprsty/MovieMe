package id.dtprsty.movieme.feature.main

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import id.dtprsty.movieme.R
import id.dtprsty.movieme.data.remote.movie.Movie
import id.dtprsty.movieme.feature.IRecyclerView
import id.dtprsty.movieme.feature.detail.DetailMovieActivity
import id.dtprsty.movieme.util.EspressoIdlingResource
import id.dtprsty.movieme.util.SnapHelper
import id.dtprsty.movieme.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import timber.log.Timber

class MainActivity : AppCompatActivity(), IRecyclerView {

    private lateinit var viewModel: MainViewModel

    private var groupMovie = GroupAdapter<GroupieViewHolder>()
    private var groupHighlight = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        val factory = ViewModelFactory.getInstance()
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        setSpinner()
        showLoading(true)
        viewModel.getMovies(2)

        subscribe()
    }

    private fun highlightList() {
        with(rvHighlight) {
            hasFixedSize()
            isNestedScrollingEnabled = false
            adapter = groupHighlight
            onFlingListener = null
            setItemTransformer(
                ScaleTransformer.Builder()
                    .setMaxScale(1.05f)
                    .setMinScale(0.8f)
                    .setPivotX(Pivot.X.CENTER)
                    .setPivotY(Pivot.Y.BOTTOM)
                    .build()
            )

        }

        groupHighlight.notifyDataSetChanged()
    }

    private fun movieList() {
        groupMovie.notifyDataSetChanged()
        with(rvMovie) {
            hasFixedSize()
            isNestedScrollingEnabled = false
            adapter = groupMovie
            onFlingListener = null
        }
        SnapHelper().attachToRecyclerView(rvMovie)
    }

    private fun subscribe() {
        viewModel.movieResponse.observe(this, Observer {
            Timber.d("MOVIE $it")
            it?.listMovie?.map {
                groupMovie.add(MovieItem(it, this@MainActivity))
            }
            showLoading(false)
            movieList()
        })

        viewModel.movieHighlight.observe(this, Observer {
            groupHighlight.clear()
            for (i in 0 until 5) {
                groupHighlight.add(MovieHighlight(it.listMovie[i]))
            }
            highlightList()
        })

        viewModel.error.observe(this, Observer {
            if (it != null) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.movieFavorite.observe(this, Observer {
            if (spinner.selectedItemPosition == 3 && !it.isNullOrEmpty()) {
                for (i in it.indices) {
                    val movie = Movie(
                        id = it[i].id,
                        voteCount = it[i].voteCount,
                        poster = it[i].poster,
                        backdrop = it[i].backdrop,
                        title = it[i].title,
                        rating = it[i].rating,
                        overview = it[i].overview,
                        releaseDate = it[i].releaseDate
                    )
                    groupMovie.add(MovieItem(movie, this@MainActivity))
                }
            }
            movieList()
            showLoading(false)
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
            rootView.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            rootView.visibility = View.VISIBLE
        }
    }

    private fun setSpinner() {
        val data = resources.getStringArray(R.array.movie_category)

        val adapter = ArrayAdapter(
            this,
            R.layout.spinner_item_selected, data
        )
        adapter.setDropDownViewResource(R.layout.spinner_item_dropdown)

        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                showLoading(true)
                groupMovie.clear()
                if (position == 3) {
                    EspressoIdlingResource.increment()
                    viewModel.loadFavoriteMovie()
                } else {
                    viewModel.getMovies(position)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    override fun onClick(movie: Movie) {
        startActivity<DetailMovieActivity>(DetailMovieActivity.EXTRA_MOVIE to movie)
    }

    override fun onResume() {
        super.onResume()
        Timber.d("oResume")
        groupMovie.clear()
        if (spinner.selectedItemPosition == 3) {
            EspressoIdlingResource.increment()
            viewModel.loadFavoriteMovie()
        } else {
            viewModel.getMovies(spinner.selectedItemPosition)
        }
    }
}
