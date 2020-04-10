package id.dtprsty.movieme.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import id.dtprsty.movieme.R
import id.dtprsty.movieme.data.local.FavoriteMovie
import id.dtprsty.movieme.ui.detail.DetailMovieActivity
import id.dtprsty.movieme.util.Constant
import kotlinx.android.synthetic.main.favorite_fragment.*
import org.jetbrains.anko.startActivity
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class FavoriteFragment : Fragment(), IRecyclerView {

    private lateinit var favoriteAdapter: FavoriteAdapter

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
        viewModel.movieFavorite(Constant.TYPE_MOVIE)
        subscribe()
        setSpinner()
        favoriteAdapter = FavoriteAdapter(this)
        setRecyclerView()
    }

    private fun subscribe() {
        viewModel.movieFavorite(Constant.TYPE_MOVIE)
            .observe(viewLifecycleOwner, Observer { movieList ->
                if (movieList != null) {
                    favoriteAdapter.submitList(movieList)
                    favoriteAdapter.notifyDataSetChanged()
                }
            })
    }

    private fun setRecyclerView() {
        with(rvMovie) {
            hasFixedSize()
            isNestedScrollingEnabled = false
            adapter = favoriteAdapter
            onFlingListener = null
        }
    }

    override fun onClick(favoriteMovie: FavoriteMovie) {
        context?.startActivity<DetailMovieActivity>(
            DetailMovieActivity.EXTRA_MOVIE to favoriteMovie,
            DetailMovieActivity.EXTRA_TYPE to Constant.TYPE_FAVORITE
        )
    }

    fun setSpinner() {
        val data = resources.getStringArray(R.array.favorite_category)

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
                if (position == 0) {
                    viewModel.movieFavorite(Constant.TYPE_MOVIE)
                        .observe(viewLifecycleOwner, Observer { movieList ->
                            if (movieList != null) {
                                favoriteAdapter.submitList(movieList)
                                favoriteAdapter.notifyDataSetChanged()
                            }
                        })
                } else {
                    viewModel.movieFavorite(Constant.TYPE_TVSHOW)
                        .observe(viewLifecycleOwner, Observer { movieList ->
                            if (movieList != null) {
                                favoriteAdapter.submitList(movieList)
                                favoriteAdapter.notifyDataSetChanged()
                            }
                        })
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }
}

interface IRecyclerView {
    fun onClick(favoriteMovie: FavoriteMovie)
}
