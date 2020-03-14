package id.dtprsty.movieme.data.remote.review

data class ReviewResponse(
    var listReview: MutableList<Review> = mutableListOf()
)