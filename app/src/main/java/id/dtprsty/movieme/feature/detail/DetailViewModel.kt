package id.dtprsty.movieme.feature.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.HttpException
import id.dtprsty.movieme.data.MovieRepository
import id.dtprsty.movieme.data.local.FavoriteMovie
import id.dtprsty.movieme.data.remote.review.ReviewResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import timber.log.Timber
import java.io.IOException

class DetailViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    val reviewResponse = MutableLiveData<ReviewResponse>()
    val error = MutableLiveData<String>()

    val movie = MutableLiveData<FavoriteMovie>()

    var movieLocal = MutableLiveData<FavoriteMovie>()

    fun getReview(movieId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val result = movieRepository.getReview(movieId)
                    Timber.d("Review RESPONSE $result")
                    reviewResponse.postValue(result)
                } catch (throwable: Throwable) {
                    when (throwable) {
                        is IOException -> {
                            error.postValue("Network error")
                        }
                        is HttpException -> {
                            val code = throwable.statusCode
                            val errorResponse = throwable.message
                            error.postValue("Error $code $errorResponse")
                        }
                        is JSONException -> {
                            error.postValue("Invalid json format")
                        }
                        else -> {
                            error.postValue("Unknown error")
                        }
                    }
                }
            }
        }
    }

    fun getMovieLocalById(movieId: Int) = viewModelScope.launch {
        movieLocal.postValue(movieRepository.movieById(movieId))
    }

    fun insert(favoriteMovie: FavoriteMovie) = viewModelScope.launch {
        movieRepository.insert(favoriteMovie)
    }


    fun delete(movieId: Int) = viewModelScope.launch {
        movieRepository.deleteById(movieId)
    }


}