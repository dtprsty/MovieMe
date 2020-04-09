package id.dtprsty.movieme.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import id.dtprsty.movieme.R
import id.dtprsty.movieme.data.local.FavoriteMovie
import id.dtprsty.movieme.ui.detail.DetailMovieActivity
import id.dtprsty.movieme.util.Constant
import kotlinx.android.synthetic.main.favorite_fragment.*
import org.jetbrains.anko.startActivity
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class FavoriteFragment : Fragment(), IRecyclerView {

    private lateinit var favoriteAdapter: FavoriteAdapter

    private var data: MutableList<Any> = mutableListOf()

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    private val viewModel by sharedViewModel<FavoriteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorite_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {
        subscribe()
        favoriteAdapter = FavoriteAdapter(data, this)
        movieList()
    }

    private fun subscribe() {
        viewModel.movieFavorite(Constant.TYPE_MOVIE).observe(viewLifecycleOwner, Observer { movie ->
            if (movie != null) {
                data.add("Movie")
                movie.indices.forEach { i ->
                    data.add(
                        FavoriteMovie(
                            movie[i].id,
                            movie[i].voteCount,
                            movie[i].poster,
                            movie[i].backdrop,
                            movie[i].title,
                            movie[i].rating,
                            movie[i].overview,
                            movie[i].releaseDate,
                            Constant.TYPE_MOVIE
                        )
                    )
                }
            }
            favoriteAdapter.notifyDataSetChanged()
        })
        viewModel.movieFavorite(Constant.TYPE_TVSHOW)
            .observe(viewLifecycleOwner, Observer { movie ->
                if (movie != null) {
                    data.add("TV Show")
                    movie.indices.forEach { i ->
                        data.add(
                            FavoriteMovie(
                                movie[i].id,
                                movie[i].voteCount,
                                movie[i].poster,
                                movie[i].backdrop,
                                movie[i].title,
                                movie[i].rating,
                                movie[i].overview,
                                movie[i].releaseDate,
                                Constant.TYPE_TVSHOW
                            )
                        )
                    }
                }
                favoriteAdapter.notifyDataSetChanged()
            })
    }

    private fun movieList() {
        var mLayoutManager = GridLayoutManager(requireActivity(), 2)
        mLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (data[position]) {
                    is String -> 2
                    is FavoriteMovie -> 1
                    else -> throw IllegalArgumentException("Undefined view type")
                }
            }
        }

        with(rvMovie) {
            hasFixedSize()
            isNestedScrollingEnabled = false
            adapter = favoriteAdapter
            onFlingListener = null
            layoutManager = mLayoutManager
        }
    }

    override fun onClick(favoriteMovie: FavoriteMovie) {
        context?.startActivity<DetailMovieActivity>(
            DetailMovieActivity.EXTRA_MOVIE to favoriteMovie,
            DetailMovieActivity.EXTRA_TYPE to Constant.TYPE_FAVORITE
        )
    }
}

interface IRecyclerView {
    fun onClick(favoriteMovie: FavoriteMovie)
}
