package id.dtprsty.movieme.feature.detail

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import id.dtprsty.movieme.R
import id.dtprsty.movieme.data.remote.review.Review
import kotlinx.android.synthetic.main.item_review.*

class ReviewItem(private val review: Review) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.tvAuthor.text = review.author
        viewHolder.tvContent.text = review.content
    }

    override fun getLayout(): Int {
        return R.layout.item_review
    }
}