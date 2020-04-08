package id.dtprsty.movieme.feature.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import id.dtprsty.movieme.R
import id.dtprsty.movieme.data.remote.movie.Movie
import id.dtprsty.movieme.feature.ContentItem
import id.dtprsty.movieme.feature.IRecyclerView
import id.dtprsty.movieme.feature.detail.DetailMovieActivity
import id.dtprsty.movieme.util.EspressoIdlingResource
import id.dtprsty.movieme.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_movie.*
import org.jetbrains.anko.startActivity
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class MovieFragment : Fragment(), IRecyclerView {

    private lateinit var viewModel: MovieViewModel

    private var groupMovie = GroupAdapter<GroupieViewHolder>()
    private var groupHighlight = GroupAdapter<GroupieViewHolder>()

    companion object {
        fun newInstance() = MovieFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity != null) {
            init()
        }
    }

    private fun init() {
        val factory = ViewModelFactory.getInstance()
        viewModel = ViewModelProvider(requireActivity(), factory)[MovieViewModel::class.java]
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
    }

    private fun subscribe() {
        viewModel.movieResponse.observe(viewLifecycleOwner, Observer {
            Timber.d("MOVIE $it")
            it?.listMovie?.map {
                groupMovie.add(
                    ContentItem(
                        it,
                        this@MovieFragment
                    )
                )
            }
            showLoading(false)
            movieList()
        })

        viewModel.movieHighlight.observe(viewLifecycleOwner, Observer {
            groupHighlight.clear()
            for (i in 0 until 5) {
                groupHighlight.add(
                    MovieHighlight(
                        it.listMovie[i]
                    )
                )
            }
            highlightList()
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.movieFavorite.observe(viewLifecycleOwner, Observer {
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
                    groupMovie.add(
                        ContentItem(
                            movie,
                            this@MovieFragment
                        )
                    )
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

        val adapter =
            ArrayAdapter(
                requireContext(),
                R.layout.spinner_item_selected, data
            )

        adapter?.setDropDownViewResource(R.layout.spinner_item_dropdown)

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
        context?.startActivity<DetailMovieActivity>(DetailMovieActivity.EXTRA_MOVIE to movie)
    }
}
