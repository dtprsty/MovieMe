package id.dtprsty.movieme.ui.tv_show

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.HttpException
import id.dtprsty.movieme.data.remote.BaseResponse
import id.dtprsty.movieme.data.remote.tvshow.TvShow
import id.dtprsty.movieme.data.remote.tvshow.TvShowRepository
import id.dtprsty.movieme.util.EspressoIdlingResource
import id.dtprsty.movieme.util.LoadingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import timber.log.Timber
import java.io.IOException

class TvShowViewModel(private val tvShowRepository: TvShowRepository) : ViewModel() {

    val loadingState = MutableLiveData<LoadingState>()
    var tvShowResponse = MutableLiveData<BaseResponse<MutableList<TvShow>>>()

    fun getTvShow(position: Int) {
        EspressoIdlingResource.increment()
        loadingState.postValue(LoadingState.LOADING)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    var result: BaseResponse<MutableList<TvShow>>? = null
                    when (position) {
                        0 -> result = tvShowRepository.getAiringToday()
                        1 -> result = tvShowRepository.getOnTheAir()
                    }
                    tvShowResponse.postValue(result)
                    Timber.d("TV SHOW RESPONSE $result")
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
