package id.dtprsty.movieme.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.dtprsty.movieme.R
import id.dtprsty.movieme.data.local.FavoriteMovie

class FavoriteAdapter(private val data: List<Any>, private val listener: IRecyclerView) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val ITEM_HEADER = 0
        const val ITEM_MOVIE = 1
    }


    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is String -> ITEM_HEADER
            is FavoriteMovie -> ITEM_MOVIE
            else -> throw IllegalArgumentException("Undefined view type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_HEADER -> TitleItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_title, parent, false)
            )
            ITEM_MOVIE -> MovieItemHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false),
                listener
            )
            else -> throw throw IllegalArgumentException("Undefined view type")
        }
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ITEM_HEADER -> {
                val headerHolder = holder as TitleItemViewHolder
                headerHolder.bind(data[position] as String)
            }
            ITEM_MOVIE -> {
                val itemHolder = holder as MovieItemHolder
                itemHolder.bind(data[position] as FavoriteMovie)
            }
            else -> throw IllegalArgumentException("Undefined view type")
        }
    }

}