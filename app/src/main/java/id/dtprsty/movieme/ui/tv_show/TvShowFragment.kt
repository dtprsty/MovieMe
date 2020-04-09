package id.dtprsty.movieme.ui.tv_show

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import id.dtprsty.movieme.R
import id.dtprsty.movieme.data.remote.tvshow.TvShow
import id.dtprsty.movieme.ui.detail.DetailMovieActivity
import id.dtprsty.movieme.util.Constant
import id.dtprsty.movieme.util.EspressoIdlingResource
import id.dtprsty.movieme.util.LoadingState
import kotlinx.android.synthetic.main.tv_show_fragment.*
import org.jetbrains.anko.startActivity
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class TvShowFragment : Fragment(), IRecyclerView {

    private val viewModel by sharedViewModel<TvShowViewModel>()
    private val groupMovie = GroupAdapter<GroupieViewHolder>()

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

    private fun init() {
        setSpinner()
        tvShowList()
        viewModel.getTvShow(0)
        subscribe()
    }

    private fun subscribe() {
        viewModel.tvShowResponse?.observe(viewLifecycleOwner, Observer {
            Timber.d("TV Show $it")
            it?.listTvShow?.map {
                groupMovie.add(
                    TvShowItem(
                        it,
                        this@TvShowFragment
                    )
                )
            }
            groupMovie.notifyDataSetChanged()
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
                    LoadingState.Status.FAILED -> Toast.makeText(
                        context,
                        it.msg,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun tvShowList() {
        with(rvTvShow) {
            hasFixedSize()
            isNestedScrollingEnabled = false
            adapter = groupMovie
            onFlingListener = null
        }
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
                EspressoIdlingResource.increment()
                viewModel.getTvShow(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    override fun onClick(tvshow: TvShow) {
        context?.startActivity<DetailMovieActivity>(
            DetailMovieActivity.EXTRA_TVSHOW to tvshow,
            DetailMovieActivity.EXTRA_TYPE to Constant.TYPE_TVSHOW
        )
    }

}

interface IRecyclerView {
    fun onClick(tvshow: TvShow)
}
