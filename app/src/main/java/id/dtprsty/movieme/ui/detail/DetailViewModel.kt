package id.dtprsty.movieme.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.HttpException
import id.dtprsty.movieme.data.local.FavoriteMovie
import id.dtprsty.movieme.data.local.FavoriteRepository
import id.dtprsty.movieme.data.remote.BaseResponse
import id.dtprsty.movieme.data.remote.detail.DetailRepository
import id.dtprsty.movieme.data.remote.detail.Review
import id.dtprsty.movieme.data.remote.detail.Video
import id.dtprsty.movieme.util.EspressoIdlingResource
import id.dtprsty.movieme.util.LoadingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import timber.log.Timber
import java.io.IOException

class DetailViewModel(
    private val favoriteRepository: FavoriteRepository,
    private val detailRepository: DetailRepository
) : ViewModel() {
    val reviewResponse = MutableLiveData<BaseResponse<MutableList<Review>>>()
    val videoResponse = MutableLiveData<BaseResponse<MutableList<Video>>>()

    val loadingState = MutableLiveData<LoadingState>()

    val movie = MutableLiveData<FavoriteMovie>()

    fun getMovieLocalById(movieId: Int) = favoriteRepository.movieById(movieId)

    fun insert(favoriteMovie: FavoriteMovie) = viewModelScope.launch {
        EspressoIdlingResource.decrement()
        favoriteRepository.insert(favoriteMovie)
    }

    fun delete(movieId: Int) = viewModelScope.launch {
        EspressoIdlingResource.decrement()
        favoriteRepository.deleteById(movieId)
    }

    fun fetchData(type: String, movieId: Int) {
        EspressoIdlingResource.increment()
        loadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val reviewReq = async { detailRepository.getReviews(type, movieId) }
                    val videoReq = async { detailRepository.getVideos(type, movieId) }

                    Timber.d("Review RESPONSE ${reviewReq.await()}")
                    Timber.d("Video  RESPONSE ${videoReq.await()}")

                    reviewResponse.postValue(reviewReq.await())
                    videoResponse.postValue(videoReq.await())
                } catch (throwable: Exception) {
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
                } finally {
                    loadingState.postValue(LoadingState.LOADED)
                }
            }
        }
    }
}