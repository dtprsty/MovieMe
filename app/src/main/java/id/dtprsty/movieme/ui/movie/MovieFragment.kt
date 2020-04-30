package id.dtprsty.movieme.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.yarolegovich.discretescrollview.transform.Pivot
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import id.dtprsty.movieme.R
import id.dtprsty.movieme.util.LoadingState
import kotlinx.android.synthetic.main.activity_detail_movie.*
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlinx.android.synthetic.main.fragment_movie.rootView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class MovieFragment : Fragment() {

    private val viewModel by sharedViewModel<MovieViewModel>()

    private val groupMovie = GroupAdapter<GroupieViewHolder>()
    private val groupHighlight = GroupAdapter<GroupieViewHolder>()

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
        setSpinner()
        setMovieRecyclerview()
        setHighlightRecyclerview()
        viewModel.getMovies(2)
        subscribe()
    }

    private fun setHighlightRecyclerview() {
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
    }

    private fun setMovieRecyclerview() {
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
            it?.data?.map {
                groupMovie.add(
                    MovieItem(
                        requireActivity(),
                        it
                    )
                )
            }
            groupMovie.notifyDataSetChanged()
        })

        viewModel.movieHighlight.observe(viewLifecycleOwner, Observer {
            groupHighlight.clear()
            for (i in 0 until 5) {
                groupHighlight.add(
                    MovieHighlightItem(
                        it.data?.get(i)
                    )
                )
            }
            groupHighlight.notifyDataSetChanged()
        })

        viewModel.loadingState.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                when (it.status) {
                    LoadingState.Status.RUNNING -> {
                        progressBar.visibility = View.VISIBLE
                        rootView.visibility = View.GONE
                    }
                    LoadingState.Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        rootView.visibility = View.VISIBLE
                    }
                    LoadingState.Status.FAILED -> {
                        val snackbar: Snackbar = Snackbar
                            .make(nestedScrollview, "${it.msg}", Snackbar.LENGTH_LONG)
                        snackbar.show()
                    }
                }
            }
        })
    }

    private fun setSpinner() {
        val data = resources.getStringArray(R.array.movie_category)

        val adapter =
            ArrayAdapter(
                requireContext(),
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
                groupMovie.clear()
                viewModel.getMovies(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }
}
