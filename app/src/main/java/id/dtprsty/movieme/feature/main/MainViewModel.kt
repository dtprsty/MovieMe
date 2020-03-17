package id.dtprsty.movieme.feature.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.HttpException
import id.dtprsty.movieme.data.MovieRepository
import id.dtprsty.movieme.data.local.FavoriteMovie
import id.dtprsty.movieme.data.remote.movie.MovieResponse
import id.dtprsty.movieme.util.EspressoIdlingResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import timber.log.Timber
import java.io.IOException

class MainViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    val movieResponse = MutableLiveData<MovieResponse>()
    val movieFavorite = MutableLiveData<MutableList<FavoriteMovie>>()
    val movieHighlight = MutableLiveData<MovieResponse>()
    val error = MutableLiveData<String>()

    fun getMovies(position: Int){
        EspressoIdlingResource.increment()
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    var result: MovieResponse? = null
                    when(position){
                        0 -> result = movieRepository.getNowPlaying()
                        1 -> result = movieRepository.getUpcoming()
                        2 -> {
                            result = movieRepository.getPopular()
                            movieHighlight.postValue(result)
                        }
                    }
                    Timber.d("MOVIE RESPONSE $result")
                    movieResponse.postValue(result)
                }catch (throwable: Throwable){
                    when(throwable){
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
                        else ->{
                            error.postValue("Unknown error")
                        }
                    }
                }

                if(!EspressoIdlingResource.getEspressoIdlingResource().isIdleNow){
                    EspressoIdlingResource.decrement()
                }
            }
        }
    }

    fun loadFavoriteMovie() = viewModelScope.launch {
        val favoriteMovie = movieRepository.movies()
        movieFavorite.postValue(favoriteMovie)
        if(!EspressoIdlingResource.getEspressoIdlingResource().isIdleNow){
            EspressoIdlingResource.decrement()
        }
    }
}