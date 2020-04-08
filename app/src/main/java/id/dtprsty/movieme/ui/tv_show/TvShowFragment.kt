package id.dtprsty.movieme.ui.tv_show

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder

import id.dtprsty.movieme.R
import id.dtprsty.movieme.data.remote.movie.Movie
import id.dtprsty.movieme.data.remote.tvshow.TvShow
import id.dtprsty.movieme.ui.detail.DetailMovieActivity
import id.dtprsty.movieme.util.EspressoIdlingResource
import kotlinx.android.synthetic.main.fragment_movie.*
import org.jetbrains.anko.startActivity
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TvShowFragment : Fragment(), IRecyclerView {

    private val viewModel = sharedViewModel<TvShowViewModel>()
    private var groupMovie = GroupAdapter<GroupieViewHolder>()

    companion object {
        fun newInstance() = TvShowFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tv_show_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity != null) {
            init()
        }
    }

    private fun init(){
        setSpinner()
    }

    private fun subscribe(){

    }


    private fun setSpinner() {
        val data = resources.getStringArray(R.array.tvshow_category)

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
                groupMovie.clear()
                if (position == 3) {
                    EspressoIdlingResource.increment()
                    //viewModel.loadFavoriteMovie()
                } else {
                    //viewModel.getMovies(position)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    override fun onClick(tvshow: TvShow) {
        context?.startActivity<DetailMovieActivity>(DetailMovieActivity.EXTRA_MOVIE to tvshow, DetailMovieActivity.EXTRA_TYPE to "tv_show")
    }

}

interface IRecyclerView {
    fun onClick(tvshow: TvShow)
}
