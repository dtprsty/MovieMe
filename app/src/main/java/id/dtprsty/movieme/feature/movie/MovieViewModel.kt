package id.dtprsty.movieme.feature.movie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.HttpException
import id.dtprsty.movieme.data.MovieRepository
import id.dtprsty.movieme.data.local.FavoriteMovie
import id.dtprsty.movieme.data.remote.movie.MovieResponse
import id.dtprsty.movieme.util.EspressoIdlingResource
import id.dtprsty.movieme.util.LoadingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import timber.log.Timber
import java.io.IOException

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    val loadingState = MutableLiveData<LoadingState>()

    val movieFavorite = MutableLiveData<MutableList<FavoriteMovie>>()
    var movieResponse = MutableLiveData<MovieResponse>()
    var movieHighlight = MutableLiveData<MovieResponse>()

    fun getMovies(position: Int) {
        EspressoIdlingResource.increment()
        loadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    var result: MovieResponse? = null
                    when (position) {
                        0 -> result = movieRepository.getNowPlaying()
                        1 -> result = movieRepository.getUpcoming()
                        2 -> {
                            result = movieRepository.getPopular()
                            movieHighlight.postValue(result)
                        }
                    }
                    Timber.d("MOVIE RESPONSE $result")
                    movieResponse.postValue(result)
                    loadingState.postValue(LoadingState.LOADED)
                } catch (throwable: Throwable) {
                    when (throwable) {
                        is IOException -> {
                            loadingState.postValue(LoadingState.error("Network error"))
                        }
                        is HttpException -> {
                            val code = throwable.statusCode
                            val errorResponse = throwable.message
                            loadingState.postValue(LoadingState.error("Error $code $errorResponse"))
                        }
                        is JSONException -> {
                            loadingState.postValue(LoadingState.error("Invalid json format"))
                        }
                        else -> {
                            loadingState.postValue(LoadingState.error("Unknown error"))
                        }
                    }
                }

                if (!EspressoIdlingResource.getEspressoIdlingResource().isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
            }

        }
    }

    fun loadFavoriteMovie() = viewModelScope.launch {
        val favoriteMovie = movieRepository.movies()
        movieFavorite.postValue(favoriteMovie)
        if (!EspressoIdlingResource.getEspressoIdlingResource().isIdleNow) {
            EspressoIdlingResource.decrement()
        }
    }
}