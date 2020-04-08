package id.dtprsty.movieme.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.HttpException
import id.dtprsty.movieme.data.MovieRepository
import id.dtprsty.movieme.data.local.FavoriteMovie
import id.dtprsty.movieme.data.remote.review.ReviewResponse
import id.dtprsty.movieme.util.EspressoIdlingResource
import id.dtprsty.movieme.util.LoadingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import timber.log.Timber
import java.io.IOException

class DetailViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    val reviewResponse = MutableLiveData<ReviewResponse>()

    val loadingState = MutableLiveData<LoadingState>()

    val movie = MutableLiveData<FavoriteMovie>()

    var movieLocal = MutableLiveData<FavoriteMovie>()

    fun getReview(movieId: Int) {
        loadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val result = movieRepository.getReview(movieId)
                    Timber.d("Review RESPONSE $result")
                    reviewResponse.postValue(result)
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
                }finally {

                    loadingState.postValue(LoadingState.LOADED)
                }
            }
            EspressoIdlingResource.decrement()
        }
    }

    fun getMovieLocalById(movieId: Int) = viewModelScope.launch {
        EspressoIdlingResource.decrement()
        movieLocal.postValue(movieRepository.movieById(movieId))
    }

    fun insert(favoriteMovie: FavoriteMovie) = viewModelScope.launch {
        EspressoIdlingResource.decrement()
        movieRepository.insert(favoriteMovie)
    }


    fun delete(movieId: Int) = viewModelScope.launch {
        EspressoIdlingResource.decrement()
        movieRepository.deleteById(movieId)
    }


}