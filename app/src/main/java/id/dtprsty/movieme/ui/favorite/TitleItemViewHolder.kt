package id.dtprsty.movieme.ui.favorite

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import id.dtprsty.movieme.R
import kotlinx.android.synthetic.main.item_title.*
import kotlinx.android.synthetic.main.item_title.view.*

class TitleItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(title: String) {
        itemView.tvType.text = title
    }
}