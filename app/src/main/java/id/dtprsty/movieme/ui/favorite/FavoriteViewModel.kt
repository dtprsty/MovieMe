package id.dtprsty.movieme.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.paging.toLiveData
import id.dtprsty.movieme.data.local.FavoriteRepository

class FavoriteViewModel(private val repo: FavoriteRepository) : ViewModel() {

    fun movieFavorite(type: String) =
        repo.movies(type).toLiveData(20)
}
