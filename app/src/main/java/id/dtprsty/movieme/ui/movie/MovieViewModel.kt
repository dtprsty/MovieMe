package id.dtprsty.movieme.ui.movie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.HttpException
import id.dtprsty.movieme.data.remote.BaseResponse
import id.dtprsty.movieme.data.remote.movie.Movie
import id.dtprsty.movieme.data.remote.movie.MovieRepository
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

    var movieResponse = MutableLiveData<BaseResponse<MutableList<Movie>>>()
    var movieHighlight = MutableLiveData<BaseResponse<MutableList<Movie>>>()

    fun getMovies(position: Int) {
        EspressoIdlingResource.increment()
        loadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    var result: BaseResponse<MutableList<Movie>>? = null
                    when (position) {
                        0 -> {
                            result = movieRepository.getNowPlaying()
                            movieResponse.postValue(result)
                        }
                        1 -> {
                            result = movieRepository.getUpcoming()
                            movieResponse.postValue(result)
                        }
                        2 -> {
                            result = movieRepository.getPopular()
                            movieHighlight.postValue(result)
                        }
                    }
                    Timber.d("MOVIE RESPONSE $result")
                } catch (throwable: Throwable) {
                    when (throwable) {
                        is IOException -> {
                            loadingState.postValue(LoadingState.error("Network error"))
                            Timber.d("error io ex")
                        }
                        is HttpException -> {
                            val code = throwable.statusCode
                            val errorResponse = throwable.message
                            loadingState.postValue(LoadingState.error("Error $code $errorResponse"))

                            Timber.d("error $code $errorResponse")
                        }
                        is JSONException -> {
                            loadingState.postValue(LoadingState.error("Invalid json format"))

                            Timber.d("error invalid json")
                        }
                        else -> {
                            loadingState.postValue(LoadingState.error("Unknown error"))
                            Timber.d("error unknown")
                        }
                    }
                } finally {
                    loadingState.postValue(LoadingState.LOADED)
                }

                if (!EspressoIdlingResource.getEspressoIdlingResource().isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
            }

        }
    }
}