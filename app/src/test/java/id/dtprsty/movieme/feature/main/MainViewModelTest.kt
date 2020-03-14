package id.dtprsty.movieme.feature.main

import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import id.dtprsty.movieme.BuildConfig
import id.dtprsty.movieme.data.MovieRepository
import id.dtprsty.movieme.data.local.FavoriteMovie
import id.dtprsty.movieme.data.remote.ApiService
import id.dtprsty.movieme.data.remote.movie.Movie
import id.dtprsty.movieme.data.remote.movie.MovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Mock
    private lateinit var movieRepository: MovieRepository
    @Mock
    private lateinit var viewModel: MainViewModel
    @Mock
    private lateinit var apiService: ApiService

    @Before
    fun setUp(){
        viewModel = MainViewModel(movieRepository)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun getMovieResponse() {
        val movies: MutableList<Movie> = mutableListOf()
        val response = MovieResponse(movies)

        viewModel.viewModelScope.launch{
            Mockito.`when`(movieRepository.getNowPlaying()).thenReturn(response)
            Mockito.`when`(apiService.getMovies(ArgumentMatchers.anyString(), Locale.getDefault().toString(), BuildConfig.API_KEY)).thenReturn(response)

            Mockito.verify(movieRepository).getNowPlaying()
            assertNotNull(viewModel.getMovies(0)) //Now Playing
            assertEquals(40, viewModel.movieResponse.value?.listMovie?.size)
        }
    }

    @Test
    fun getMovieFavorite() {
        val favoriteMovie: MutableList<FavoriteMovie> = mutableListOf()
        viewModel.viewModelScope.launch{
            Mockito.`when`(movieRepository.movies()).thenReturn(favoriteMovie)
            Mockito.verify(movieRepository).movies()
            assertNotNull(viewModel.getMovies(3)) //Now Playing
        }

    }

    @Test
    fun getMovieHighlight() {
        val movies: MutableList<Movie> = mutableListOf()
        val response = MovieResponse(movies)

        viewModel.viewModelScope.launch{
            Mockito.`when`(movieRepository.getPopular()).thenReturn(response)
            Mockito.`when`(apiService.getMovies(ArgumentMatchers.anyString(), Locale.getDefault().toString(), BuildConfig.API_KEY)).thenReturn(response)
            assertNotNull(viewModel.getMovies(2)) //Now Playing
            assertEquals(40, viewModel.movieResponse.value?.listMovie?.size)
            Mockito.verify(movieRepository).getPopular()
        }
    }
}