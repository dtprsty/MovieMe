package id.dtprsty.movieme.ui.favorite

import androidx.lifecycle.ViewModel
import id.dtprsty.movieme.data.remote.review.MovieRepository

class FavoriteViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    fun movieFavorite(type: String) = movieRepository.movies(type)
}
